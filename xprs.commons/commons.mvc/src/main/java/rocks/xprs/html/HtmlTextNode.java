package rocks.xprs.html;

/**
 *
 * @author Ralph Borowski
 */
public class HtmlTextNode extends HtmlNode {

  private String text;

  public HtmlTextNode() {

  }

  public HtmlTextNode(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}