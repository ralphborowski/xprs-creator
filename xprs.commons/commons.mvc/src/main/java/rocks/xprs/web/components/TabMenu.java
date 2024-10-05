/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.util.List;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;

/**
 *
 * @author borowski
 */
public class TabMenu extends Menu {

  @Override
  public TabMenu addMenuItem(Link linkView) {
    super.addMenuItem(linkView);
    return this;
  }

  @Override
  public HtmlNode render() {
    HtmlElement baseElement = (HtmlElement) super.render();
    List<HtmlElement> uls = baseElement.getElementsByTagName("ul");
    if (!uls.isEmpty()) {
      uls.get(0).addCssClass("tabs");
      uls.get(0).removeCssClass("menu");
    }
    return baseElement;
  }

}
