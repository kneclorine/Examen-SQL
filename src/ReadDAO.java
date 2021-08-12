import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

public interface ReadDAO <T>{
    public T get(UUID id) throws SQLException;
    public Set<T> getAll() throws SQLException;
}
