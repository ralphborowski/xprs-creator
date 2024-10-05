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

/**
 * Range for use in filters.
 *
 * @param <T> type of the range
 * @author Ralph Borowski
 */
public class Range<T> {

  private T start;
  private T end;

  /**
   * Creates a new TimeRange.
   */
  public Range() {

  }

  /**
   * Creates a new TimeRange.
   * @param start start of the time range
   * @param end end of the time range
   */
  public Range(T start, T end) {
    this.start = start;
    this.end = end;
  }

  //<editor-fold defaultstate="collapsed" desc="Gettters and Setters">
  /**
   * @return the start
   */
  public T getStart() {
    return start;
  }

  /**
   * @param start the start to set
   */
  public void setStart(T start) {
    this.start = start;
  }

  /**
   * @return the end
   */
  public T getEnd() {
    return end;
  }

  /**
   * @param end the end to set
   */
  public void setEnd(T end) {
    this.end = end;
  }
  //</editor-fold>

}
