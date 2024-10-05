package rocks.xprs.components;

import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;

/**
 *
 * @author Ralph Borowski <ralph.borowski@borowski.it>
 */
public class TableRow extends GroupView {

  @Override
  public TableRow addView(View view) {
    super.addView(view);
    return this;
  }

  @Override
  public TableRow addView(String name, View view) {
    super.addView(name, view);
    return this;
  }

  @Override
  public TableRow removeView(View view) {
    super.removeView(view);
    return this;
  }

  @Override
  public HtmlNode render() {
    HtmlElement baseElement = new HtmlElement("tr");
    for (View v : childViews) {
      baseElement.addChildNode(v.render());
    }
    return applyDecorators(baseElement);
  }

}
