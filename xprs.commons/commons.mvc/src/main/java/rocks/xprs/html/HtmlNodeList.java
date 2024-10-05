package rocks.xprs.html;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ralph Borowski <ralph.borowski@borowski.it>
 */
public class HtmlNodeList extends HtmlNode {

  private final LinkedList<HtmlNode> childNodes = new LinkedList<>();

  public List<HtmlNode> getNodes() {
    return childNodes;
  }

  public boolean isEmpty() {
    return childNodes.isEmpty();
  }

  public HtmlNodeList add(HtmlNode node) {
    childNodes.add(node);
    return this;
  }

  public HtmlNodeList remove(HtmlNode node) {
    childNodes.remove(node);
    return this;
  }

}
