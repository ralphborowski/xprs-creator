/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.serialization;

import java.util.List;
import java.util.Locale;

/**
 *
 * @author borowski
 */
public abstract class Converter {

  public Converter(Locale locale) {

  }

  public abstract List<Class> canFormat();

  public abstract String format(Object o);

  public abstract <T> T parse(String string, Class<T> targetClass);

}
