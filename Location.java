/**
 * * Этот класс представляет собой конкретное место на 2D-карте. Координаты
  * целочисленные значения.
 **/
public class Location
{
    /**Координата X этого места. **/
    public int xCoord;

    /** Координата Y этого места **/
    public int yCoord;


    /** Создает новое местоположение с указанными целыми координатами.**/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }

    /**Создает новое местоположение с координатами (0, 0).**/
    public Location()
    {
        this(0, 0);
    }

    /** Сравнивает это местоположение с другим. **/
    public boolean equals(Object obj) {
 
        // Is obj a Location? Является ли объект местоположением?
        if (obj instanceof Location) {
            // Приведение другого объекта к типу Location,
             // затем сравните. Вернуть истину, если равно.
            Location other = (Location) obj;
            if (xCoord == other.xCoord && yCoord == other.yCoord) {
                return true;
            }
        }
        //Если мы попали сюда, они не равны. Вернуть false.
        return false;
    }

    /** Предоставляет хэш-код для каждого местоположения **/
    public int hashCode() {
        int result = 23;
        //Используйте другое простое значение для объединения
        result = 37 * result + xCoord;
        result = 37 * result + yCoord;
        return result;
    }
}

