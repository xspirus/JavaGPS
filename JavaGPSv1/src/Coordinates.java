/**
 * A class for a point's location on the map.
 */
public class Coordinates {

    private double x;
    private double y;

    /**
     * The class constructor.
     * @param x The longitude.
     * @param y The latitude.
     */
    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Finds the haversian distance of two locations.
     * @param other The other location.
     * @return The distance found.
     */
    public double distance(Coordinates other) {
        double r = 6371.0;
        double dLat = degreesToRadians(this.y - other.y);
        double dLon = degreesToRadians(this.x - other.x);
        double lat1 = degreesToRadians(this.y);
        double lat2 = degreesToRadians(other.y);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (r * c);
    }

    private double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    /**
     * The equals method.</br>
     * Two locations are deemed equal if they have
     * the same longitude and latitude.
     * @param o The other location.
     * @return true iff same location.
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
     * The toString method.</br>
     * Used in writing the kml file.
     * @return The string which contains the coordinates split by a comma.
     */
    @Override
    public String toString() {
        return (x + ", " + y);
    }

    /**
     * A hash function.
     * @return The sum of the hashes of the two coordinates (trivial I know).
     */
    @Override
    public int hashCode() {
        return (Double.valueOf(x).hashCode() / 31 + Double.valueOf(y).hashCode());
    }
}
