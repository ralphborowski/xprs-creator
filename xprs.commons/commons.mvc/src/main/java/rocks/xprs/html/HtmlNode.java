package rocks.xprs.html;

/**
 *
 * @author Ralph Borowski
 */
public abstract class HtmlNode implements Cloneable {

  public static HtmlNode EMPTY_NODE = new HtmlTextNode("");

  private HtmlElement parentNode;

  /**
   * @return the parentNode
   */
  public HtmlElement getParentNode() {
    return parentNode;
  }

  /**
   * @param parentNode the parentNode to set
   */
  public void setParentNode(HtmlElement parentNode) {
    this.parentNode = parentNode;
  }
}
