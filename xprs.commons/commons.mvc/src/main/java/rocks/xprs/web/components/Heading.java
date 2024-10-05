/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.html.HtmlTextNode;
import rocks.xprs.util.ReflectiveUtil;

/**
 *
 * @author borowski
 */
public class Heading extends View {

  private String text;
  private String level = "1";

  @Override
  public HtmlNode render() {
    HtmlElement baseElement = new HtmlElement("h" + level);
    if (text != null) {
      baseElement.addChildNode(new HtmlTextNode(applyExpression(text)));
    } else {
      baseElement.addChildNode(new HtmlTextNode(
              format(ReflectiveUtil.getValue(getModel(), getExpression(), 0))));
    }
    return applyDecorators(baseElement);
  }

  @Override
  public Heading bind(Object object, String expression) {
    this.text = null;
    super.bind(object, expression);
    return this;
  }

  @Override
  public Heading bind(String expression) {
    this.text = null;
    super.bind(expression);
    return this;
  }

  /**
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * @param text the text to set
   * @return the current object
   */
  public Heading setText(String text) {
    this.bind(null);
    this.text = text;
    return this;
  }

  /**
   * @return the level
   */
  public String getLevel() {
    return level;
  }

  /**
   * @param level the level to set
   * @return the current object
   */
  public Heading setLevel(int level) {
    if (level > 5) {
      level = 5;
    }
    this.level = String.valueOf(level);
    return this;
  }

}
