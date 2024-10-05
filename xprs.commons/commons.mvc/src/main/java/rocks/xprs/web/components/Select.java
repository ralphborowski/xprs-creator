/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.util.Collection;
import java.util.Objects;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.util.ReflectiveUtil;

/**
 *
 * @author borowski
 */
public class Select<T> extends SingleValueFormControlView<T> {

  private OptionRenderer<T> optionRenderer;
  private boolean isRenderEmpty;

  public Select(Class<T> type) {
    super(type);
  }

  @Override
  public Select bind(String expression) {
    super.bind(expression);
    return this;
  }

  @Override
  public Select bind(Object object, String expression) {
    super.bind(object, expression);
    return this;
  }

  @Override
  public Select unbind() {
    super.unbind();
    return this;
  }

  @Override
  public HtmlNode render() {

    String localId = getName() + "-" + getSequence();

    HtmlElement baseElement = new HtmlElement("div").addCssClass("form-control-group");

    if (!isValid()) {
      baseElement.addCssClass("has-error");
    }

    if (getLabel() != null) {
      baseElement.addChildNode(new HtmlElement("label")
              .setAttribute("for", localId)
              .setTextContent(t(getLabel())));
    }

    HtmlElement select = new HtmlElement("select")
            .setAttribute("id", localId)
            .setAttribute("name", getName());

    if (isRenderEmpty) {
       select.addChildNode(new HtmlElement("option"));
    }

    if (optionRenderer.getOptions() != null && !optionRenderer.getOptions().isEmpty()) {
      T value = getValue();
      for (T e : optionRenderer.getOptions()) {
        String itemId = String.valueOf(optionRenderer.getId(e));
        HtmlElement option = new HtmlElement("option")
                .setAttribute("value", itemId)
                .setTextContent(optionRenderer.getLabel(e));

        if (Objects.equals(e, value)) {
          option.setAttribute("selected", "selected");
        }
        select.addChildNode(option);
      }
    }

    baseElement.addChildNode(select);

    if (getHint() != null) {
      baseElement.addChildNode(new HtmlElement("p")
                .addCssClass("hint")
                .setTextContent(getHint()));
    }

    if (!isValid()) {
      for (String error : getErrors()) {
        baseElement.addChildNode(new HtmlElement("p")
                .addCssClass("error-message")
                .setTextContent(t(error)));
      }
    }
    return applyDecorators(baseElement);
  }

  @Override
  public Select setLabel(String label) {
    super.setLabel(label);
    return this;
  }

  @Override
  public T getValue() {

    if (optionRenderer == null) {
      return null;
    }

    if (getUpdateType() == UpdateType.POST && getContext().isPostback()) {
      String rawValue = getContext().getPostParameters().getValue(getName(), getSequence());
      return optionRenderer.getValue(rawValue);
    }

    if (getUpdateType() == UpdateType.GET && !getContext().isPostback()) {
      String rawValue = getContext().getGetParameters().getValue(getName(), getSequence());
      return optionRenderer.getValue(rawValue);
    }

    return (T) ReflectiveUtil.getValue(getModel(), getExpression(), 0);
  }

  public static interface OptionRenderer<T> {

    public String getId(T object);
    public String getLabel(T object);
    public T getValue(String id);
    public Collection<T> getOptions();

  }

  public boolean isRenderEmpty() {
    return isRenderEmpty;
  }

  public Select setRenderEmpty(boolean renderEmpty) {
    this.isRenderEmpty = renderEmpty;
    return this;
  }

  public Select setOptionRenderer(OptionRenderer<T> optionRenderer) {
    this.optionRenderer = optionRenderer;
    return this;
  }

  @Override
  public Select<T> setHint(String hint) {
    super.setHint(hint);
    return this;
  }
}
