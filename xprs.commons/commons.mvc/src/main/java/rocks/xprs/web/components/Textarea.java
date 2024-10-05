/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;

/**
 *
 * @author borowski
 */
public class Textarea extends SingleValueFormControlView<String> {

  public Textarea() {
    super(String.class);
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

    HtmlElement textarea = new HtmlElement("textarea")
            .setAttribute("id", localId)
            .setAttribute("name", getName())
            .setTextContent(format(getValue()));

    if (getHint() != null || !isValid()) {

      HtmlElement wrapper = new HtmlElement("div");
      baseElement.addChildNode(wrapper);
      wrapper.addChildNode(textarea);

      if (getHint() != null) {
        wrapper.addChildNode(new HtmlElement("p")
                .addCssClass("hint")
                .setTextContent(getHint()));
      }

      if (!isValid()) {
        for (String error : getErrors()) {
          wrapper.addChildNode(new HtmlElement("p")
                  .addCssClass("error-message")
                  .setTextContent(t(error)));
        }
      }
    } else {
      baseElement.addChildNode(textarea);
    }

    return applyDecorators(baseElement);
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  @Override
  public Textarea setLabel(String label) {
    super.setLabel(label);
    return this;
  }

  @Override
  public Textarea setHint(String hint) {
    super.setHint(hint);
    return this;
  }
  //</editor-fold>

}
