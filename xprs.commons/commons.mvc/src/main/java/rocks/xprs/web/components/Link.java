/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.util.ReflectiveUtil;

/**
 *
 * @author borowski
 */
public class Link extends View {

  private String label;
  private String url;
  private String target;

  @Override
  public HtmlNode render() {
    String displayString;
    if (label != null) {
      displayString = applyExpression(label);
    } else {
      displayString = format(ReflectiveUtil.getValue(getModel(), getExpression(), getSequence()));
    }

    HtmlElement baseElement = new HtmlElement("a")
            .setAttribute("href", url)
            .setTextContent(displayString);
    if (target != null) {
      baseElement.setAttribute("target", target);
    }
    return applyDecorators(baseElement);
  }

  @Override
  public Link bind(String expression) {
    super.bind(expression);
    label = null;
    return this;
  }

  @Override
  public Link bind(Object object, String expression) {
    super.bind(object, expression);
    label = null;
    return this;
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
   * @return the current object
   */
  public Link setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url the url to set
   * @return the current object
   */
  public Link setUrl(String url) {
    this.url = url;
    return this;
  }

  /**
   * @return the target
   */
  public String getTarget() {
    return target;
  }

  /**
   * @param target the target to set
   * @return the current object
   */
  public Link setTarget(String target) {
    this.target = target;
    return this;
  }
  //</editor-fold>
}
