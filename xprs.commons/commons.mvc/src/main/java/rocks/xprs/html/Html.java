package rocks.xprs.html;

/**
 *
 * @author Ralph Borowski <ralph.borowski@borowski.it>
 */
public class Html {

  public static String escapeText(String text) {
    return escapeAttribute(text);
  }

  public static String escapeAttribute(String attributeValue) {
    if (attributeValue == null) {
      return "";
    }
    return attributeValue.replace("&", "&amp;").replace("\"", "&quot;").replace("'", "&#39;")
          .replace("<", "&lt;").replace(">", "&gt;").replace("\n", "").replace("\r", "")
          .replace("\t", "");
  }

  public static String escapeAttributeName(String attributeName) {
    String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-:";
    char[] target = new char[attributeName.length()];
    int targetPos = 0;
    for (char c : attributeName.toCharArray()) {
      if (allowedChars.contains(String.valueOf(c))) {
        target[targetPos++] = c;
      }
    }
    return new String(target);
  }

}
