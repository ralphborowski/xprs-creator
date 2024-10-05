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
import java.util.Date;

/**
 * Base class for all entities.
 * @author Ralph Borowski
 */
public interface DataItem extends Serializable {
  
  /**
   * Returns the primary key of an entity.
   * @return the primary key
   */
  public Long getId();
  
  /**
   * Set the primary key of an entity.
   * @param id the primary key
   */
  public void setId(Long id);
  
  /**
   * Returns the username that created this item.
   * @return the username
   */
  public String getCreateUser();
  
  /**
   * Set the username that creates this item.
   * @param user th username
   */
  public void setCreateUser(String user);
  
  /**
   * Returns the creation date of this item.
   * @return the username
   */
  public Date getCreateDate();
  
  /**
   * Set the creation date of this item.
   * @param date the creation date
   */
  public void setCreateDate(Date date);
  
  /**
   * Returns the username of the user who made the last changes on this item.
   * @return the username
   */
  public String getModifyUser();
  
  /**
   * Sets the username of the user who made the last changes on this item.
   * @param user the username
   */
  public void setModifyUser(String user);
  
  /**
   * Get the date of the last modification.
   * @return the date
   */
  public Date getModifyDate();
  
  /**
   * Set the date of the last modification.
   * @param date the date
   */
  public void setModifyDate(Date date);
}
