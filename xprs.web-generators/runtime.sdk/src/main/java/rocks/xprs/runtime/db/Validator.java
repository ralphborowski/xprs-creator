/*
 * The MIT License
 *
 * Copyright 2016 Ralph Borowski.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package rocks.xprs.runtime.db;

/**
 * Provides funtions to test for a common format.
 * @author Ralph Borowski
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
  
  public static boolean isInteger(String string) {
    return (string != null && string.matches("-?\\d+"));
  }

  public static boolean isDecimal(String string) {
    return (string != null && string.matches("-?\\d+(\\.\\d+)?"));
  }

  public static boolean isDate(String string) {
    return (string != null && (string.matches("[1-9][0-9][0-9][0-9]-[0|1][0-9]-[0-3][0-9]") || string.matches("[0-3][0-9]\\.[0|1][0-9]\\.[1-9][0-9][0-9][0-9]")));
  }
  
  public static boolean isTime(String string) {
    return (string != null && string.matches("[0-2][0-9]:[0-6][0-9]"));
  }
  
  public static boolean isDateTime(String string) {
    return (string != null && string.matches("[1-9][0-9][0-9][0-9]-[0|1][0-9]-[0-3][0-9] [0-2][0-9]:[0-6][0-9]"));
  }

  public static boolean isEmail(String string) {
    return (string != null && string.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"));
  }

  public static boolean isPhoneNumber(String string) {
    return (string != null && string.matches("[\\+]?[0-9\\s\\-]{3,25}"));
  }

  public static boolean maxLength(String string, int length) {
    return (string != null && string.length() <= length);
  }

  public static boolean minLength(String string, int length) {
    return (string != null && string.length() >= length);
  }
}
