/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.util.LinkedList;
import java.util.List;
import rocks.xprs.exceptions.XprsException;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;

/**
 *
 * @author borowski
 */
public class GroupView extends View {

  protected final List<View> childViews = new LinkedList<>();

  public GroupView addView(View view) {
    addView(getContext().getId(view.getClass().getSimpleName()), view);
    return this;
  }

  public GroupView addView(String name, View view) {
    if (getName() != null) {
      name = getName() + "." + name;
    }
    view.setName(name);
    view.setParent(this);
    if (name != null) {
      GroupView rootView = this;
      View parent = this;
      while((parent = parent.getParent()) != null) {
        if (parent instanceof GroupView) {
          rootView = (GroupView) parent;
        }
      }

      view.setSequence(rootView.find(name).size());
    }
    view.init();
    childViews.add(view);
    view.initViews();
    return this;
  }

  public GroupView removeView(View view) {
    childViews.remove(view);
    return this;
  }

  public <T extends IView> List<T> find(Class<T> clazz) {
    LinkedList<T> result = new LinkedList<>();
    for (View v : childViews) {
      if (clazz.isAssignableFrom(v.getClass())) {
        result.add((T) v);
      }

      if (v instanceof GroupView) {
        result.addAll(((GroupView)v).find(clazz));
      }
    }

    return result;
  }

  public List<View> find(String name) {
    LinkedList<View> result = new LinkedList<>();
    for (View v : childViews) {
      if (v.getName().equals(name)) {
        result.add(v);
      }

      if (v instanceof GroupView) {
        result.addAll(((GroupView)v).find(name));
      }
    }

    return result;
  }

  public <T extends View> GroupView addDecorator(Decorator decorator) {
    super.addDecorator(decorator);
    return this;
  }

  public <T extends View> GroupView addDecorator(Decorator decorator, Class<T> clazz) {
    for (View v : find(clazz)) {
      v.addDecorator(decorator);
    }
    return this;
  }

  @Override
  public void loadModels() throws XprsException {
    for (View v : childViews) {
      v.loadModels();
    }
  }

  @Override
  public void initViews() {
//    for (View v : childViews) {
//      v.initViews();
//    }
  }

  @Override
  public void onGet() throws XprsException {
    for (View v : childViews) {
      v.onGet();
    }
  }

  @Override
  public void onPost() throws XprsException {
    for (View v : childViews) {
      v.onPost();
    }
  }

  @Override
  public HtmlNode render() {
    HtmlElement baseElement = new HtmlElement("div");
    for (View v : childViews) {
      if (v.isVisible()) {
        baseElement.addChildNode(v.render());
      }
    }
    return applyDecorators(baseElement);
  }
}
