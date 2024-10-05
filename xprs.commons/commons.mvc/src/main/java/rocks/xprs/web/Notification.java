/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.web;

/**
 *
 * @author borowski
 */
public class Notification {
  
  public enum Type { SUCCESS, ERROR, INFO }
  
  private Type type;
  private String message;
  
  public Notification() {
    
  }
  
  public Notification(Type type, String message) {
    this.type = type;
    this.message = message;
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the type
   */
  public Type getType() {
    return type;
  }
  
  /**
   * @param type the type to set
   */
  public void setType(Type type) {
    this.type = type;
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
  //</editor-fold>
}
