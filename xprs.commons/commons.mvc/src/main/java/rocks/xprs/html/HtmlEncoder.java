package rocks.xprs.html;

/**
 *
 * @author Ralph Borowski
 */
public class HtmlEncoder {

  public static String encode(String text) {
    if (text == null) {
      return "";
    }
    return text.replace("&", "&amp;").replace("<", "&lt;")
            .replace(">", "&gt;").replace("\"", "&quot;");
  }

  public static String decode(String encoded) {
    if (encoded == null) {
      return null;
    }
    return encoded.replace("&quot;", "\"").replace("&gt;", ">")
            .replace("&lt;", "<").replace("&amp;", "&");
  }

}
