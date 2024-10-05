/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.security;

import java.security.SecureRandom;

/**
 *
 * @author borowski
 */
public class RandomCodeGenerator {
  
  public static String generate(int length, String characters) {
    StringBuilder sb = new StringBuilder();
    SecureRandom rand = new SecureRandom();
    for (int i = 0; i < length; i++) {
      sb.append(characters.charAt((Math.abs(rand.nextInt()) % characters.length())));
    }
    
    return sb.toString();
  }

  public static String generateAlphaNumeric(int length) {
    return generate(length, "1234567890qwertzuiopasdfghjklyxcvbnmQWERTZUIOPASDFGHJKLYXCVBNM");
  }
  
  public static String generateNumeric(int length) {
    return generate(length, "1234567890");
  }
}