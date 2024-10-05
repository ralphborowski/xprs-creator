/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.exceptions;

/**
 *
 * @author borowski
 */
public class InternalErrorException extends XprsException {
  
  public InternalErrorException() {
  }
  
  public InternalErrorException(String msg) {
    super(msg);
  }
  
  public InternalErrorException(String msg, Throwable ex) {
    super(msg, ex);
  }
}
