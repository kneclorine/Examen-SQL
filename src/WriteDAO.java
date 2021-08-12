public interface WriteDAO <T> {
    public void add(T type);
    public void update(T type);
    public void delete(T type);
}
