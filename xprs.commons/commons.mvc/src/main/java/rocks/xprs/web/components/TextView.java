/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.html.HtmlNodeList;
import rocks.xprs.html.HtmlTextNode;
import rocks.xprs.util.ReflectiveUtil;

/**
 *
 * @author borowski
 */
public class TextView extends View {

  private String text;

  @Override
  public HtmlNode render() {
    String displayString;
    if (text != null) {
      displayString = applyExpression(text);
    } else {
      displayString = format(ReflectiveUtil.getValue(getModel(), getExpression(), getSequence()));
    }

    HtmlNodeList list = new HtmlNodeList();
    if (displayString != null) {
      for (String s : displayString.split("\n")) {
        if (!list.isEmpty()) {
          list.add(new HtmlElement("br"));
        }
        list.add(new HtmlTextNode(s));
      }
    }
    return list;
  }

  @Override
  public TextView bind(String expression) {
    super.bind(expression);
    text = null;
    return this;
  }

  @Override
  public TextView bind(Object object, String expression) {
    super.bind(object, expression);
    text = null;
    return this;
  }

  @Override
  public TextView unbind() {
    super.unbind();
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
  public TextView setText(String text) {
    unbind();
    this.text = text;
    return this;
  }
}
