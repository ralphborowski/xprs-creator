/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rocks.xprs.web.Application;
import rocks.xprs.web.Context;

/**
 *
 * @author borowski
 */
public class ReflectiveUtil {

  private static final Logger LOG = Logger.getLogger(ReflectiveUtil.class.getName());
  private static final List<Class> PRIMITIVE_TYPES = Arrays.asList(Integer.class, Short.class,
          Long.class, BigInteger.class, Float.class, Double.class, BigDecimal.class, Character.class,
          String.class, Boolean.class);

  public static List<Field> getFields(Class clazz) {

    List<Field> result = new LinkedList<>();
    if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
      result.addAll(getFields(clazz.getSuperclass()));
    }

    for (Field f : clazz.getDeclaredFields()) {
      if (!Modifier.isStatic(f.getModifiers())) {
        result.add(f);
      }
    }

    return result;
  }

  public static Method getGetterMethod(Class clazz, String fieldName) {

    // try several types of getters
    String capitalizedFieldName = Strings.capitalize(fieldName);
    String[] methodNames = new String[]{
      "get" + capitalizedFieldName,
      "is" + capitalizedFieldName,
      "has" + capitalizedFieldName,
      fieldName
    };

    for (String methodName : methodNames) {
      try {
        Method method = clazz.getMethod(methodName);
        return method;
      } catch (ReflectiveOperationException ex) {
        // ignore, we are trying methods
      }
    }
    return null;
  }

  public static Object getValue(Object object, String field, int sequence) {

    if (object == null) {
      return null;
    }

    if (field == null) {
      return object;
    }

    // get value from bound object (in form view)
    Object unformattedValue = object;
    for (String v : field.split("\\.")) {

      // stop if value is null
      if (unformattedValue == null) {
        break;
      }

      if (unformattedValue instanceof List) {
        List<Object> list = (List) unformattedValue;
        if (list.size() >= sequence + 1) {
          unformattedValue = list.get(sequence);
        } else {
          unformattedValue = null;
        }
        continue;
      }

      // try several types of getters
      String capitalizedName = Strings.capitalize(v);
      String[] methodNames = new String[]{
        "get" + capitalizedName,
        "is" + capitalizedName,
        "has" + capitalizedName,
        v
      };

      boolean found = false;
      for (String methodName : methodNames) {
        try {
          Method m = unformattedValue.getClass().getMethod(methodName);
          unformattedValue = m.invoke(unformattedValue);
          found = true;
          break;
        } catch (ReflectiveOperationException ex) {
          // ignore, we are trying methods
        }
      }

      // the given attribute path doesn't match the structure of the model object
      if (!found) {
        LOG.log(Level.WARNING, String.format(
            "Object of type %s can't be bound to input by path %s.",
            object.getClass().getCanonicalName(), field));
        return null;
      }
    }

    return unformattedValue;
  }

  public static Method getSetterMethod(Class clazz, String fieldName, Class parameterClass) {

    // try several types of getters
    String methodName = "set" + Strings.capitalize(fieldName);

    try {
      Method method = clazz.getMethod(methodName, parameterClass);
      return method;
    } catch (ReflectiveOperationException ex) {
      // ignore, we are trying methods
    }

    return null;
  }

  public static void setValue(Context context, Object target, String path,
          MultiValuedMap<String, String> values, int sequence) {

    if (target instanceof List) {
      List list = (List) target;
      for (int i = 0; i < list.size(); i++) {
        setValue(context, list.get(i), path, values, i);
      }
    } else {
      for (Field f : ReflectiveUtil.getFields(target.getClass())) {

        // build new path
        String currentPath = "";
        if (Strings.notEmpty(path)) {
          currentPath = path + ".";
        }
        currentPath += f.getName();

        Class type = f.getType();
        if (PRIMITIVE_TYPES.contains(type) || type.isEnum()) {

          try {

            // if primitive type, try to set it
            // get value
            List<String> currentValues = values.get(currentPath);
            String value = null;
            if (currentValues != null && currentValues.size() > sequence) {
              value = currentValues.get(sequence);
            }

            // get setter and try to set value
            Method setter = ReflectiveUtil.getSetterMethod(target.getClass(), f.getName(), type);
            if (setter != null && value != null) {
              Object convertedValue = ((Application) context.getApplication())
                      .getConverters().parse(value, type, context.getLocale());
              setter.invoke(target, convertedValue);
            }
          } catch (ReflectiveOperationException ex) {
            LOG.log(Level.WARNING,
                    String.format("Could not set %s to %s", type.getName(), currentPath),
                    ex);
          }
        } else {

          try {

            // if target is of type object
            // get new target object and create it if neccessary
            Method getter = ReflectiveUtil.getGetterMethod(target.getClass(), currentPath);
            if (getter == null) {
              continue;
            }

            Object newTarget = getter.invoke(target);
            if (newTarget == null) {
              newTarget = getter.getReturnType().getDeclaredConstructor().newInstance();
              Method setter = ReflectiveUtil.getSetterMethod(
                      target.getClass(), currentPath, newTarget.getClass());
              if (setter == null) {
                continue;
              }
              setter.invoke(target, newTarget);
            }

            // fill with values
            setValue(context, newTarget, currentPath, values, sequence);
          } catch (ReflectiveOperationException ex) {
            LOG.log(Level.WARNING,
                    String.format("Could not get %s to %s", type.getName(), currentPath),
                    ex);
          }
        }
      }
    }

  }

}
