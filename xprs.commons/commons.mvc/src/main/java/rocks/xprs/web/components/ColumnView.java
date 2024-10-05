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
public class ColumnView extends GroupView {

  @Override
  public HtmlNode render() {
    HtmlElement baseElement = new HtmlElement("div").addCssClass("columns");
    for (View v : childViews) {
      baseElement.addChildNode(
              new HtmlElement("div").addCssClass("column").addChildNode(v.render()));
    }
    return applyDecorators(baseElement);
  }

}
