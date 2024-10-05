package rocks.xprs.db;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author rborowski
 */
public class ResultList<T> extends LinkedList<T> {

  private final Class<T> type;
  private final int total;
  private final int offset;
  private final int limit;

  public ResultList(Collection<? extends T> c, Class<T> type) {
    super(c);
    this.type = type;
    this.total = c.size();
    this.offset = 0;
    this.limit = c.size();
  }

  public ResultList(Collection<? extends T> c, Class<T> type, int total, int offset, int limit) {
    super(c);
    this.type = type;
    this.total = total;
    this.offset = offset;
    this.limit = limit;
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the type
   */
  public Class<T> getType() {
    return type;
  }

  /**
   * @return the total
   */
  public int getTotal() {
    return total;
  }

  /**
   * @return the offset
   */
  public int getOffset() {
    return offset;
  }

  /**
   * @return the limit
   */
  public int getLimit() {
    return limit;
  }
  //</editor-fold>
}
