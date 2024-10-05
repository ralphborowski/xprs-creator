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
 * @author rborowski
 */
public class ListView extends GroupView {

  @Override
  public ListView addView(View view) {
    super.addView(view);
    return this;
  }

  @Override
  public ListView addView(String name, View view) {
    super.addView(name, view);
    return this;
  }

  @Override
  public ListView removeView(View view) {
    super.removeView(view);
    return this;
  }

  @Override
  public HtmlNode render() {
    HtmlElement baseElement = new HtmlElement("ul");
    for (View v : childViews) {
      baseElement.addChildNode(new HtmlElement("li").addChildNode(v.render()));
    }
    return applyDecorators(baseElement);
  }
}
