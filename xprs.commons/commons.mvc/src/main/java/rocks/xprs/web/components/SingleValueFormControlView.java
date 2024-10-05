/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import rocks.xprs.serialization.Converters;
import rocks.xprs.util.ReflectiveUtil;
import rocks.xprs.web.Application;

/**
 *
 * @author borowski
 */
public abstract class SingleValueFormControlView<T> extends DefaultFormControlView<T, String> {

  private static final String UNCHANGED_RAW_VALUE = "unchanged";

  public SingleValueFormControlView(Class<T> type) {
    super(type);
  }

  @Override
  public String getRawValue() {
    if (getUpdateType() == UpdateType.POST && getContext().isPostback()) {
      return getContext().getPostParameters().getValue(getName(), getSequence());
    }

    if (getUpdateType() == UpdateType.GET && !getContext().isPostback()) {
      return getContext().getGetParameters().getValue(getName(), getSequence());
    }

    return UNCHANGED_RAW_VALUE;
  }

  @Override
  public T getValue() {
    Converters converters = ((Application) getContext().getApplication()).getConverters();
    String rawValue = getRawValue();

    if (rawValue != null && !hasChanged()) {
      return (T) ReflectiveUtil.getValue(getModel(), getExpression(), 0);
    }

    return (T) converters.parse(getRawValue(), getType(), getContext().getLocale());
  }

  public boolean hasChanged() {
    // NOTE: UNCHANGED_RAW_VALUE is used as a marker for not updating models so null or the empty
    //       string are accepted as valid inputs as well. Direct comparison between strings is the
    //       proper way of doing this, because else the input of the word "unchanged" would skip
    //       the update as well.
    return getRawValue() != UNCHANGED_RAW_VALUE;
  }

  @Override
  public SingleValueFormControlView<T> setHint(String hint) {
    super.setHint(hint);
    return this;
  }
}
