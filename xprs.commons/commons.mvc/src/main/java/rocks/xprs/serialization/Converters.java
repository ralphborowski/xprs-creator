/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.serialization;

import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author borowski
 */
public class Converters {

  private static final Logger LOG = Logger.getLogger(Converters.class.getName());

  private final HashMap<Class, Class> converters = new HashMap<>();
  private final HashMap<Locale, HashMap<Class, Converter>> instances = new HashMap<>();

  public void register(Class<? extends Converter> clazz) {
    try {
      Converter converter = clazz.getDeclaredConstructor(Locale.class).newInstance(Locale.UK);

      for (Class c : converter.canFormat()) {
        converters.put(c, clazz);
      }
    } catch (ReflectiveOperationException ex) {
      LOG.log(Level.WARNING,
          String.format("Could not register converter %s.", clazz.getCanonicalName()),
          ex);
    }
  }

  public String format(Object value, Locale locale) {

    // check for null
    if (value == null) {
      return "";
    }

    // format with the best matching converter
    Class currentClass = value.getClass();
    initConverters(locale, currentClass);
    HashMap<Class, Converter> converterMap = instances.get(locale);
    while (!currentClass.equals(Object.class)) {
      if (converterMap.containsKey(currentClass)) {
        return converterMap.get(currentClass).format(value);
      }
      currentClass = currentClass.getSuperclass();
      initConverters(locale, currentClass);
    }

    return value.toString();
  }

  public <T> T parse(String string, Class<T> clazz, Locale locale) {

    // check for null
    if (string == null) {
      return null;
    }

    // try to parse
    initConverters(locale, clazz);
    HashMap<Class, Converter> converterMap = instances.get(locale);
    if (converterMap.containsKey(clazz)) {
      return converterMap.get(clazz).parse(string, clazz);
    }

    // return null if nothing was found
    return null;
  }

  private void initConverters(Locale locale, Class clazz) {

    // check if the locale was initialized already
    if (!instances.containsKey(locale)) {
      instances.put(locale, new HashMap<>());
    }
    HashMap<Class, Converter> converterMap = instances.get(locale);

    // check if a converter is available and initialized
    if (!converterMap.containsKey(clazz)
        && converters.containsKey(clazz)) {

      try {
        Converter converter = (Converter) converters.get(clazz)
            .getDeclaredConstructor(Locale.class).newInstance(locale);

        for (Class c : converter.canFormat()) {
          converterMap.put(c, converter);
        }
      } catch (ReflectiveOperationException ex) {
        LOG.log(Level.WARNING,
          String.format("Could not init converter for %s with locale %s.",
              clazz.getCanonicalName(), locale.toString()),
          ex);
      }
    }
  }

}
