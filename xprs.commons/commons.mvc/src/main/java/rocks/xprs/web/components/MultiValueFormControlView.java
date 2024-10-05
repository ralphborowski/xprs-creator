package rocks.xprs.components;

import java.util.HashMap;
import java.util.Map;
import rocks.xprs.util.ReflectiveUtil;

/**
 *
 * @author rborowski
 * @param <T> type of the value
 */
public abstract class MultiValueFormControlView<T> extends DefaultFormControlView<T, Map<String, String>> {

  private static final Map<String, String> UNCHANGED_RAW_VALUE = new HashMap<>();
  private static final String NAMING_PATTERN = "%s.%s";
  private static final String NAMING_PATTERN_SEQENCE = "%s.%s-%d";

  private final String[] partNames;

  public MultiValueFormControlView(Class<T> type, String... partNames) {
    super(type);
    this.partNames = partNames;
  }

  protected abstract T parse(Map<String, String> inputValues);

  @Override
  public Map<String, String> getRawValue() {
    if (getUpdateType() == UpdateType.POST && getContext().isPostback()) {
      Map<String, String> result = new HashMap<>();
      for (String p : partNames) {
        result.put(p, getContext().getPostParameters().getValue(
                String.format(NAMING_PATTERN ,getName(), p),
                getSequence()));
      }
      return result;
    }

    if (getUpdateType() == UpdateType.GET && !getContext().isPostback()) {
      Map<String, String> result = new HashMap<>();
      for (String p : partNames) {
        result.put(p, getContext().getGetParameters().getValue(
                String.format(NAMING_PATTERN ,getName(), p),
                getSequence()));
      }
      return result;
    }

    return UNCHANGED_RAW_VALUE;
  }

  @Override
  public T getValue() {
    Map<String, String> rawValue = getRawValue();
    // NOTE: UNCHANGED_RAW_VALUE is used as a marker for not updating models so null or the empty
    //       string are accepted as valid inputs as well. Direct comparison between strings is the
    //       proper way of doing this, because else the input of the word "unchanged" would skip
    //       the update as well.
    if (rawValue != null && UNCHANGED_RAW_VALUE == rawValue) {
      return (T) ReflectiveUtil.getValue(getModel(), getExpression(), 0);
    }

    return parse(getRawValue());
  }

  protected String getNameForPart(String partName) {
    return String.format(NAMING_PATTERN, getName(), partName);
  }

  protected String getIdForPart(String partName) {
    return String.format(NAMING_PATTERN_SEQENCE, getName(), partName, getSequence());
  }

}
