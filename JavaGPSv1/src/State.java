import java.util.ArrayList;
import java.util.Collection;

/**
 * A State class. Contains:
 * <ul>
 *     <li><code>Coordinates</code>.</li>
 *     <li>gScore.</li>
 *     <li>fScore</li>
 *     <li>Previous <code>State</code>.</li>
 * </ul>
 */
public class State implements Comparable<State> {

    private Coordinates node;
    private double cost;
    private double hcost;
    private State previous;

    /**
     * The class constructor. Sets all values.
     * @param node The <code>Coordinates</code>.
     * @param cost The gScore.
     * @param hcost The fScore.
     * @param previous The previous <code>State</code>.
     */
    public State(Coordinates node, double cost, double hcost, State previous) {
        this.node = node;
        this.cost = cost;
        this.hcost = hcost;
        this.previous = previous;
    }

    /**
     * Gets the coordinates of this <code>State</code>.
     * @return The state's coordinates.
     */
    public Coordinates getCoordinates() {
        return node;
    }

    /**
     * Gets the gScore of this <code>State</code>.
     * @return The current gScore.
     */
    public double getCost() {
        return cost;
    }

    public double getHCost() {
        return hcost;
    }

    /**
     * Gets the previous of this <code>State</code>.
     * @return The state from which this state was reached.
     */
    public State getPrevious() {
        return previous;
    }

    /**
     * Finds for each neighbor of this <code>State</code>
     * the gScore and the fScore and returns those
     * neighbors as <code>States</code>.
     * @param graph The graph from which we get the neighbors' coordinates.
     * @return The <code>States</code> which are this state's neighbors.
     */
    public Collection<State> neighbors(Graph graph) {

        Collection<State> states = new ArrayList<>();
        double distance;

        Collection<Coordinates> neighborsList = graph.getNeighbors(node);
        for (Coordinates current : neighborsList) {
            distance = node.distance(current);
            distance += cost;
            states.add(new State(current, distance, (distance + graph.heuristic(current)), this));
        }

        return states;

    }

    /**
     * The compare method.</br>
     * It depends on the fScore.
     * @param other The <code>State</code> to compare to.
     * @return -1 or 1 for less or greater/equal fScores accordingly.
     */
    @Override
    public int compareTo(State other) {
        if (this.hcost < other.hcost)
            return -1;
        return 1;
    }

    /**
     * The equals method.</br>
     * Two states are equal if they have the same coordinates.
     * @param o The other object
     * @return true iff same object or same coordinates.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof State))
            return false;

        State other = (State) o;
        return (this.node.equals(other.node));
    }

    /**
     * A hash function which uses the <code>Coordinates</code>.
     * @return A pseudorandom int.
     */
    @Override
    public int hashCode() {
        return node.hashCode();
    }

}
