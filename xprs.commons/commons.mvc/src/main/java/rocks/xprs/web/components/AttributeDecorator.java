package rocks.xprs.components;

import java.util.HashMap;
import java.util.Map;
import rocks.xprs.html.HtmlElement;

/**
 *
 * @author rborowski
 */
public class AttributeDecorator implements Decorator {

  private final Map<String, String> attributes = new HashMap<>();

  public AttributeDecorator() {

  }

  public AttributeDecorator set(String attribute, String value) {
    this.attributes.put(attribute, value);
    return this;
  }

  public AttributeDecorator set(String attribute) {
    this.attributes.put(attribute, attribute);
    return this;
  }

  @Override
  public HtmlElement decorate(HtmlElement element) {
    for (Map.Entry<String, String> a : this.attributes.entrySet()) {
      element.setAttribute(a.getKey(), a.getValue());
    }
    return element;
  }

}
