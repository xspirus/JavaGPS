import java.util.*;

/**
 * A PriorityQueue class with fixed size.</br>
 * Auxiliary class used <code>TreeSet</code>.
 */
public class PrioQueue {

    private final int size;
    private int current_size;
    private TreeSet<State> queue;

    /**
     * The class constructor.</br>
     * Sets fixed size of queue.
     * @param size The fixed size of the OpenSet.
     */
    public PrioQueue(int size) {
        this.size = size;
        current_size = 0;
        queue = new TreeSet<>();
    }

    /**
     * Adds a State into the queue if possible.</br>
     * Checks whether the state is fit to enter the queue,
     * if the queue is full, and rejects if so.</br>
     * Uses <code>TreeSet</code> add.
     * @param state The state to be inserted
     * @return true iff state was added to the queue
     */
    public boolean add(State state) {
        if (size <= 0)
            return false;

        boolean added = queue.add(state);

        if (!added)
            return false;

        current_size++;

        //System.out.println(current_size);

        if (current_size > size) {
            if (queue.pollLast().equals(state))
                return false;
        }

        return true;
    }

    /**
     * Clears the queue.
     */
    public void clear() {
        current_size = 0;
        queue.clear();
    }

    /**
     * Checks if queue is empty.
     * @return true iff number of elements is 0.
     */
    public boolean isEmpty() {
        return (current_size == 0);
    }

    /**
     * Removes the first state from the queue.
     * @return The <code>State</code> removed.
     */
    public State remove() {
        current_size--;
        return queue.pollFirst();
    }

    /**
     * Removes a specific <code>State</code> from the queue.
     * <code>HashSet</code> must be used because <code>TreeSet</code>
     * uses <code>compareTo</code> not <code>equals</code>.
     * @param state The <code>State</code> to be removed.
     * @return true iff <code>State</code> was removed successfully.
     */
    public boolean remove(State state) {
        HashSet<State> OpenSet = new HashSet<>(queue);
        if (OpenSet.remove(state)) {
            current_size--;
            queue = new TreeSet<>(OpenSet);
            return true;
        }
        return false;
    }

    /**
     * Checks if a specific <code>State</code> is in the queue.
     * <code>HashSet</code> must be used because <code>TreeSet</code>
     * uses <code>compareTo</code> not <code>equals</code>.
     * @param state The <code>State</code> to be examined.
     * @return true iff it is contained.
     */
    public boolean contains(State state) {
        HashSet<State> OpenSet = new HashSet<>(queue);
        return OpenSet.contains(state);
    }

}
