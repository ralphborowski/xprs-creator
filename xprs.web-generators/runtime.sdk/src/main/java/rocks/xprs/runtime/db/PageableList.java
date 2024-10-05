/*
 * The MIT License
 *
 * Copyright 2016 Ralph Borowski.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package rocks.xprs.runtime.db;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Return type of a database list that is limited in results.
 * @author Ralph Borowski
 * @param <T>
 */
public class PageableList<T extends DataItem> implements Serializable {

  private int limit;
  private int offset;
  private int currentNumberOfResults;
  private int totalNumberOfResults;
  private List<T> items = Collections.EMPTY_LIST;

  public PageableList() {

  }

  public PageableList(List<T> result) {
    this.limit = result.size();
    this.totalNumberOfResults = this.limit;
    this.currentNumberOfResults = this.limit;
    this.offset = 0;

    this.items = result;
  }

  public PageableList(PageableFilter filter, List<T> result) {

    // copy settings
    this.limit = filter.getLimit();
    this.offset = filter.getOffset();

    // set total
    totalNumberOfResults = result.size();

    // calculate list
    if (result.size() > offset) {
      if (limit == -1 || totalNumberOfResults < offset + limit ) {
        items = result.subList(offset, totalNumberOfResults);
      } else {
        items = result.subList(offset, offset + limit);
      }
    }

    // set current number of results
    currentNumberOfResults = items.size();
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the limit
   */
  public int getLimit() {
    return limit;
  }

  /**
   * @param limit the limit to set
   */
  public void setLimit(int limit) {
    this.limit = limit;
  }

  /**
   * @return the offset
   */
  public int getOffset() {
    return offset;
  }

  /**
   * @param offset the offset to set
   */
  public void setOffset(int offset) {
    this.offset = offset;
  }

  /**
   * @return the currentNumberOfResults
   */
  public int getCurrentNumberOfResults() {
    return currentNumberOfResults;
  }

  /**
   * @param currentNumberOfResults the currentNumberOfResults to set
   */
  public void setCurrentNumberOfResults(int currentNumberOfResults) {
    this.currentNumberOfResults = currentNumberOfResults;
  }

  /**
   * @return the totalNumberOfResults
   */
  public int getTotalNumberOfResults() {
    return totalNumberOfResults;
  }

  /**
   * @param totalNumberOfResults the totalNumberOfResults to set
   */
  public void setTotalNumberOfResults(int totalNumberOfResults) {
    this.totalNumberOfResults = totalNumberOfResults;
  }

  /**
   * @return the items
   */
  public List<T> getItems() {
    return items;
  }

  /**
   * @param items the items to set
   */
  public void setItems(List<T> items) {
    this.items = items;
  }
//</editor-fold>

}
