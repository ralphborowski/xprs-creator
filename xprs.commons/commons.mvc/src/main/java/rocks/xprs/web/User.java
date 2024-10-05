/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.web;

import java.security.Principal;
import java.util.Locale;

/**
 *
 * @author borowski
 */
public interface User extends Principal {
  
  public Locale getLocale();
  public String getTimezone();
  
}
