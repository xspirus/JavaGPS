/**
 * The Taxi class.
 * Contains:
 * <ul>
 *     <li>Taxi's location.</li>
 *     <li>Taxi's ID.</li>
 * </ul>
 */
public class Taxi {

    private Coordinates co;
    private int id;

    /**
     * The taxi constructor. (Yes you can build your own taxi...just kidding :P).
     * @param co The taxi's location.
     * @param id The taxi's id.
     */
    public Taxi(Coordinates co, int id) {
        this.co = co;
        this.id = id;
    }

    /**
     * Coordinates getter.
     * @return The taxi's coordinates.
     */
    public Coordinates getCoordinates() {
        return co;
    }

    /**
     * ID getter.
     * @return The taxi's id.
     */
    public int getID() {
        return id;
    }

}
