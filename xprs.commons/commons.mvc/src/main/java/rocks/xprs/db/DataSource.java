package rocks.xprs.db;

import java.util.List;

/**
 *
 * @author Ralph Borowski
 * @param <T> the return type of the data source
 */
public interface DataSource<T> {

  public T get(String id);
  public List<T> get(List<String> ids);

  public List<T> search(String keyword);

  public void delete(String id);
  public void delete(List<String> ids);

}
