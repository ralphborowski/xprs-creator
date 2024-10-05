package rocks.xprs.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ralph Borowski
 */
public class MultiValuedMap<T, U> extends HashMap<T, List<U>> {

  public MultiValuedMap() {
    super();
  }

  public MultiValuedMap(Map<T, List<U>> map) {
    super(map);
  }

  public U getValue(T o) {
    return (get(o) != null && get(o).size() > 0) ? get(o).get(0) : null;
  }

  public U getValue(T o, int sequence) {
    return (get(o) != null && get(o).size() >= sequence + 1) ? get(o).get(sequence) : null;
  }

  public void setValue(T key, U value) {
    put(key, Arrays.asList(value));
  }

  public void addValue(T key, U value) {
    if (!containsKey(key)) {
      put(key, new LinkedList<>());
    }
    get(key).add(value);
  }
}