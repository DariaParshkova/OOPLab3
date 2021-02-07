import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;


/**
 * This class is a custom Swing component for representing a single map cell in
 * a 2D map.  The cell has several different kinds of state, but the most basic
 * state is whether the cell is passable or not.
 * * Этот класс является настраиваемым компонентом Swing для представления одной ячейки карты в
  * 2D-карта. Ячейка имеет несколько разных состояний, но самое основное
  * состояние - проходима клетка или нет.
 */
public class JMapCell extends JComponent
{
    private static final Dimension CELL_SIZE = new Dimension(12, 12);
    
    /** True indicates that the cell is an endpoint, either start or finish.True указывает, 
     * что ячейка является конечной точкой, либо начальной, либо конечной.  **/
    
    boolean endpoint = false;
    
    
    /** True indicates that the cell is passable; false means it is not.
     *  Истина указывает, что ячейка проходима; ложь означает, что это не так**/
    boolean passable = true;
    
    /**
     * True indicates that this cell is part of the path between start and end.
     * Истина указывает, что эта ячейка является частью пути между началом и концом.
     **/
    boolean path = false;
    
    /**
     * Construct a new map cell with the specified "passability."  An input of
     * true means the cell is passable.
     * * Построить новую ячейку карты с указанной «проходимостью». Ввод
      * true означает, что ячейка проходима.
     **/
    public JMapCell(boolean pass)
    {
        // Set the preferred cell size, to drive the initial window size.
        // Установите предпочтительный размер ячейки, чтобы управлять начальным размером окна.
        setPreferredSize(CELL_SIZE);
        
        setPassable(pass);
    }
    
    /** Construct a new map cell, which is passable by default.
     *  Создайте новую ячейку карты, которая по умолчанию является проходимой. **/
    public JMapCell()
    {
        // Call the other constructor, specifying true for "passable".
        // Вызовите другой конструктор, указав истину для "удовлетворительно".
        this(true);
    }
    
    /** Marks this cell as either being the starting or the ending cell.
     * Отмечает эту ячейку как начальную или конечную. **/
    public void setEndpoint(boolean end)
    {
        endpoint = end;
        updateAppearance();
    }
    
    /**
     * Sets this cell to be passable or not passable.  An input of true marks
     * the cell as passable; an input of false marks it as not passable.
     * * Делает эту ячейку проходимой или непроходимой. Ввод истинных оценок
      * клетка как проходимая; ввод false помечает его как непроходимый.
     **/
    public void setPassable(boolean pass)
    {
        passable = pass;
        updateAppearance();
    }
    
    /** Returns true if this cell is passable, or false otherwise. 
     * Возвращает true, если эта ячейка проходима, или false в противном случае.**/
    public boolean isPassable()
    {
        return passable;
    }
    
    /** Toggles the current "passable" state of the map cell.
     * Переключает текущее "проходимое" состояние ячейки карты **/
    public void togglePassable()
    {
        setPassable(!isPassable());
    }
    
    /** Marks this cell as part of the path discovered by the A* algorithm.
     * Помечает эту ячейку как часть пути, обнаруженного алгоритмом A *. **/
    public void setPath(boolean path)
    {
        this.path = path;
        updateAppearance();
    }
    
    /**
     * This helper method updates the background color to match the current
     * internal state of the cell.
     * * Этот вспомогательный метод обновляет цвет фона в соответствии с текущим
      * внутреннее состояние клетки.
     **/
    private void updateAppearance()
    {
        if (passable)
        {
            // Passable cell.  Indicate its state with a border.
            //Проходимая ячейка. Обозначьте его состояние рамкой.
            setBackground(Color.WHITE);

            if (endpoint)
                setBackground(Color.CYAN);
            else if (path)
                setBackground(Color.GREEN);
        }
        else
        {
            // Impassable cell.  Make it all red.
            // Непроходимая клетка. Сделайте все красным.
            setBackground(Color.RED);
        }
    }

    /**
     * Implementation of the paint method to draw the background color into the
     * map cell.
     * * Реализация метода рисования для рисования цвета фона в
      * ячейка карты
     **/
    protected void paintComponent(Graphics g)
    {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
