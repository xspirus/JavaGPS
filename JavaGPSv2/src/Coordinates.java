/**
 * A class for a point's location on the map.
 */
public class Coordinates implements CostObject<Coordinates> {

    private double x;
    private double y;

    /**
     * Generic class constructor.
     */
    public Coordinates() {
        x = Double.MAX_VALUE;
        y = Double.MIN_VALUE;
    }

    /**
     * Typical constructor.
     * @param x The longitude.
     * @param y The latitude.
     */
    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * The create method. Creates a new
     * coordinates object.
     * @param x The longitude.
     * @param y The latitude.
     * @return Coordinates with the above location.
     */
    @Override
    public Coordinates create(double x, double y) {
        return (new Coordinates(x, y));
    }

    /**
     * The create method. Creates a new
     * coordinates object.
     * @param other The object to be copied.
     * @return Coordinates with the same location as the given object.
     */
    @Override
    public Coordinates create(Coordinates other) {
        return (new Coordinates(other.x, other.y));
    }

    /**
     * Calculates the distance between two Earth coordinates.
     * @param other The other coordinates.
     * @return The distance in KiloMetres.
     */
    @Override
    public Double distance(Coordinates other) {
        Double r = 6371.0;
        Double dLat = degreesToRadians(this.y - other.y);
        Double dLon = degreesToRadians(this.x - other.x);
        Double lat1 = degreesToRadians(this.y);
        Double lat2 = degreesToRadians(other.y);
        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (r * c);
    }

    private Double degreesToRadians(double degrees) {
        return (degrees * Math.PI / 180);
    }

    /**
     * The equals method. Two coordinates are equal
     * if they have the same x and y.
     * @param o The other coordinates.
     * @return true iff same x && y.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coordinates))
            return false;

        Coordinates other = (Coordinates)o;
        if (this == other)
            return true;

        return (this.x == other.x && this.y == other.y);
    }

    /**
     * General hash code.
     * @return int PseudoRandom.
     */
    @Override
    public int hashCode() {
        return (Double.valueOf(x).hashCode() * 31 + Double.valueOf(y).hashCode());
    }

    /**
     * The toString method. Used by kml
     * to println int the kml file.
     * @return String as "x, y".
     */
    @Override
    public String toString() {
        return (x + ", " + y);
    }

}
