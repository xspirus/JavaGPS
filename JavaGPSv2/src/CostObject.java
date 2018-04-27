public interface CostObject<T> {

    public T create(double x, double y);

    public T create(T other);

    public Double distance(T other);

}
