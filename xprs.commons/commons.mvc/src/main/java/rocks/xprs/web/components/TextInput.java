/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.util.Locale;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.util.Strings;

/**
 *
 * @author borowski
 */
public class TextInput<T> extends SingleValueFormControlView<T> {

  public enum InputType {TEXT, PASSWORD, HIDDEN}

  private InputType inputType = InputType.TEXT;

  public TextInput(Class<T> type) {
    super(type);
  }

  public TextInput setInputType(InputType type) {
    this.inputType = type;
    return this;
  }

  @Override
  public TextInput bind(String expression) {
    super.bind(expression);
    return this;
  }

  @Override
  public TextInput bind(Object object, String expression) {
    super.bind(object, expression);
    return this;
  }

  @Override
  public TextInput unbind() {
    super.unbind();
    return this;
  }

  @Override
  public HtmlNode render() {

    String localId = getName() + "-" + getSequence();

    HtmlElement input = new HtmlElement("input")
            .setAttribute("id", localId)
            .setAttribute("type", inputType.name().toLowerCase(Locale.ENGLISH))
            .setAttribute("name", getName())
            .setAttribute("value", format(getValue()));

    if (inputType == InputType.HIDDEN || Strings.isEmpty(getLabel())) {
      return input;
    }

    HtmlElement baseElement = new HtmlElement("div")
            .addCssClass("form-control-group");

    if (!isValid()) {
      baseElement.addCssClass("has-error");
    }

    if (getLabel() != null) {
      baseElement.addChildNode(new HtmlElement("label")
              .setAttribute("for", localId)
              .setTextContent(t(getLabel())));
    }

    baseElement.addChildNode(input);

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

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  @Override
  public TextInput<T> setLabel(String label) {
    super.setLabel(label);
    return this;
  }

  @Override
  public TextInput<T> setHint(String hint) {
    super.setHint(hint);
    return this;
  }
  //</editor-fold>

}
