/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.serialization;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rocks.xprs.http.HttpResponse;

/**
 *
 * @author borowski
 */
public class XmlSerializer {

  public static String serialize(Object o) {
    StringBuilder sb = new StringBuilder();

    // if is null
    if (o == null) {
      return "";
    }

    // do not encapsuate primitives, enums and strings
    else if (o.getClass().isPrimitive() ||
             o.getClass().isEnum() ||
             o instanceof Number ||
             o instanceof Boolean ||
             o instanceof String ||
             o instanceof Character) {

      String s = String.valueOf(o);

      if (s.equals("NaN")) {
        return "";
      }

      // escape result
      s = s.replace("&", "&amp;");
      s = s.replace("<", "&lt;");
      s = s.replace(">", "&gt;");

      return s;
    }

    // if it is an array
    else if (o.getClass().getSimpleName().contains("[]")) {
      int length = Array.getLength(o);
      for (int i = 0; i < length; i++) {
        Object o2 = Array.get(o, i);
        sb.append("<").append(o2.getClass().getSimpleName().toLowerCase().replace("[]", "")).append(">");
        sb.append(serialize(o2));
        sb.append("</").append(o2.getClass().getSimpleName().toLowerCase().replace("[]", "")).append(">");
      }
    }

    // if it is any kind of list, iterate through it
    else if (o instanceof Iterable) {
      Iterable i = (Iterable) o;

      for (Iterator it = i.iterator(); it.hasNext();) {
        Object o2 = it.next();
        sb.append("<").append(o2.getClass().getSimpleName().toLowerCase().replace("[]", "")).append(">");
        sb.append(serialize(o2));
        sb.append("</").append(o2.getClass().getSimpleName().toLowerCase().replace("[]", "")).append(">");
      }
    }

    // if we have a map
    else if (o instanceof Map) {
      Map m = (Map) o;

      for(Iterator<Map.Entry> it = m.entrySet().iterator(); it.hasNext();) {
        Map.Entry set = it.next();

        if (set.getKey() == null) {
          continue;
        }

        String key = set.getKey().toString();
        if (set.getKey() instanceof Date) {
          key = "D" + String.valueOf(((Date)set.getKey()).getTime());
        }

        sb.append("<").append(key).append(">");
        sb.append(serialize(set.getValue()));
        sb.append("</").append(key).append(">");
      }
    }

    // if we have a date
    else if (o instanceof Date) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"); // CHECK CHECK CHECK!!
      sb.append(sdf.format((Date) o));
    }

    else if (o instanceof LocalDate) {
      return ((LocalDate) o).toString();
    }

    // if we have a ConnectResponse
    else if (o instanceof HttpResponse) {
      sb.append(removeRoot(((HttpResponse)o).getData()));
    }

    else if (o instanceof Locale) {
      return ((Locale) o).toString();
    }

    // o is a "real" object
    else {

      Class c = o.getClass();

      // get methods
      Method[] methods = c.getMethods();

      // prepare parameters
      Class[] typelist = new Class[]{};
      Object[] arglist = new Object[]{};

      // iterate through fields
      for (Method m : methods) {

        // check if is getter
        if (Arrays.equals(m.getParameterTypes(), typelist)
                && (m.getName().startsWith("get") || m.getName().startsWith("is")
                        || m.getName().startsWith("has"))
                && !((m.getName().equals("hashCode")) || m.getName().equals("getClass"))
                && !m.getReturnType().equals(Void.class)
                && !m.isSynthetic()) {

          try {
            Object o2 = m.invoke(o, arglist);

            if (o2 != null) {
              // build field name
              String fieldname = m.getName();

              if (fieldname.startsWith("is")) {
                fieldname = fieldname.substring(2);
              } else {
                fieldname = fieldname.substring(3);
              }

              fieldname = fieldname.substring(0, 1).toLowerCase() + fieldname.substring(1);

              // serialize it
              sb.append("<").append(fieldname).append(">");
              sb.append(serialize(o2));
              sb.append("</").append(fieldname).append(">");
            }

          } catch (Exception ex) {
            Logger.getLogger(XmlSerializer.class.getName()).log(Level.WARNING,
                    String.format("Error invoking method %s on object of type %s",
                            m.getName(), o.getClass().getSimpleName()),
                    ex);
          }

        }
      }
    }

    // tidy up null values
    // String result = sb.toString().replaceAll("<([a-zA-Z][a-zA-Z0-9]*)></([a-zA-Z][a-zA-Z0-9]*)>", "");
    String result = sb.toString().replace("<id>0</id>", "");
    result = result.replaceAll("<([a-zA-Z][a-zA-Z0-9]*)>false</([a-zA-Z][a-zA-Z0-9]*)>", "");

    return result;
  }

  private static String removeRoot(String s) {
    // get first element's name
    Pattern pattern = Pattern.compile("<(.*?)>");
    Matcher matcher = pattern.matcher(s);

    if (matcher.find()) {

      String oldRoot = matcher.group(1);

      if (s.length() > (2 * oldRoot.length()) ) {

        // return string between
        return s.substring(oldRoot.length() + 2, s.length() - oldRoot.length() - 3).trim();
      } else {
        return s.replace(oldRoot, "").trim();
      }
    } else {
      // return string
      return s.trim();
    }
  }
}
