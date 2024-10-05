/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;

/**
 *
 * @author borowski
 */
public class Toolbar extends View {

  private final LinkedList<ButtonAction> actions = new LinkedList();
  private static final Comparator<ButtonAction> ACTION_ORDER = new Comparator<>() {

    @Override
    public int compare(ButtonAction o1, ButtonAction o2) {
      return Integer.compare(o1.getPosition(), o2.getPosition());
    }
  };

  public Toolbar addAction(ButtonAction action) {
    actions.add(action);
    return this;
  }

  public Toolbar addAction(ButtonAction action, int position) {
    action.setPosition(position);
    actions.add(action);
    return this;
  }

  public Toolbar removeAction(ButtonAction action) {
    actions.remove(action);
    return this;
  }

  public List<ButtonAction> getActions() {
    return actions;
  }

  @Override
  public Toolbar addDecorator(Decorator... decorator) {
    super.addDecorator(decorator);
    return this;
  }

  @Override
  public HtmlNode render() {
    if (actions.isEmpty()) {
      return HtmlNode.EMPTY_NODE;
    }

    // order actions by position
    actions.sort(ACTION_ORDER);

    // render actions
    HtmlElement baseElement = new HtmlElement("ul").addCssClass("toolbar");
    for (ButtonAction a : actions) {
      baseElement.addChildNode(new HtmlElement("li").addChildNode(a.render()));
    }
    return applyDecorators(baseElement);
  }
}
