package rocks.xprs.db;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ralph Borowski
 */
public class DataSourceRegister {

  private static final Map<String, DataSource> registry = new HashMap<>();

  public static void register(Class clazz, DataSource dataSource) {
    registry.put(clazz.getCanonicalName(), dataSource);
  }

  public static void unregister(Class clazz) {
    registry.remove(clazz.getCanonicalName());
  }

  public static DataSource lookup(Class clazz) {
    return registry.get(clazz.getCanonicalName());
  }

}
