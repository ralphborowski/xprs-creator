package rocks.xprs.components;

import java.util.HashSet;
import java.util.Set;
import rocks.xprs.html.HtmlElement;

/**
 *
 * @author rborowski
 */
public class CssClassDecorator implements Decorator {

  private final Set<String> cssClasses = new HashSet<>();
  private final Set<String> removeCssClasses = new HashSet<>();

  public CssClassDecorator() {

  }

  public CssClassDecorator add(String cssClass) {
    cssClasses.add(cssClass);
    return this;
  }

  public CssClassDecorator remove(String cssClass) {
    if (cssClasses.contains(cssClass)) {
      cssClasses.remove(cssClass);
    } else {
      removeCssClasses.add(cssClass);
    }
    return this;
  }

  public CssClassDecorator toggle(String cssClass) {
    if (cssClasses.contains(cssClass)) {
      cssClasses.remove(cssClass);
    } else {
      cssClasses.add(cssClass);
    }
    return this;
  }

  @Override
  public HtmlElement decorate(HtmlElement element) {
    for (String c : cssClasses) {
      element.addCssClass(c);
    }

    for (String c : removeCssClasses) {
      element.removeCssClass(c);
    }
    return element;
  }
}
