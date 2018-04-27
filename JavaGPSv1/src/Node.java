/**
 * The nodes class.</br>
 * Contains:
 * <ul>
 *     <li>Coordinates of the node.</li>
 *     <li>ID of the node.</li>
 *     <li>Name of node.</li>
 * </ul>
 */
public class Node {

    private Coordinates co;
    private int id;
    private String name;

    /**
     * The Node constructor.
     * Line contains x, y, id, name.
     * @param line The string which contains the info.
     */
    public Node(String[] line) {
        double x, y;
        x = Double.parseDouble(line[0]);
        y = Double.parseDouble(line[1]);
        co = new Coordinates(x, y);
        id = Integer.parseInt(line[2]);
        if (line.length == 3)
            name = null;
        else
            name = line[3];
    }

    /**
     * Coordinates getter.
     * @return The node's coordinates.
     */
    public Coordinates getCoordinates() {
        return co;
    }

    /**
     * ID getter.
     * @return The node's id.
     */
    public int getId() {
        return id;
    }

}
