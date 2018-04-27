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
 */
public class aStarSolver {

    private int capacity;

    /**
     *  The A* Algorithm Class constructor.
     *  Reads the capacity from the arguments.
     *  @param arg The <code>String</code> containing the capacity number.
     */
    public aStarSolver(String arg) {
        capacity = Integer.parseInt(arg);
        //System.out.println("Capacity: " + capacity);
    }

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
     * @return A <code>TreeMap</code> with States as keys and Taxis as values. It is sorted through the compareTo of <code>State</code>.
     */
    public TreeMap<State, Taxi> solve(Graph graph) {

        TreeMap<State, Taxi> results = new TreeMap<>();

        PrioQueue remaining = new PrioQueue(capacity);
        HashMap<Coordinates, Double> gScore = new HashMap<>();
        HashSet<Coordinates> visited = new HashSet<>();

        //System.out.println("Goal coordinates " + graph.getEnd());

        long startTime = System.currentTimeMillis();
        /**
         *  For each taxi find the best route to the
         *  client. Implement the Beam A* Algorithm.
         *  The algorithm is explained above the
         *  function in a javadoc style comment section.
         */
        for (Taxi taxi : graph.getTaxis()) {

            remaining.clear();
            gScore.clear();
            visited.clear();

            //System.out.println();
            Coordinates taxi_co = taxi.getCoordinates();
            remaining.add(new State(taxi_co, 0, graph.heuristic(taxi_co), null));
            gScore.put(taxi_co, 0.0);

            while (!remaining.isEmpty()) {

                State current = remaining.remove();
                Coordinates cur_co = current.getCoordinates();
                visited.add(cur_co);

                //System.out.println("\nCurrent: " + cur_co);
                //System.out.println("Heuristic: " + current.getHcost());

                /**
                 *  Found the end of the route.
                 *  Put it on the results and go
                 *  to next taxi.
                 */
                if (cur_co.equals(graph.getEnd())) {
                    //System.out.println(taxi.getID());
                    //System.out.println(current.getCost());
                    //System.out.println();
                    results.put(current, taxi);
                    break;
                }

                //System.out.println("With neighbors:");
                for (State neighbor : current.neighbors(graph)) {

                    Coordinates neighbor_co = neighbor.getCoordinates();

                    if (!visited.contains(neighbor_co)) {
                        /**
                         *  If state has been visited but
                         *  is still in OpenSet check its
                         *  gScore. If it is lower do not
                         *  update. Else update gScore and
                         *  OpenSet (OpenSet is PrioQueue).
                         */
                        //System.out.print(neighbor_co);
                        //System.out.print(" with gScore " + neighbor.getCost());
                        //System.out.println(" with fScore " + neighbor.getHCost());
                        if (gScore.containsKey(neighbor_co)) {
                            if (neighbor.getCost() < gScore.get(neighbor_co)) {
                                if (remaining.contains(neighbor)) {
                                    gScore.put(neighbor_co, neighbor.getCost());
                                    remaining.remove(neighbor);
                                    remaining.add(neighbor);
                                    //System.out.println("Added after removing");
                                } else {
                                    if (remaining.add(neighbor))
                                        gScore.put(neighbor_co, neighbor.getCost());
                                    //System.out.println("Added with better score");
                                }
                            }
                        } else {
                            /**
                             *  Check if it is put in queue
                             *  because capacity may have been
                             *  reached. If so do not update
                             *  the gScore value for this State.
                             */
                            if (remaining.add(neighbor)) {
                                gScore.put(neighbor_co, neighbor.getCost());
                                //System.out.println("Added normally");
                            }
                        }

                    }
                }

            }

        }

        long endTime = System.currentTimeMillis();
        System.out.println("Done A* in " + (endTime - startTime) / 1000.0 + " s");

        return results;

    }


}
