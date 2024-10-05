/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.exceptions;

/**
 *
 * @author borowski
 */
public class XprsException extends Exception {
  
  private String message;
  
  public XprsException() {
  }
  
  public XprsException(String msg) {
    super(msg);
    message = msg;
  }
  
  public XprsException(String msg, Throwable ex) {
    super(msg, ex);
    message = msg;
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }
  
  
}
