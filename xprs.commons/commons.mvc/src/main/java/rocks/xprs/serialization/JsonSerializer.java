/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.serialization;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;

/**
 *
 * @author Ralph Borowski<ralph.borowski@exelonix.com>
 */
public class JsonSerializer {

  private final static Logger LOGGER = Logger.getLogger(JsonSerializer.class.getName());
  private static final String CLASS_ATTR = "class";
  private static final String MAP_ATTR = "Map";

  private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
          "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

  private final Map<String, JsonValue> numbers = new HashMap<>();
  private final Map<String, JsonValue> strings = new HashMap<>();
  private final Map<String, Class> knownTypes = new HashMap<>();

  public JsonSerializer() {
    knownTypes.put("Map", HashMap.class);
  }

  public void registerKnownType(Class... clazz) {
    for (Class c : clazz) {
      knownTypes.put(c.getSimpleName(), c);
    }
  }

  public String serialize(Object o) {
    return serializeJson(o).toString();
  }

  public void serialize(Object o, Writer writer) throws IOException {

    JsonValue jsonContent = serializeJson(o);
    if (jsonContent == null) {
      writer.write(JsonValue.NULL.toString());
    } else if (jsonContent instanceof JsonStructure) {
      Json.createWriter(writer).write((JsonStructure) jsonContent);
    } else {
      writer.write(jsonContent.toString());
    }

    writer.flush();
  }

  private JsonValue serializeJson(Object o) {

    if (o == null) {
      return JsonValue.NULL;

    } else if (o instanceof Number) {
      String key = String.valueOf(o);
      if (!numbers.containsKey(key)) {
        numbers.put(key, new JsonNumberImpl((Number) o));
      }
      return numbers.get(key);

    } else if (o instanceof Boolean) {
      return (Boolean) o ? JsonValue.TRUE : JsonValue.FALSE;

    } else if (o instanceof Character || o instanceof String || o.getClass().isEnum()) {
      String key = String.valueOf(o);
      if (!strings.containsKey(key)) {
        strings.put(key, new JsonStringImpl(o));
      }
      return strings.get(key);

    } else if (o instanceof Date) {
      String key = dateFormatter.format((Date) o);
      if (!strings.containsKey(key)) {
        strings.put(key, new JsonStringImpl(key));
      }
      return strings.get(key);

    } else if (o instanceof LocalDate) {
      String key = DateTimeFormatter.ISO_LOCAL_DATE.format((LocalDate) o);
      if (!strings.containsKey(key)) {
        strings.put(key, new JsonStringImpl(key));
      }
      return strings.get(key);

    } else if (o instanceof LocalDateTime) {
      String key = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format((LocalDateTime) o);
      if (!strings.containsKey(key)) {
        strings.put(key, new JsonStringImpl(key));
      }
      return strings.get(key);

    } else if (o instanceof LocalTime) {
      String key = DateTimeFormatter.ISO_LOCAL_TIME.format((LocalTime) o);
      if (!strings.containsKey(key)) {
        strings.put(key, new JsonStringImpl(key));
      }
      return strings.get(key);

    } else if (o instanceof Collection) {
      Collection c = (Collection) o;
      JsonArrayBuilder jsonArray = Json.createArrayBuilder();
      for (Object i : c) {
        if (i == null) {
          jsonArray.addNull();
        } else {
          jsonArray.add(serializeJson(i));
        }
      }
      return jsonArray.build();

    } else if (o instanceof Map) {
      JsonObjectBuilder jsonObject = Json.createObjectBuilder();

      Map<Object, Object> m = (Map<Object, Object>) o;
      for (Map.Entry<Object, Object> e : m.entrySet()) {
        jsonObject.add(String.valueOf(e.getKey()), serializeJson(e.getValue()));
      }

      return jsonObject.build();

    } else {

      JsonObjectBuilder jsonObject = Json.createObjectBuilder();

      // add classname to restore it properly in deserialization phase
      jsonObject.add(CLASS_ATTR, o.getClass().getSimpleName());

      Class currentClass = o.getClass();
      while (currentClass != null && !currentClass.equals(Object.class)) {
        for (Field f : currentClass.getDeclaredFields()) {

          // ignore static fields
          if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers())) {
            continue;
          }

          try {

            // check access rights (usually fields are private)
            boolean isAccessable = f.canAccess(o);
            if (!isAccessable && !f.trySetAccessible()) {
              // ignore field, it's not accessable
              continue;
            }

            // add field to json object
            jsonObject.add(f.getName(), serializeJson(f.get(o)));

            // restore access rights if needed
            if (!isAccessable) {
              f.setAccessible(false);
            }
          } catch (ReflectiveOperationException ex) {
            LOGGER.log(Level.SEVERE,
                    String.format("Could not serialize field %s of class %s.",
                            f.getName(), o.getClass().getCanonicalName()),
                    ex);
          }
        }
        currentClass = currentClass.getSuperclass();
      }

      return jsonObject.build();
    }
  }

  public Object deserialize(String jsonString) throws IOException {
    return deserialize(new StringReader(jsonString));
  }

  public Object deserialize(Reader reader) throws IOException {
    try {
      JsonReader jsonReader = Json.createReader(reader);
      JsonStructure jsonStructure = jsonReader.read();
      return deserializeJson(jsonStructure);
    } catch (JsonException ex) {
      throw new IOException(ex);
    }
  }

  private Object deserializeJson(JsonValue jsonValue) throws IOException {

    switch (jsonValue.getValueType()) {

      case NULL:
        return null;

      case TRUE:
        return true;

      case FALSE:
        return false;

      case STRING:
        return ((JsonString) jsonValue).getString();

      case NUMBER:
        return ((JsonNumber) jsonValue);

      case ARRAY:
        List list = new LinkedList();
        JsonArray jsonArray = (JsonArray) jsonValue;
        for (JsonValue v : jsonArray) {
          list.add(deserializeJson(v));
        }
        return list;

      case OBJECT:
        // get class
        JsonObject jsonObject = (JsonObject) jsonValue;

        // dezerialize Map (if no class information)
        if (!jsonObject.containsKey(CLASS_ATTR)
                || jsonObject.getString(CLASS_ATTR).equals(MAP_ATTR)) {

          Map<String, Object> result = new HashMap<>();

          for (Map.Entry<String, JsonValue> e : jsonObject.entrySet()) {
            result.put(e.getKey(), deserializeJson(e.getValue()));
          }

          return result;
        }

        String classname = jsonObject.getString(CLASS_ATTR);

        // ignore unknown or unallowed classes
        if (!knownTypes.containsKey(classname)) {
          return null;
        }

        // create and fill a new instance
        try {
          Class clazz = knownTypes.get(classname);
          Object instance = clazz.getDeclaredConstructor().newInstance();

          Class currentClass = clazz;
          while (!currentClass.equals(Object.class)) {
            for (Field f : currentClass.getDeclaredFields()) {

              // ignore fields that are not in json or constants
              if (!jsonObject.containsKey(f.getName()) || Modifier.isStatic(f.getModifiers())
                      || Modifier.isFinal(f.getModifiers())) {
                continue;
              }

              // deserialize json value
              Object o = deserializeJson(jsonObject.get(f.getName()));

              // check if conversion is needed
              if (o != null) {
                Class attributeType = f.getType();
                if (!isSubclassOf(o.getClass(), attributeType)) {

                  if (o instanceof String) {
                    String stringValue = (String) o;

                    if (attributeType.isEnum()) {
                      o = Enum.valueOf(attributeType, stringValue);
                    } else if (attributeType.equals(Date.class)) {
                      try {
                        o = dateFormatter.parse(stringValue);
                      } catch (ParseException ex) {
                        LOGGER.log(Level.WARNING,
                                String.format("Could not parse date %s for attribute %s.%s",
                                        stringValue, clazz.getSimpleName(), f.getName()),
                                ex);
                      }
                    } else if (attributeType.equals(LocalDate.class)) {
                      o = DateTimeFormatter.ISO_LOCAL_DATE.parse(stringValue);
                    } else if (attributeType.equals(LocalTime.class)) {
                      o = DateTimeFormatter.ISO_LOCAL_TIME.parse(stringValue);
                    } else if (attributeType.equals(LocalDateTime.class)) {
                      o = DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(stringValue);
                    } else if (attributeType.equals(LocalDate.class)) {
                      o = DateTimeFormatter.ISO_LOCAL_DATE.parse(stringValue);
                    } else if (attributeType.equals(Boolean.class)) {
                      o = Boolean.parseBoolean(stringValue);
                    }
                  } else if (o instanceof JsonNumber) {
                    JsonNumber jsonNumber = (JsonNumber) o;

                    if (attributeType.equals(Integer.class)
                            || attributeType.equals(int.class)) {
                      o = jsonNumber.intValueExact();
                    } else if (attributeType.equals(Short.class)
                            || attributeType.equals(short.class)) {
                      o = jsonNumber.intValueExact();
                    } else if (attributeType.equals(Long.class)
                            || attributeType.equals(long.class)) {
                      o = jsonNumber.longValueExact();
                    } else if (attributeType.equals(Float.class)
                            || attributeType.equals(float.class)) {
                      o = ((Double) jsonNumber.doubleValue()).floatValue();
                    } else if (attributeType.equals(Double.class)
                            || attributeType.equals(double.class)) {
                      o = jsonNumber.doubleValue();
                    } else if (attributeType.equals(BigInteger.class)) {
                      o = jsonNumber.bigIntegerValueExact();
                    } else if (attributeType.equals(BigDecimal.class)) {
                      o = jsonNumber.bigDecimalValue();
                    }
                  } else {
                    o = null;
                  }
                }
              }

              // check access rights (usually fields are private)
              boolean isAccessable = f.canAccess(instance);
              if (!isAccessable && !f.trySetAccessible()) {
                // ignore field, it's not accessable
                continue;
              }

              // add field to json object
              f.set(instance, o);

              // restore access rights if needed
              if (!isAccessable) {
                f.setAccessible(false);
              }
            }

            currentClass = currentClass.getSuperclass();
          }

          return instance;
        } catch (ReflectiveOperationException ex) {
          throw new IOException(
                  String.format("Could not deserialize class %s.",
                          classname),
                  ex);
        }

      default:
        return null;
    }
  }

  private boolean isSubclassOf(Class class1, Class class2) {

    if (class1 == null || class2 == null) {
      return false;
    }

    if (class1.isPrimitive()) {
      class1 = getClassForPrimitive(class1);
    }

    if (class2.isPrimitive()) {
      class2 = getClassForPrimitive(class2);
    }

    return class2.isAssignableFrom(class1);
  }

  private Class getClassForPrimitive(Class primitiveClass) {
    if (primitiveClass == int.class) {
      return Integer.class;
    } else if (primitiveClass == byte.class) {
      return Byte.class;
    } else if (primitiveClass == long.class) {
      return Long.class;
    } else if (primitiveClass == short.class) {
      return Short.class;
    } else if (primitiveClass == float.class) {
      return Float.class;
    } else if (primitiveClass == double.class) {
      return Double.class;
    } else if (primitiveClass == char.class) {
      return Character.class;
    } else if (primitiveClass == boolean.class) {
      return Boolean.class;
    } else {
      LOGGER.log(Level.SEVERE, "Missing case in primitive detection: %s", primitiveClass.getName());
      return primitiveClass;
    }
  }

  private class JsonNumberImpl implements JsonNumber {

    private final Number number;

    public JsonNumberImpl(Number number) {
      this.number = number;
    }

    @Override
    public boolean isIntegral() {
      return number instanceof Integer || number instanceof Long;
    }

    @Override
    public int intValue() {
      return number.intValue();
    }

    @Override
    public int intValueExact() {
      return number.intValue();
    }

    @Override
    public long longValue() {
      return number.longValue();
    }

    @Override
    public long longValueExact() {
      return number.longValue();
    }

    @Override
    public BigInteger bigIntegerValue() {
      return new BigInteger(String.valueOf(number));
    }

    @Override
    public BigInteger bigIntegerValueExact() {
      return new BigInteger(String.valueOf(number));
    }

    @Override
    public double doubleValue() {
      return number.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
      return new BigDecimal(String.valueOf(number));
    }

    @Override
    public ValueType getValueType() {
      return ValueType.NUMBER;
    }

    @Override
    public String toString() {
      return String.valueOf(number);
    }
  }

  private static class JsonStringImpl implements JsonString {

    private final String string;

    public JsonStringImpl(Object string) {
      this.string = String.valueOf(string);
    }

    @Override
    public String getString() {
      return string;
    }

    @Override
    public CharSequence getChars() {
      return string;
    }

    @Override
    public ValueType getValueType() {
      return ValueType.STRING;
    }

    @Override
    public String toString() {
      return string;
    }
  }

}
