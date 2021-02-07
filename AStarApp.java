import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/*
 * Простое приложение Swing для демонстрации алгоритма поиска пути A *. В
  * пользователю предоставляется карта, которая содержит начал-ю и конечную точки. Пользователь
  * может рисовать или устранять препятствия на карте, а затем нажимать кнопку, чтобы вычислить
  * путь от начала до конца с использованием алгоритма поиска пути A *. Если путь
  * найден, отображается зеленым цветом.
 */
public class AStarApp {

    /*Количество ячеек сетки по оси X.*/
    private int width;
    
    /*Количество ячеек сетки в направлении Y. */
    private int height;
    
    /* Место, откуда начинается путь. */
    private Location startLoc;
    
    /*
     * Место, где путь закончится. **/
    private Location finishLoc;
    
    /*
     * Это двумерный массив компонентов пользовательского интерфейса,
     * которые обеспечивают отображение и управление
      * ячеек на карте.*/
    private JMapCell[][] mapCells;

    
    /*
     * Этот внутренний класс обрабатывает события мыши в основной сетке ячеек карты путем
      * изменение ячеек на основе состояния кнопки мыши и начального редактирования
      * что было выполнено.
     */
    private class MapCellHandler implements MouseListener
    {
        /*
          * Это значение будет истинным, если была нажата кнопка мыши и мы
          * в настоящее время в процессе модификации.
         */
        private boolean modifying;
        
        /*
         * Это значение указывает, делаем ли мы ячейки проходимыми или
          * непроходимыми. Это зависит от исходного состояния ячейки
          * что операция была начата внутри.
         */
        private boolean makePassable;
        
        /*Запускает операцию модификации.*/
        public void mousePressed(MouseEvent e)
        {
            modifying = true;
            
            JMapCell cell = (JMapCell) e.getSource();

            // Если текущая ячейка проходима, то делаем их
             // непроходимыми если непроходимо, то делаем их проходимыми.
            
            makePassable = !cell.isPassable();
            
            cell.setPassable(makePassable);
        }

        /*Завершает операцию модификации. */
        public void mouseReleased(MouseEvent e)
        {
            modifying = false;
        }
        
        /*Если была нажата мышь, модификация продолжается.операция в новой ячейке.*/
        public void mouseEntered(MouseEvent e)
        {
            if (modifying)
            {
                JMapCell cell = (JMapCell) e.getSource();
                cell.setPassable(makePassable);
            }
        }

        /* Не требуется для этого обработчика. */
        public void mouseExited(MouseEvent e)
        {
            //  Это игнорируем.
        }
        
        /* Не требуется для этого обработчика */
        public void mouseClicked(MouseEvent e)
        {
            // И этот тоже.
        }
    }
    
    
    /*Создает новый экземпляр AStarApp с указанной шириной карты и рост.*/
    public AStarApp(int w, int h) {
        if (w <= 0)
            throw new IllegalArgumentException("w must be > 0; got " + w);
        
        if (h <= 0)
            throw new IllegalArgumentException("h must be > 0; got " + h);
        
        width = w;
        height = h;
        
        startLoc = new Location(2, h / 2);
        finishLoc = new Location(w - 3, h / 2);
    }
    
    
    /*Простой вспомогательный метод для настройки пользовательского интерфейса Swing.
    Это называетсяиз поток обработчика событий Swing для обеспечения безопасности потоков.*/
    private void initGUI()
    {
        JFrame frame = new JFrame("Pathfinder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        contentPane.setLayout(new BorderLayout());

        // Используйте GridBagLayout, потому что он действительно учитывает предпочтительный размер
         // определяется компонентами, которые он размещает.
        GridBagLayout gbLayout = new GridBagLayout();
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 1;
        gbConstraints.insets.set(0, 0, 1, 1);

        JPanel mapPanel = new JPanel(gbLayout);
        mapPanel.setBackground(Color.GRAY);
        
        mapCells = new JMapCell[width][height];
        
        MapCellHandler cellHandler = new MapCellHandler();
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                mapCells[x][y] = new JMapCell();

                gbConstraints.gridx = x;
                gbConstraints.gridy = y;

                gbLayout.setConstraints(mapCells[x][y], gbConstraints);
                
                mapPanel.add(mapCells[x][y]);
                mapCells[x][y].addMouseListener(cellHandler);
            }
        }
        
        contentPane.add(mapPanel, BorderLayout.CENTER);
        
        JButton findPathButton = new JButton("Find Path");
        findPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { findAndShowPath(); }
        });
        
        contentPane.add(findPathButton, BorderLayout.SOUTH);
        
        frame.pack();
        frame.setVisible(true);

        mapCells[startLoc.xCoord][startLoc.yCoord].setEndpoint(true);
        mapCells[finishLoc.xCoord][finishLoc.yCoord].setEndpoint(true);
    }

    
    /*Запускает приложение. Вызывается из метода {@link #main} */
    private void start()
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { initGUI(); }
        });
    }
    

    /**
     * * Этот вспомогательный метод пытается вычислить путь, используя текущую карту
      * штат. Реализация довольно медленная; новый объект {@link Map2D}
      * создан и инициализирован из текущего состояния приложения. Тогда A *
      * вызывается pathfinder, и если путь найден, отображение обновляется до
      * показать найденный путь. (Лучшее решение будет использовать модель
      * Просмотр шаблона проектирования контроллера.)
     **/
    private void findAndShowPath()
    {
        // Создаем объект Map2D, содержащий текущее состояние пользовательского ввода.

        Map2D map = new Map2D(width, height);
        map.setStart(startLoc);
        map.setFinish(finishLoc);
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                mapCells[x][y].setPath(false);

                if (mapCells[x][y].isPassable())
                    map.setCellValue(x, y, 0);
                else
                    map.setCellValue(x, y, Integer.MAX_VALUE);
            }
        }

        // Пытаемся вычислить путь. Если можно вычислить, отметьте все ячейки в
         // дорожка.
        
        Waypoint wp = AStarPathfinder.computePath(map);
        
        while (wp != null)
        {
            Location loc = wp.getLocation();
            mapCells[loc.xCoord][loc.yCoord].setPath(true);
            
            wp = wp.getPrevious();
        }
    }
    
    
    /**
     * * Точка входа для приложения. Аргументы командной строки не указаны.
      * признано в настоящее время.
     **/
    public static void main(String[] args) {
        AStarApp app = new AStarApp(40, 30);
        app.start();
    }   
}
