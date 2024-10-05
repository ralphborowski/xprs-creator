package rocks.xprs.html;

import java.util.List;

/**
 *
 * @author Ralph Borowski
 */
public class HtmlDocument extends HtmlElement {

  private static final String TAG_TITLE = "title";
  private static final String TAG_HEAD = "head";
  private static final String TAG_BODY = "body";

  private String contentType = "text/html";
  private String doctype;

  public HtmlDocument() {
    super(new HtmlElement("html")
            .addChildNode(new HtmlElement("head")
                    .addChildNode(new HtmlElement("meta")
                            .setAttribute("charset", "utf-8"))
                    .addChildNode(new HtmlElement("title")))
            .addChildNode(new HtmlElement("body")));
  }

  public HtmlDocument(String tagName) {
    super(tagName);
  }

  public HtmlDocument(HtmlElement htmlElement) {
    super(htmlElement);
  }

  //<editor-fold defaultstate="collapsed" desc="Shortcuts to some frequent child elements">
  public String getTitle() {
    HtmlElement titleElement = getTitleElement();
    StringBuilder sb = new StringBuilder();
    if (titleElement != null) {
      for (HtmlNode n : titleElement.getChildNodes()) {
        if (n instanceof HtmlTextNode) {
          sb.append(((HtmlTextNode) n).getText());
        }
      }
    }
    return sb.toString();
  }

  public HtmlDocument setTitle(String title) {
    HtmlElement titleElement = getTitleElement();
    if (titleElement != null) {
      titleElement.clearChildNodes();
      HtmlTextNode titleTextNode = new HtmlTextNode(title);
      titleElement.addChildNode(titleTextNode);
    }
    return this;
  }

  private HtmlElement getTitleElement() {
    HtmlElement headElement = getHead();
    if (headElement != null) {
      List<HtmlElement> results = headElement.getChildElementsByTagName(TAG_TITLE);
      return (results.size() > 0) ? results.get(0) : null;
    }
    return null;
  }

  public HtmlElement getHead() {
    List<HtmlElement> results = getChildElementsByTagName(TAG_HEAD);
    return (results.size() > 0) ? results.get(0) : null;
  }

  public HtmlElement getBody() {
    List<HtmlElement> results = getChildElementsByTagName(TAG_BODY);
    return (results.size() > 0) ? results.get(0) : null;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Getters and setters">
  /**
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @param contentType the contentType to set
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * @return the doctype
   */
  public String getDoctype() {
    return doctype;
  }

  /**
   * @param doctype the doctype to set
   */
  public void setDoctype(String doctype) {
    this.doctype = doctype;
  }
  //</editor-fold>

}
