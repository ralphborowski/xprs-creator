package rocks.xprs.components;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author rborowski
 */
public class MapOptionRenderer implements Select.OptionRenderer<String> {

  private Map<String, String> options;

  public MapOptionRenderer(Map<String, String> options) {
    this.options = options;
  }

  @Override
  public String getId(String object) {
    return object;
  }

  @Override
  public String getLabel(String object) {
    return options.get(object);
  }

  @Override
  public String getValue(String id) {
    return id;
  }

  @Override
  public Collection<String> getOptions() {
    return options.keySet();
  }

}
