/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

/**
 *
 * @author borowski
 */
public interface Validator<T> {

  public String isValid(String expression, T value);

}
