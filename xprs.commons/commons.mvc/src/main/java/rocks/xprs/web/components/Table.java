package rocks.xprs.components;

import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;

/**
 *
 * @author Ralph Borowski <ralph.borowski@borowski.it>
 */
public class Table extends GroupView {

  @Override
  public Table addView(View view) {
    super.addView(view);
    return this;
  }

  @Override
  public Table addView(String name, View view) {
    super.addView(name, view);
    return this;
  }

  @Override
  public Table removeView(View view) {
    super.removeView(view);
    return this;
  }

  @Override
  public HtmlNode render() {
    HtmlElement baseElement = new HtmlElement("table");
    for (View v : childViews) {
      baseElement.addChildNode(v.render());
    }
    return applyDecorators(baseElement);
  }

}
