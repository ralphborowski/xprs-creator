package rocks.xprs.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rocks.xprs.db.Entity;
import rocks.xprs.web.Context;

/**
 *
 * @author Ralph Borowski <ralph.borowski@borowski.it>
 * @param <T> type of the entity to render
 */
public class EntityOptionRenderer<T extends Entity> implements Select.OptionRenderer<T> {

  private final Map<String, T> options = new HashMap<>();
  private final Context context;

  public EntityOptionRenderer(List<T> options, Context context) {
    this.context = context;
    for (T o : options) {
      this.options.put(getId(o), o);
    }
  }

  @Override
  public final String getId(T object) {
    return String.valueOf(object.getId());
  }

  @Override
  public String getLabel(T object) {
    return context.format(object);
  }

  @Override
  public T getValue(String id) {
    return options.get(id);
  }

  @Override
  public Collection<T> getOptions() {
    return options.values();
  }

}
