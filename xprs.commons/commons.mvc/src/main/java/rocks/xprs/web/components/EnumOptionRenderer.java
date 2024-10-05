package rocks.xprs.components;

import rocks.xprs.util.Translator;
import java.util.Arrays;
import java.util.Collection;
import rocks.xprs.util.Strings;
import rocks.xprs.web.Context;

/**
 *
 * @author rborowski
 */
public class EnumOptionRenderer<T> implements Select.OptionRenderer<T> {

  private Class<Enum> enumClass;

  public EnumOptionRenderer(Class<Enum> enumClass) {
    this.enumClass = enumClass;
  }

  @Override
  public String getId(T object) {
    return object.toString();
  }

  @Override
  public String getLabel(T object) {
    return Translator.get(Context.get().getLocale()).get(
            Strings.decapitalize(enumClass.getSimpleName()) + ".options." + object.toString());
  }

  @Override
  public T getValue(String id) {
    return (T) Enum.valueOf(enumClass, id);
  }

  @Override
  public Collection<T> getOptions() {
    return (Collection<T>) Arrays.asList(enumClass.getEnumConstants());
  }

}
