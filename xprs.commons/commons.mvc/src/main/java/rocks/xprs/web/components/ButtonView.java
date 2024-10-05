/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.util.Locale;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;

/**
 *
 * @author borowski
 */
public class ButtonView extends View {

  public static enum Type {SUBMIT, BUTTON}

  private String label;
  private Type type = Type.SUBMIT;
  private String onClick;

  @Override
  public HtmlNode render() {
    HtmlElement element = new HtmlElement("button")
            .setAttribute("type", type.toString().toLowerCase(Locale.ENGLISH))
            .setTextContent(getLabel());
    if (onClick != null) {
      element.setAttribute("onclick", onClick);
    }

    return applyDecorators(element);
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @param label the label to set
   * @return current button
   */
  public ButtonView setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * @return the type
   */
  public Type getType() {
    return type;
  }

  /**
   * @param type the type to set
   * @return current button
   */
  public ButtonView setType(Type type) {
    this.type = type;
    return this;
  }

  /**
   * @return the onClick
   */
  public String getOnClick() {
    return onClick;
  }

  /**
   * @param onClick the onClick to set
   */
  public ButtonView setOnClick(String onClick) {
    this.onClick = onClick;
    return this;
  }
  //</editor-fold>
}
