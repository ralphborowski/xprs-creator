/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.exceptions;

/**
 *
 * @author borowski
 */
public class ConfigurationException extends Error {
  
  public ConfigurationException() {
  }
  
  public ConfigurationException(String msg) {
    super(msg);
  }
  
  public ConfigurationException(String msg, Throwable ex) {
    super(msg, ex);
  }
}
