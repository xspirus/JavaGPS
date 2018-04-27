import java.io.*;
import java.util.*;

/**
 * A Graph class. Contains:
 * <ul>
 *     <li>The nodes that can be parsed.</li>
 *     <li>The neighbors of each node.</li>
 *     <li>The taxis.</li>
 *     <li>The client coordinates.</li>
 * </ul>
 * Classes used:
 * <ul>
 *     <li><code>ArrayList<Node></code> which contains the nodes.</li>
 *     <li><code>HashMap</code> which contains the neighbors for each node.</li>
 *     <li><code>ArrayList<Taxi></code> which contains the taxis</li>
 *     <li><code>Coordinates</code> for the client coordinates.</li>
 * </ul>
 * @see Coordinates
 */
public class Graph {

    private ArrayList<Node> nodes;
    private HashMap<Coordinates, ArrayList<Coordinates>> neighbors;
    private ArrayList<Taxi> taxis;
    private Coordinates end;

    /**
     * The constructor of the class.</br>
     * Read the nodes, taxis and client files and make
     * the according sets of objects.</br>
     * For the neighbors we use a <code>HashMap</code>
     * in which the keys are <code>Coordinates</code>
     * which helps us find same coordinates throughout
     * the nodes and let them have different neighbors
     * (even if the IDs are different). Moreover neighbors
     * are the coordinates with the same ID and are next
     * and previous in the nodes file.
     * @param nodefile The nodes.csv
     * @param taxifile The taxis.csv
     * @param clientfile The client.csv
     */
    public Graph(String nodefile, String taxifile, String clientfile) {

        nodes = new ArrayList<>();
        neighbors = new HashMap<>();
        taxis = new ArrayList<>();
        BufferedReader br = null;
        Coordinates co;

        // Read the file of nodes
        long startTime = System.currentTimeMillis();
        try {
            br = new BufferedReader(new FileReader(nodefile));
            String line;
            // Eliminate x, y, id, name.
            br.readLine();
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] node = line.split(",");
                Node current = new Node(node);
                nodes.add(current);
                co = current.getCoordinates();
                if (i == 0) {
                    i++;
                    continue;
                }
                Node prev = nodes.get(i - 1);
                Coordinates prevCo = prev.getCoordinates();
                /**
                 * Find if neighbor with previous.
                 * If so add in both coordinates the
                 * other as neighbor.
                 */
                if (prev.getId() == current.getId()) {
                    if (neighbors.containsKey(prevCo)) {
                        //System.out.println("Crossroad " + prevCo);
                        neighbors.get(prevCo).add(co);
                    } else {
                        neighbors.put(prevCo, new ArrayList<>());
                        neighbors.get(prevCo).add(co);
                    }
                    if (neighbors.containsKey(co)) {
                        //System.out.println("Crossroad " + co);
                        neighbors.get(co).add(prevCo);
                    } else {
                        neighbors.put(co, new ArrayList<>());
                        neighbors.get(co).add(prevCo);
                    }
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Something went wrong");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("Could not close file!");
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Done reading in " + (endTime - startTime) / 1000.0 + " s");

        br = null;

        // Read the file of taxis
        try {
            br = new BufferedReader(new FileReader(taxifile));
            String line;
            double x, y;
            // Eliminate x, y, id.
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] taxi = line.split(",");
                x = Double.parseDouble(taxi[0]);
                y = Double.parseDouble(taxi[1]);
                co = findClosest(new Coordinates(x, y));
                taxis.add(new Taxi(co, Integer.parseInt(taxi[2])));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Something went wrong");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("Could not close file!");
            }
        }

        br = null;

        // Read the file of client(s)
        try {
            br = new BufferedReader(new FileReader(clientfile));
            String line;
            double x, y;
            // Eliminate x, y.
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] taxi = line.split(",");
                x = Double.parseDouble(taxi[0]);
                y = Double.parseDouble(taxi[1]);
                end = findClosest(new Coordinates(x, y));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Something went wrong");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("Could not close file!");
            }
        }

    }

    /**
     * Get the taxis array.
     * @return The <code>ArrayList</code> containing <code>Taxis</code>.
     */
    public ArrayList<Taxi> getTaxis() {
        return taxis;
    }

    /**
     * Some taxi coordinates may not be in the nodes.csv.
     * With this function we match them with the closest.
     * @param co The real coordinates of the taxi.
     * @return The fixed coordinates of the taxi.
     */
    public Coordinates findClosest(Coordinates co) {

        double min = Double.MAX_VALUE;
        Coordinates result = null;
        double dist;

        for (Node current : nodes) {
            dist = current.getCoordinates().distance(co);
            if (dist < min) {
                min = dist;
                result = current.getCoordinates();
            }
        }

        return result;

    }

    /**
     * Client coordinates.
     * @return The client coordinates.
     */
    public Coordinates getEnd() {
        return end;
    }

    /**
     * Implements the heuristic function used for
     * the A* fScore. Finds the euclidean distance
     * between the client and the current node.
     * @param node The <code>Coordinates</code> to find the cost from.
     * @return The distance between client and node.
     */
    public double heuristic(Coordinates node) {
        return end.distance(node);
    }

    /**
     * Gets the neighbors from the <code>HashMap</code> for this node.
     * @param node The <code>Coordinates</code> for which the neighbors are to be returned.
     * @return The node's neighbors.
     */
    public Collection<Coordinates> getNeighbors(Coordinates node) {
        return neighbors.get(node);
    }

}
