package rocks.xprs.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author rborowski
 */
public class MapBuilder<T, U> {

  private LinkedHashMap<T, U> map = new LinkedHashMap<>();

  public MapBuilder() {

  }

  public MapBuilder put(T key, U value) {
    map.put(key, value);
    return this;
  }

  public Map<T, U> build() {
    return map;
  }
}
