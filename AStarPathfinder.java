import java.util.HashMap;
import java.util.HashSet;


/**
 * * Этот класс содержит реализацию алгоритма поиска пути A *. В
  * алгоритм реализован как статический метод, поскольку алгоритм поиска пути
  * действительно не нужно поддерживать какое-либо состояние между вызовами
  * алгоритм.
 */
public class AStarPathfinder
{
    /**
     * * Эта константа устанавливает максимальный предел отсечения для стоимости путей. Если
      * конкретная путевая точка превышает этот предел стоимости, путевая точка
      * исключено.
     **/
    public static final float COST_LIMIT = 1e6f;

    
    /**
     * * Попытки вычислить путь, который перемещается между началом и концом
      * местоположения указанной карты. Если путь может быть найден, путевая точка
      * возвращается <em> последний </em> шаг пути; эта путевая точка может быть
      * раньше ходили назад к исходной точке. Если путь не найден,
      * Возвращается <code> null </code>.
     **/
    public static Waypoint computePath(Map2D map)
    {
         // Переменные, необходимые для поиска A *.
        AStarState state = new AStarState(map);
        Location finishLoc = map.getFinish();

        // Устанавливаем начальную точку для начала поиска A *.
        Waypoint start = new Waypoint(map.getStart(), null);
        start.setCosts(0, estimateTravelCost(start.getLocation(), finishLoc));
        state.addOpenWaypoint(start);

        Waypoint finalWaypoint = null;
        boolean foundPath = false;
        
        while (!foundPath && state.numOpenWaypoints() > 0)
        {
            // Находим пока что "лучшую" (т.е. самую дешевую) путевую точку.
            Waypoint best = state.getMinOpenWaypoint();
            
            //  Если лучшее место - это место финиша, тогда все готово!
            if (best.getLocation().equals(finishLoc))
            {
                finalWaypoint = best;
                foundPath = true;
            }

            // Добавить / обновить всех соседей текущего лучшего местоположения. Это
             // эквивалентно выполнению всех "следующих шагов" из этого места.
            takeNextStep(best, state);

            // Наконец, переместите это место из "открытого" списка в "закрытый"
             // список.
            state.closeWaypoint(best.getLocation());
        }
        
        return finalWaypoint;
    }

    /**
     * This static helper method takes a waypoint, and generates all valid "next
     * steps" from that waypoint.  The new waypoints are added to the "open
     * waypoints" collection of the passed-in A* state object.
     * / **
      * Этот статический вспомогательный метод принимает путевую точку и генерирует все действительные "next"
      * шаги "от этой путевой точки. Новые путевые точки добавляются в" открытую "
      * waypoints »коллекция переданного объекта состояния A *.
      ** /
     **/
    private static void takeNextStep(Waypoint currWP, AStarState state)
    {
        Location loc = currWP.getLocation();
        Map2D map = state.getMap();
        
        for (int y = loc.yCoord - 1; y <= loc.yCoord + 1; y++)
        {
            for (int x = loc.xCoord - 1; x <= loc.xCoord + 1; x++)
            {
                Location nextLoc = new Location(x, y);
                
                // If "next location" is outside the map, skip it. 
                //Если «следующее место» находится за пределами карты, пропустите его.
                if (!map.contains(nextLoc))
                    continue;
                
                // If "next location" is this location, skip it.
                // Если "следующее местоположение" - это это местоположение, пропустите его.
                if (nextLoc == loc)
                    continue;
                
                // If this location happens to already be in the "closed" set
                // then continue on with the next location.
                // Если это место уже находится в "закрытом" наборе
                 // затем переходим к следующему месту.
                if (state.isLocationClosed(nextLoc))
                    continue;

                // Make a waypoint for this "next location."
                // Создаем путевую точку для этого "следующего местоположения"
                
                Waypoint nextWP = new Waypoint(nextLoc, currWP);
                
                // OK, we cheat and use the cost estimate to compute the actual
                // cost from the previous cell.  Then, we add in the cost from
                // the map cell we step onto, to incorporate barriers etc.
                // Хорошо, мы обманываем и используем смету для вычисления фактического
                 // стоимость из предыдущей ячейки. Затем мы добавляем стоимость из
                 // ячейка карты, на которую мы ступаем, чтобы установить барьеры и т. д.

                float prevCost = currWP.getPreviousCost() +
                    estimateTravelCost(currWP.getLocation(),
                                       nextWP.getLocation());

                prevCost += map.getCellValue(nextLoc);
                
                // Skip this "next location" if it is too costly.
                // Пропускаем это «следующее место», если оно слишком дорогое.
                if (prevCost >= COST_LIMIT)
                    continue;
                
                nextWP.setCosts(prevCost,
                    estimateTravelCost(nextLoc, map.getFinish()));

                // Add the waypoint to the set of open waypoints.  If there
                // happens to already be a waypoint for this location, the new
                // waypoint only replaces the old waypoint if it is less costly
                // than the old one.
                // Добавляем путевую точку в набор открытых путевых точек. Если там
                 // оказывается уже путевой точкой для этого места, новый
                 // путевая точка заменяет старую путевую точку, только если она менее дорогая
                 // чем старый.
                state.addOpenWaypoint(nextWP);
            }
        }
    }
    
    /**
     * Estimates the cost of traveling between the two specified locations.
     * The actual cost computed is just the straight-line distance between the
     * two locations.
     * / **
      * Оценивает стоимость поездки между двумя указанными местами.
      * Фактическая стоимость рассчитывается как расстояние по прямой между
      * два места.
      ** /
     **/
    private static float estimateTravelCost(Location currLoc, Location destLoc)
    {
        int dx = destLoc.xCoord - currLoc.xCoord;
        int dy = destLoc.yCoord - currLoc.yCoord;
        
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
