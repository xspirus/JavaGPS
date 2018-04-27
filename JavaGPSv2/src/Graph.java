import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * A Graph class.
 * Contains the vertices of the graph, as well
 * as the taxis and the client.
 * Classes used:
 * <ul>
 *     <li>HashMap</li>
 *     <li>ArrayList</li>
 *     <li>T (generic object)</li>
 * </ul>
 * @param <T> The type of the object the graph contains.
 */
public class Graph<T extends CostObject<T>> {

    private Map<Vertex<T>, Vertex<T>>   allVertices;
    private List<Vertex<T>>             taxis;
    private T                           client;

    /**
     * The graph constructor.
     * Finds all the individual vertices (no duplicates).
     * Finds all the taxis.
     * Finds the client.
     * @param nodefile The file of nodes.
     * @param taxifile The file of taxis.
     * @param clientfile The file of client(s).
     * @param generic A generic instance of the class used.
     */
    public Graph(String nodefile, String taxifile, String clientfile, T generic) {
        allVertices = new HashMap<>();
        taxis = new ArrayList<>();
        BufferedReader br = null;
        int prevId = -1, currId;
        Vertex<T> previous = null;
        Vertex<T> current = null;

        try {
            br = new BufferedReader(new FileReader(nodefile));
            String line;
            br.readLine();
            line = br.readLine();
            String node[] = line.split(",");
            double x = Double.parseDouble(node[0]);
            double y = Double.parseDouble(node[1]);
            currId = Integer.parseInt(node[2]);
            current = new Vertex<T>(generic.create(x, y));
            allVertices.put(current, current);
            while ((line = br.readLine()) != null) {
                node = line.split(",");
                x = Double.parseDouble(node[0]);
                y = Double.parseDouble(node[1]);
                currId = Integer.parseInt(node[2]);
                //System.out.println(currId + " " + prevId);
                current = new Vertex<>(generic.create(x, y));

                if (allVertices.containsKey(current)) {
                    //System.out.println("Crossroad " + current.location);
                    current = allVertices.get(current);
                } else {
                    allVertices.put(current, current);
                }

                if (prevId == currId) {
                    try {
                        //System.out.println(current.getLocation());
                        //System.out.println(previous.getLocation());
                        //System.out.println();
                        current.addEdge(new Edge<>(current, previous));
                        previous.addEdge((new Edge<>(previous, current)));
                    } catch (SameVerticesException e) {

                    }
                    //System.out.println("Current neighbors: " + current.edges.size());
                    //System.out.println("Previous neighbors: " + previous.edges.size());
                }
                prevId = currId;
                previous = current;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nodes file not found!");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IOException");
            System.exit(1);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("Could not close nodes file");
            }
        }

        try {
            br = new BufferedReader(new FileReader(taxifile));
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String taxi[] = line.split(",");
                double x = Double.parseDouble(taxi[0]);
                double y = Double.parseDouble(taxi[1]);
                int id = Integer.parseInt(taxi[2]);
                taxis.add(new Vertex<>(findClosest(generic.create(x, y)), id));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Taxis file not found!");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IOException");
            System.exit(1);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("Could not close taxis file");
            }
        }

        try {
            br = new BufferedReader(new FileReader(clientfile));
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String clientinfo[] = line.split(",");
                double x = Double.parseDouble(clientinfo[0]);
                double y = Double.parseDouble(clientinfo[1]);
                client = findClosest(generic.create(x, y));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Taxis file not found!");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IOException");
            System.exit(1);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("Could not close taxis file");
            }
        }

    }

    /**
     * To find the location of the taxis and
     * the client as they are not given as
     * a node of the map given.
     * @param loc The taxi's/client's location.
     * @return The closest location based on the nodes.
     */
    private T findClosest(T loc) {
        double min = Double.MAX_VALUE;
        T result = null;
        T curLoc = null;
        double dist;

        for (Vertex<T> current : allVertices.keySet()) {
            curLoc = current.getLocation();
            dist = curLoc.distance(loc);
            if (dist < min) {
                min = dist;
                result = curLoc;
            }
        }

        return result.create(result);
    }

    /**
     * Get all vertices.
     * @return HashMap<K: Vertex, V: Vertex> of the nodes.
     */
    public Map<Vertex<T>, Vertex<T>> getAllVertices() {
        return allVertices;
    }

    /**
     * Get all taxis.
     * @return ArrayList<Vertex> of taxis.
     */
    public List<Vertex<T>> getTaxis() {
        return taxis;
    }

    /**
     * Get location of client.
     * @return The client's location.
     */
    public T getClient() {
        return client;
    }

    /**
     * A vertex class.
     * Contains the location of the vertex,
     * its id and all of its neighbors.
     * @param <T> The type of the object the vertex contains.
     */
    public static class Vertex<T extends CostObject<T>> {

        private T               location;
        private final int       id;
        private List<Edge<T>>   edges;

        /**
         * Constructor used for nodes.
         * We don't need ID after reading
         * so put it to zero.
         * @param location The coordinates of the node.
         */
        public Vertex(T location) {
            this.location = location;
            id = 0;
            edges = new ArrayList<>();
        }

        /**
         * Constructor used for the taxis.
         * We need the IDs.
         * No edges.
         * @param location The coordinates of the taxi.
         * @param id The ID of the taxi.
         */
        public Vertex(T location, int id) {
            this.location = location;
            this.id = id;
            edges = null;
        }

        /**
         * Copy constructor.
         * @param other The vertex to be hard copied.
         */
        public Vertex(Vertex<T> other) {
            this(other.location);
            this.edges.addAll(other.edges);
        }

        /**
         *
         * @return The coordinates.
         */
        public T getLocation() {
            return location;
        }

        /**
         *
         * @return The id.
         */
        public int getId() {
            return id;
        }

        /**
         *
         * @return ArrayList of the edges.
         */
        public List<Edge<T>> getEdges() {
            return edges;
        }

        /**
         * Add an edge to the existing edges of node.
         * @param edge The edge to be added.
         */
        public void addEdge(Edge<T> edge) {
            edges.add(edge);
        }

        /**
         * Calculate the distance between two vertices.
         * @param other The other vertex.
         * @return The distance.
         */
        public Double distance(Vertex<T> other) {
            return this.location.distance(other.getLocation());
        }

        /**
         * Calculate the distance between vertex
         * and location.
         * @param other The location.
         * @return The distance.
         */
        public Double distance(T other) {
            return this.location.distance(other);
        }

        /**
         * A hashCode function.
         * Needed for HashMaps and HashSets.
         * @return int HashCode.
         */
        @Override
        public int hashCode() {
            return (this.location.hashCode() * 31);
        }

        /**
         * The equals method.
         * Needed for HashMaps and HashSets.
         * @param o The object to compare.
         * @return true iff same T.
         */
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Vertex))
                return false;

            Vertex<T> other = (Vertex<T>) o;
            return this.location.equals(other.getLocation());

        }

    }

    /**
     * A edge class.
     * Contains the beginning and the ending
     * of an edge between two vertices.
     * @param <T> The type of the object the edges contain.
     */
    public static class Edge<T extends CostObject<T>> {

        private Vertex<T>   from;
        private Vertex<T>   to;

        /**
         * Constructor.
         * @param from The beginning of the edge.
         * @param to The ending of the edge.
         * @throws SameVerticesException
         */
        public Edge(Vertex<T> from, Vertex<T> to) throws SameVerticesException {
            if (from == null || to == null)
                throw (new NullPointerException("Both 'from' and 'to' vertices must be non-null."));
            if (from.equals(to))
                throw (new SameVerticesException("'from' and 'to' vertices must be of different location."));
            this.from = from;
            this.to = to;
        }

        /**
         *
         * @return The from vertex.
         */
        public Vertex<T> getFrom() {
            return from;
        }

        /**
         *
         * @return The to vertex.
         */
        public Vertex<T> getTo() {
            return to;
        }

        /**
         *
         * @return The edges cost.
         */
        public Double cost() {
            return from.distance(to);
        }

    }

    /**
     * Exception to be used when we try to initialise
     * two same vertices in the same edge -> no edge.
     */
    public static class SameVerticesException extends Exception {

        @SuppressWarnings("unused")
        public SameVerticesException() {
            super();
        }

        public SameVerticesException(String message) {
            super(message);
        }

    }

}
