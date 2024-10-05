/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.math.BigDecimal;
import java.util.Map;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.types.Monetary;

/**
 *
 * @author rborowski
 */
public class MonetaryInputView extends MultiValueFormControlView<Monetary> {

  private boolean showCurrencyInput = true;

  public MonetaryInputView() {
    super(Monetary.class, "amount", "currency");
  }

  @Override
  protected Monetary parse(Map<String, String> inputValues) {

    BigDecimal amount = getContext().parse(inputValues.get("amount"), BigDecimal.class);

    if (amount != null) {
      return new Monetary(amount, inputValues.get("currency"));
    } else {
      return null;
    }
  }

  @Override
  public HtmlNode render() {

    HtmlElement baseElement = new HtmlElement("div").addCssClass("form-control-group");

    if (!isValid()) {
      baseElement.addCssClass("has-error");
    }

    if (getLabel() != null) {
      baseElement.addChildNode(new HtmlElement("label")
              .setAttribute("for", getIdForPart("amount"))
              .setTextContent(t(getLabel())));
    }

    HtmlElement inputGroup = new HtmlElement("div")
            .addCssClass("form-control-input-group")
            .addChildNode(renderInput("amount"));

    if (isShowCurrencyInput()) {
      inputGroup.addChildNode(renderInput("currency"));
    }

    baseElement.addChildNode(inputGroup);

    if (!isValid()) {
      for (String error : getErrors()) {
        baseElement.addChildNode(new HtmlElement("p")
                .addCssClass("error-message")
                .setTextContent(t(error)));
      }
    }

    return applyDecorators(baseElement);
  }

  private HtmlElement renderInput(String partname) {
    Monetary value = getValue();
    String formattedValue = "";
    if (value != null) {
      switch (partname) {
        case "amount": formattedValue = format(value.getAmount()); break;
        case "currency": formattedValue = format(value.getCurrency()); break;
      }
    }

    return new HtmlElement("input")
                    .setAttribute("id", getIdForPart(partname))
                    .setAttribute("type", "text")
                    .setAttribute("placeholder", t("monetary.placeholders." + partname))
                    .setAttribute("name", getNameForPart(partname))
                    .setAttribute("value", formattedValue);
  }

  /**
   * @return the showCurrencyInput
   */
  public boolean isShowCurrencyInput() {
    return showCurrencyInput;
  }

  /**
   * @param showCurrencyInput the showCurrencyInput to set
   * @return this object
   */
  public MonetaryInputView setShowCurrencyInput(boolean showCurrencyInput) {
    this.showCurrencyInput = showCurrencyInput;
    return this;
  }

  @Override
  public MonetaryInputView setLabel(String text) {
    super.setLabel(text);
    return this;
  }

  @Override
  public MonetaryInputView bind(String expression) {
    super.bind(expression);
    return this;
  }

  @Override
  public MonetaryInputView bind(Object object, String expression) {
    super.bind(object, expression);
    return this;
  }

  @Override
  public MonetaryInputView unbind() {
    super.unbind();
    return this;
  }
}
