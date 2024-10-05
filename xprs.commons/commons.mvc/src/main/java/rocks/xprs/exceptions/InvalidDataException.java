/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.exceptions;

import java.util.List;
import java.util.Map;

/**
 *
 * @author borowski
 */
public class InvalidDataException extends XprsException {
  
  private Map<String, List<String>> errors;
  
  public InvalidDataException() {
  }
  
  public InvalidDataException(String msg) {
    super(msg);
  }
  
  public InvalidDataException(String msg, Map<String, List<String>> errors) {
    super(msg);
    this.errors = errors;
  }
  
  public InvalidDataException(String msg, Throwable ex) {
    super(msg, ex);
  }
  
  public Map<String, List<String>> getErrors() {
    return this.errors;
  }

  public void setErrors(Map<String, List<String>> errors) {
    this.errors = errors;
  }
}