/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.web;

/**
 *
 * @author borowski
 */
public interface Controller {
  
  public void init(Context context) throws Exception;
  
  public abstract void process() throws Exception;
  
}