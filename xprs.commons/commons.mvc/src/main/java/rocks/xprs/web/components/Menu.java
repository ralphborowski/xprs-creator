/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.util.LinkedList;
import java.util.List;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.web.UrlBuilder;

/**
 *
 * @author borowski
 */
public class Menu extends View {

  private final List<Link> menuItems = new LinkedList<>();

  public boolean isEmpty() {
    return menuItems.isEmpty();
  }

  @Override
  public HtmlNode render() {
    HtmlElement baseElement = new HtmlElement("nav");
    HtmlElement ul = new HtmlElement("ul").addCssClass("menu");

    HtmlElement selectedItem = null;

    for (Link l : menuItems) {
      HtmlElement li = new HtmlElement("li");
      String currentUrl = UrlBuilder.combinePaths(getContext().getRequest().getContextPath(), getContext().getRelativeUrl());
      if (l.getUrl() != null && currentUrl.startsWith(l.getUrl())) {
        selectedItem = li;
      }
      li.addChildNode(l.render());
      ul.addChildNode(li);
    }

    selectedItem.addCssClass("selected");
    
    baseElement.addChildNode(ul);
    return applyDecorators(baseElement);
  }

  public Menu addMenuItem(Link link) {
    menuItems.add(link);
    return this;
  }

}
