/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.exceptions;

/**
 *
 * @author borowski
 */
public class AccessDeniedException extends XprsException {
  
  public AccessDeniedException() {
  }
  
  public AccessDeniedException(String msg) {
    super(msg);
  }
  
  public AccessDeniedException(String msg, Throwable ex) {
    super(msg, ex);
  }
}
