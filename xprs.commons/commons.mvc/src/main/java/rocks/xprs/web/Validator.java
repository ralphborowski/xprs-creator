/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.web;

/**
 *
 * @author borowski
 */
public class Validator {

  public static boolean notEmpty(String string) {
    return (string != null && !string.trim().isEmpty());
  }

  public static boolean isEmpty(String string) {
    return !notEmpty(string);
  }

  public static boolean isNumber(String string) {
    return (string != null && string.matches("\\d+"));
  }

  public static boolean isDouble(String string) {
    return (string != null && string.matches("[0-9]+[,\\.]?[0-9]{0,2}"));
  }

  public static boolean isDate(String string) {
    return (string != null && (string.matches("[1-9][0-9][0-9][0-9]-[0|1][0-9]-[0-3][0-9]") || string.matches("[0-3][0-9]\\.[0|1][0-9]\\.[1-9][0-9][0-9][0-9]")));
  }

  public static boolean isEmail(String string) {
    return (string != null && string.matches("[a-zA-Z0-9._+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]+"));
  }

  public static boolean isPhoneNumber(String string) {
    return (string != null && string.matches("[\\+]?[0-9\\s\\-]{1,20}"));
  }
  public static boolean isIBAN(String string) {
    return (string != null && string.matches("[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}"));
  }
  public static boolean isBIC(String string) {
    return (string != null && string.matches("([a-zA-Z]{4}[a-zA-Z]{2}[a-zA-Z0-9]{2}([a-zA-Z0-9]{3})?)"));
  }

  public static boolean maxLength(String string, int length) {
    return (string != null && string.length() <= length);
  }

  public static boolean minLength(String string, int length) {
    return (string != null && string.length() >= length);
  }
  
  public static Validator validate(String string) {
    return new Validator(string);
  }
  
  private String string;
  private boolean isValid = false;
  private boolean optional = false;
  
  private Validator(String string) {
    this.string = string;
  }
  
  public Validator isRequired() {
    this.isValid &= Validator.notEmpty(string);
    return this;
  }
  
  public Validator isOptional() {
    this.isValid &= true;
    return this;
  }
  
  public Validator isNumber() {
    this.isValid &= string == null || Validator.isNumber(string);
    return this;
  }
  
  public Validator isDouble() {
    this.isValid &= string == null || Validator.isDouble(string);
    return this;
  }
  
  public Validator isDate() {
    this.isValid &= string == null || Validator.isDate(string);
    return this;
  }
  
  public Validator hasMaxLength(int maxLength) {
    this.isValid &= string == null || string.length() <= maxLength;
    return this;
  }
  
  public Validator hasMinLength(int minLength) {
    this.isValid &= string == null || string.length() >= minLength;
    return this;
  }
  
  public boolean isValid() {
    return isValid;
  }
}
