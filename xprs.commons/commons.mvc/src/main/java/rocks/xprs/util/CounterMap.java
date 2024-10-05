/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author borowski
 * @param <T> type of the elements to count
 */
public class CounterMap<T> {

  private final Map<T, Integer> counterMap = new HashMap<>();

  public int add(T element) {
    if (!counterMap.containsKey(element)) {
      counterMap.put(element, 0);
    }

    int newCount = counterMap.get(element) + 1;
    counterMap.put(element, newCount);

    return newCount;
  }

  public int get(T element) {
    if (!counterMap.containsKey(element)) {
      return 0;
    }

    return counterMap.get(element);
  }

  public Set<Map.Entry<T, Integer>> entrySet() {
    return counterMap.entrySet();
  }

  public Set<T> keySet() {
    return counterMap.keySet();
  }

}
