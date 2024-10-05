/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.util.Collections;
import java.util.List;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.util.ReflectiveUtil;

/**
 *
 * @author borowski
 */
public class Checkbox<T> extends SingleValueFormControlView<T> {

  private boolean isChecked;

  public Checkbox(Class<T> type) {
    super(type);
  }

  @Override
  public Checkbox bind(String expression) {
    super.bind(expression);
    return this;
  }

  @Override
  public Checkbox bind(Object object, String expression) {
    super.bind(object, expression);
    return this;
  }

  @Override
  public HtmlNode render() {

    String localId = getName() + "-" + getSequence();

    HtmlElement baseElement = new HtmlElement("div");

    if (!isValid()) {
      baseElement.addCssClass("has-error");
    }

    if (getLabel() != null) {
      baseElement.addChildNode(new HtmlElement("label")
              .setAttribute("for", localId)
              .setTextContent(t(getLabel())));
    }

    HtmlElement inputElement = new HtmlElement("input")
            .setAttribute("id", localId)
            .setAttribute("type", "checkbox")
            .setAttribute("name", getName());
    if (getType() == Boolean.class) {
      inputElement.setAttribute("value", "true");
    } else {
      inputElement.setAttribute("value", format(getValue()));
    }

    if (isChecked || (getType() == Boolean.class && (Boolean)getValue())) {
      inputElement.setAttribute("checked", "checked");
    }

    if (getLabel() == null && isValid()) {
      return applyDecorators(inputElement);
    } else {
      baseElement.addChildNode(inputElement);

      if (!isValid()) {
        for (String error : getErrors()) {
          baseElement.addChildNode(new HtmlElement("p")
                  .addCssClass("error-message")
                  .setTextContent(t(error)));
        }
      }
      return applyDecorators(baseElement);
    }
  }

  @Override
  public T getValue() {
    if (getType() == Boolean.class) {
      Boolean superValue = (Boolean) super.getValue();
      return  (superValue != null && superValue) ? (T) Boolean.TRUE : (T) Boolean.FALSE;
    }
    return (T) ReflectiveUtil.getValue(getModel(), getExpression(), 0);
  }

  @Override
  public void updateModel() {

    if (getType() == Boolean.class) {
      super.updateModel();
      isChecked = (Boolean) getValue();
    } else {

      List<String> values = null;

      if (getUpdateType() == UpdateType.POST && getContext().isPostback()) {
        values = getContext().getPostParameters().get(getName());
      }

      if (getUpdateType() == UpdateType.GET && !getContext().isPostback()) {
        values = getContext().getGetParameters().get(getName());
      }

      if (values == null) {
        values = Collections.EMPTY_LIST;
      }

      String currentValue = format(getValue());
      isChecked = values.contains(currentValue);
    }
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  @Override
  public Checkbox setLabel(String label) {
    super.setLabel(label);
    return this;
  }

  /**
   * @return the isChecked
   */
  public boolean isChecked() {
    return isChecked;
  }

  /**
   * @param isChecked the isChecked to set
   */
  public void setChecked(boolean isChecked) {
    this.isChecked = isChecked;
  }
  //</editor-fold>
}
