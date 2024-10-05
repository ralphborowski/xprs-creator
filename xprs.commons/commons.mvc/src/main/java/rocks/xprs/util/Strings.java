package rocks.xprs.util;

import java.util.Locale;

/**
 *
 * @author Ralph Borowski
 */
public class Strings {

  public static String capitalize(String string) {

    if (string == null || string.isEmpty()) {
      return string;
    }

    return string.substring(0, 1).toUpperCase(Locale.ENGLISH) + string.substring(1);
  }

  public static String decapitalize(String string) {

    if (string == null || string.isEmpty()) {
      return string;
    }

    return string.substring(0, 1).toLowerCase(Locale.ENGLISH)+ string.substring(1);
  }

  public static boolean isEmpty(String string) {
    return string == null || string.trim().isEmpty();
  }

  public static boolean notEmpty(String string) {
    return !isEmpty(string);
  }

  public static String trim(String string) {
    if (string == null) {
      return null;
    }
    return string.trim();
  }

}
