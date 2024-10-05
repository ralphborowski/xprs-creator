/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author borowski
 */
public class Md5 {
  
  public static String getHash(String plaintext) {
    try {
      
      // hash text
      MessageDigest m = MessageDigest.getInstance("MD5");
      m.reset();
      m.update(plaintext.getBytes());
      byte[] digest = m.digest();
      
      // convert to string
      BigInteger bigInt = new BigInteger(1,digest);
      String hashtext = bigInt.toString(16);
      
      // add a leading zero if neccessary
      while(hashtext.length() < 32 ){
        hashtext = "0"+hashtext;
      }
      
      // return result
      return hashtext;
    } catch (NoSuchAlgorithmException ex) {
      // should never happen
      throw new RuntimeException(ex);
    }
  }
  
}
