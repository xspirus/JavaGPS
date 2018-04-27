import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 *  The A* Algorithm Class.
 *  Implements a beam search A* algorithm,
 *  which means that the OpenSet can contain
 *  a fixed amount of States.
 *  </br>
 *  Classes used:
 *  <ul>
 *      <li><code>TreeMap</code> which keeps results in ascending order of cost.</li>
 *      <li><code>PrioQueue</code> which serves as the OpenSet.</li>
 *      <li><code>HashSet</code> which keeps the all the visited States (pulled from OpenSet).</li>
 *      <li><code>HashMap</code> which keeps for each State its best gScore.</li>
 *  </ul>
 *  @see PrioQueue
 *  @see CostObject
 *  The objects that the A* can work on are based
 *  on the <code>CostObject</code> interface. These
 *  are objects which have some kind of method to
 *  compute the cost from one object to another.
 */
public class AStar<T extends CostObject<T>> {

    private Set<Graph.Vertex<T>>                    closedSet;
    private PrioQueue<Graph.Vertex<T>>              openSet;
    private Map<Graph.Vertex<T>, Graph.Vertex<T>>   cameFrom;
    private Map<Graph.Vertex<T>, Double>            gScore;
    private Map<Graph.Vertex<T>, Double>            fScore;
    Map<Graph.Vertex<T>, Double>                    finalScore;

    /**
     * The A* Algorithm implementation method.
     * The A* Algorithm works as follows:
     * <ul>
     *     <li>Get taxi.</li>
     *     <li>Place it in <code>PrioQueue</code> with 0 gScore and its heuristic cost.</li>
     *     <li>While OpenSet (PrioQueue) not empty do:</li>
     *     <ul>
     *         <li>Pull first <code>State</code> from OpenSet.</li>
     *         <li>If it is the client coordinates put result in <code>TreeMap</code>.</li>
     *         <li>Else find neighbors.</li>
     *         <li>Check if neighbors are visited.</li>
     *         <li>If a neighbor is visited do nothing.</li>
     *         <li>Else check the previous gScore of the neighbor.</li>
     *         <li>If there is no score place currentScore in gScore and put him in the OpenSet.</li>
     *         <li>Else check if currentScore is better than old gScore and act accordingly.</li>
     *     </ul>
     * </ul>
     * @param graph The <code>Graph</code> which contains nodes, taxis, neighbors and client.
     * @param arg The capacity of the A* openSet.
     * @return A <code>TreeMap</code> with <code>Vertex</code> as key and list of <code>Vertex</code> as value.
     */
    public Map<Graph.Vertex<T>, List<Graph.Vertex<T>>> solve(Graph<T> graph, String arg) {

        closedSet = new HashSet<>();
        cameFrom = new HashMap<>();
        gScore = new HashMap<>();
        fScore = new HashMap<>();
        finalScore = new HashMap<>();
        PrintWriter writer = null;
        String file = "astar" + Integer.parseInt(arg) + ".csv";
        int max, steps;

        /**
         * The comparator for the insertion to the <code>PrioQueue</code>.
         * It is based on the fScore of the two vertices and is in ascending order.
         */
        final Comparator<Graph.Vertex<T>> qComparator = new Comparator<Graph.Vertex<T>>() {
            @Override
            public int compare(Graph.Vertex<T> v1, Graph.Vertex<T> v2) {
                if (fScore.get(v1) < fScore.get(v2))
                    return -1;
                return 1;
            }
        };

        /**
         * The comparator for the insertion to the <code>TreeMap</code>.
         * It is based on the final score of each taxi route and is in ascending order.
         */
        final Comparator<Graph.Vertex<T>> tComparator = new Comparator<Graph.Vertex<T>>() {
            @Override
            public int compare(Graph.Vertex<T> t1, Graph.Vertex<T> t2) {
                if (finalScore.get(t1) < finalScore.get(t2))
                    return -1;
                return 1;
            }
        };

        openSet = new PrioQueue<>(Integer.parseInt(arg), qComparator);
        Map<Graph.Vertex<T>, List<Graph.Vertex<T>>> result = new TreeMap<>(tComparator);

        Map<Graph.Vertex<T>, Graph.Vertex<T>> vertices = graph.getAllVertices();
        T goal = graph.getClient();
        //System.out.println("Goal coordinates " + goal);
        try {
            writer = new PrintWriter(file);
            writer.println("Taxi ID, Max openSet Size, A* Steps, Distance");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(1);
        }

        for (Graph.Vertex<T> taxi : graph.getTaxis()) {

            closedSet.clear();
            openSet.clear();
            cameFrom.clear();
            gScore.clear();
            fScore.clear();

            //System.out.println();
            //System.out.println(taxi.getLocation());
            Graph.Vertex<T> current = vertices.get(taxi);
            cameFrom.put(current, null);
            gScore.put(current, 0.0);
            fScore.put(current, current.distance(goal));
            openSet.add(current);
            max = 1;
            steps = 0;

            while (!openSet.isEmpty()) {
                steps++;
                current = openSet.pollFirst();
                //System.out.println("\nCurrent " + current.getLocation());

                if (current.getLocation().equals(goal)) {
                    finalScore.put(taxi, fScore.get(current));
                    result.put(taxi, reconstructPath(current, cameFrom));
                    writer.println(taxi.getId() + ", " + max + ", " + steps + ", " + fScore.get(current));
                    break;
                }

                closedSet.add(current);
                Double scoreSoFar = gScore.get(current);

                //System.out.println("With neighbors:");
                for (Graph.Edge<T> edge : current.getEdges()) {

                    Graph.Vertex<T> neighbor = edge.getTo();

                    if (closedSet.contains(neighbor))
                        continue;

                    Double tGScore = scoreSoFar + edge.cost();
                    //System.out.print(neighbor.getLocation());
                    //System.out.print(" with gScore " + tGScore);
                    //System.out.println(" with fScore " + (tGScore + neighbor.distance(goal)));
                    if (gScore.containsKey(neighbor)) {
                        if (tGScore < gScore.get(neighbor)) {
                            if (openSet.contains(neighbor)) {
                                gScore.put(neighbor, tGScore);
                                fScore.put(neighbor, (tGScore + neighbor.distance(goal)));
                                openSet.remove(neighbor);
                                openSet.add(neighbor);
                                cameFrom.put(neighbor, current);
                                //System.out.println("Added after removing");
                            } else {
                                double prevFScore = fScore.get(neighbor);
                                fScore.put(neighbor, (tGScore + neighbor.distance(goal)));
                                if (openSet.add(neighbor)) {
                                    gScore.put(neighbor, tGScore);
                                    cameFrom.put(neighbor, current);
                                    //System.out.println("Added with better score");
                                } else {
                                    fScore.put(neighbor, prevFScore);
                                }
                            }
                        }
                    } else {
                        fScore.put(neighbor, (tGScore + neighbor.distance(goal)));
                        if (openSet.add(neighbor)) {
                            gScore.put(neighbor, tGScore);
                            cameFrom.put(neighbor, current);
                            //System.out.println("Added normally");
                        } else {
                            fScore.remove(neighbor);
                        }
                    }

                }

                if (openSet.size() > max)
                    max = openSet.size();

            }

        }

        writer.close();
        return result;

    }

    /**
     * Reconstructs the path of the route.
     * Finds all the previous vertices through the
     * cameFrom map, and returns all the parents in
     * an <code>ArrayList</code> of vertices.
     * @param goal
     * @param cameFrom
     * @return
     */
    private List<Graph.Vertex<T>> reconstructPath(Graph.Vertex<T> goal, Map<Graph.Vertex<T>, Graph.Vertex<T>> cameFrom) {
        List<Graph.Vertex<T>> result = new ArrayList<>();
        result.add(goal);
        while ((goal = cameFrom.get(goal)) != null) {
            result.add(goal);
        }
        return result;
    }

}
