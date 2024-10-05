/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.exceptions;

/**
 *
 * @author borowski
 */
public class ResourceNotFoundException extends XprsException {
  
  public ResourceNotFoundException() {
  }
  
  public ResourceNotFoundException(String msg) {
    super(msg);
  }
  
  public ResourceNotFoundException(String msg, Throwable ex) {
    super(msg, ex);
  }
}
