import java.util.*;


/**
 * This class stores the basic state necessary for the A* algorithm to compute 
 * a path across a map.  This state includes a collection of "open waypoints" 
 * and another collection of "closed waypoints."  In addition, this class 
 * provides the basic operations that the A* pathfinding algorithm needs to 
 * perform its processing.
 * **
  * Этот класс хранит базовое состояние, необходимое для алгоритма A * для вычисления
  * путь по карте. Это состояние включает в себя набор «открытых путевых точек».
  * и еще один сборник «закрытых путевых точек». Кроме того, этот класс
  * предоставляет основные операции, необходимые алгоритму поиска пути A *
  * выполнить его обработку
 **/
public class AStarState
{
    /** 
     * This is a reference to the map that the A* algorithm
     * is navigating.
     * * Это ссылка на карту, которую алгоритм A *
      * перемещается.
     **/
    private Map2D map;

    /** Initialize a map of all open waypoints and their locations.
     * / ** Инициализируем карту всех открытых путевых точек и их местоположений. **/
    private Map<Location, Waypoint> open_waypoints
    = new HashMap<Location, Waypoint> ();
    
    /** Initialize a map of all closed waypoints and their locations. 
     * Инициализировать карту всех закрытых путевых точек и их местоположений.**/
    private Map<Location, Waypoint> closed_waypoints
    = new HashMap<Location, Waypoint> ();

    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     * Инициализируйте новый объект состояния для использования алгоритма поиска пути A *.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;
    }

    /** Returns the map that the A* pathfinder is navigating.
     * Возвращает карту, по которой перемещается поисковик A *. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     * This method scans through all open waypoints, and returns the waypoint
     * with the minimum total cost.  If there are no open waypoints, this 
     * method returns <code>null</code>.
     * * Этот метод просматривает все открытые путевые точки и возвращает путевую точку.
      * с минимальной общей стоимостью. Если нет открытых путевых точек, это
      * метод возвращает <code> null </code>.
     **/
    public Waypoint getMinOpenWaypoint()
    {
        // If there are no open waypoints, returns <code>null</code>.
        //Если открытых путевых точек нет, возвращает <code> null </code>.
        if (numOpenWaypoints() == 0)
            return null;
        
        // Initialize a keySet of all open waypoints, an interator to
        // iterate through the set, and a variable to hold the best waypoint
        // and the cost for that waypoint. 
        // Инициализируем набор ключей всех открытых путевых точек, итератор для // 
        //итерации по набору и переменную для хранения лучшей путевой точки // и стоимости этой путевой точки.
        Set open_waypoint_keys = open_waypoints.keySet();
        Iterator i = open_waypoint_keys.iterator();
        Waypoint best = null;
        float best_cost = Float.MAX_VALUE;
        
        // Scans through all open waypoints.
        // Сканирует все открытые путевые точки.
        while (i.hasNext())
        {
            // Stores the current location.
            // Сохраняет текущее местоположение.
            Location location = (Location)i.next();
            // Stores the current waypoint.
            // Сохраняет текущую путевую точку.
            Waypoint waypoint = open_waypoints.get(location);
            // Stores the total cost for the current waypoint.
            // Сохраняет общую стоимость для текущей путевой точки.
            float waypoint_total_cost = waypoint.getTotalCost();
            
            // If the total cost for the current waypoint is better (lower)
            // than the stored cost for the stored best waypoint, replace
            // the stored waypoint with the current waypoint and the stored
            // total cost with the current total cost.
            // Если общая стоимость текущей путевой точки лучше (ниже)
             // чем сохраненная стоимость для сохраненной лучшей путевой точки, заменим
             // сохраненная путевая точка с текущей путевой точкой и сохраненная
             // общая стоимость с текущей общей стоимостью.
            if (waypoint_total_cost < best_cost)
            {
                best = open_waypoints.get(location);
                best_cost = waypoint_total_cost;
            }
            
        }
        // Returns the waypoint with the minimum total cost.
        // Возвращает путевую точку с минимальной общей стоимостью.
        return best;
    }

    /**
     * This method adds a waypoint to (or potentially updates a waypoint 
     * already in) the "open waypoints" collection.  If there is not already 
     * an open waypoint at the new waypoint's location then the new waypoint 
     * is simply added to the collection.  However, if there is already a 
     * waypoint at the new waypoint's location, the new waypoint replaces 
     * the old one <em>only if</em> the new waypoint's "previous cost" value 
     * is less than the current waypoint's "previous cost" value.
     * * Этот метод добавляет путевую точку (или потенциально обновляет путевую точку
      * уже в) коллекции "открытых путевых точек". Если еще нет
      * открытая путевая точка в местоположении новой путевой точки, затем новая путевая точка
      * просто добавляется в коллекцию. Однако, если уже есть
      * путевая точка в местоположении новой путевой точки, новая путевая точка заменяет
      * старый <em> только если </em> значение "предыдущей стоимости" новой путевой точки
      * меньше, чем значение "предыдущей стоимости" текущей путевой точки.
      ** /
     **/
    public boolean addOpenWaypoint(Waypoint newWP)
    {
        // Finds the location of the new waypoint.
        // Находит местоположение новой путевой точки.
        Location location = newWP.getLocation();
        
        // Checks to see if there is already an open waypoint at the new
        // waypoint's location.
        // Проверяет, есть ли уже открытая путевая точка в новом
         // местоположение путевой точки.
        if (open_waypoints.containsKey(location))
        {
            // If there is already an open waypoint at the new waypoint's
            // location, checks to see if the new waypoint's "previous
            // cost" value is less than the current waypoint's "previous
            // cost" value.
            // Если в новой путевой точке уже есть открытая путевая точка
             // местоположение, проверяет, является ли новая путевая точка "предыдущей"
             // значение стоимости меньше, чем предыдущее значение текущей путевой точки
             // стоимость »значение.
            Waypoint current_waypoint = open_waypoints.get(location);
            if (newWP.getPreviousCost() < current_waypoint.getPreviousCost())
            {
                // If the new waypoint's "previous cost" value is less than the
                // current waypoint's "previous cost" value, the new waypoint
                // replaces the old waypoint and returns true.
                // Если значение "предыдущей стоимости" новой путевой точки меньше, чем
                 // значение "предыдущей стоимости" текущей путевой точки, новая путевая точка
                 // заменяет старую путевую точку и возвращает true.
                open_waypoints.put(location, newWP);
                return true;
            }
            // If the new waypoint's "previous cost" value is not less than
            // the current waypoint's "previous cost" value, return false.
            // Если значение "предыдущей стоимости" новой путевой точки не меньше, чем
             // значение "предыдущей стоимости" текущей путевой точки, возврат false.
            return false;
        }
        // If there is not already an open waypoint at the new waypoint's
        // location, add the new waypoint to the collection of open waypoints
        // and return true. 
        // Если в новой путевой точке еще нет открытой путевой точки
         // местоположение, добавляем новую путевую точку в коллекцию открытых путевых точек
         // и вернем истину
        open_waypoints.put(location, newWP);
        return true;
    }


    /** Returns the current number of open waypoints. / ** Возвращает текущее количество открытых путевых точек. ** / **/
    
    public int numOpenWaypoints()
    {
        return open_waypoints.size();
    }

    /**
     * This method moves the waypoint at the specified location from the
     * open list to the closed list.
     * * Этот метод перемещает путевую точку в указанном месте из
      * открытый список к закрытому списку.
     **/
    public void closeWaypoint(Location loc)
    {
        Waypoint waypoint = open_waypoints.remove(loc);
        closed_waypoints.put(loc, waypoint);
    }

    /**
     * Returns true if the collection of closed waypoints contains a waypoint
     * for the specified location.
     * * Возвращает истину, если коллекция закрытых путевых точек содержит путевую точку
      * для указанного места.
     **/
    public boolean isLocationClosed(Location loc)
    {
        return closed_waypoints.containsKey(loc);
    }
}