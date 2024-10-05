package rocks.xprs.html;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents a HTML element.
 *
 * @author Ralph Borowski
 */
public class HtmlElement extends HtmlNode {

  private static final String ATTR_ID = "id";
  private static final String ATTR_XPRS_ID = "xprs:id";
  private static final String ATTR_CSS_CLASS = "class";

  private String tagName;
  private HtmlElement documentRoot;
  private boolean visible = true;
  private final Map<String, String> attributes = new HashMap<>();
  private final LinkedList<HtmlNode> childNodes = new LinkedList<>();

  public HtmlElement() {

  }

  public HtmlElement(String tagName) {
    this.tagName = tagName;
  }

  public HtmlElement(HtmlElement element) {

    // set tag name
    this.tagName = element.getTagName();

    // copy attributes
    this.attributes.putAll(element.getAttributes());

    // copy child nodes
    for (HtmlNode n : element.childNodes) {
      if (n instanceof HtmlElement) {
        this.childNodes.add(new HtmlElement((HtmlElement) n));
      } else if (n instanceof HtmlTextNode) {
        this.childNodes.add(new HtmlTextNode(((HtmlTextNode) n).getText()));
      }
    }
  }

  //<editor-fold defaultstate="collapsed" desc="Generic getters and setters">
  public String getId() {
    return getAttribute(ATTR_ID);
  }

  public HtmlElement setId(String id) {
    setAttribute(ATTR_ID, id);
    return this;
  }

  public String getXprsId() {
    return getAttribute(ATTR_XPRS_ID);
  }

  public String getTagName() {
    return tagName;
  }

  public HtmlElement setTagName(String tagName) {
    this.tagName = tagName;
    return this;
  }

  /**
   * @return the documentRoot
   */
  public HtmlElement getDocumentRoot() {
    return documentRoot;
  }

  /**
   * @param documentRoot the documentRoot to set
   */
  public void setDocumentRoot(HtmlElement documentRoot) {
    this.documentRoot = documentRoot;
  }

  /**
   * @return the visible
   */
  public boolean isVisible() {
    return visible;
  }

  /**
   * @param visible the visible to set
   */
  public void setVisible(boolean visible) {
    this.visible = visible;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Managing attributes">
  public boolean hasAttribute(String name) {
    return attributes.containsKey(name);
  }

  public String getAttribute(String name) {
    return attributes.get(name);
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  public HtmlElement setAttribute(String name, String value) {
    attributes.put(name, value);
    return this;
  }

  public HtmlElement removeAttribute(String name) {
    attributes.remove(name);
    return this;
  }

  public boolean hasCssClass(String cssClass) {
    for (String s : getCssClasses()) {
      if (s.equals(cssClass)) {
        return true;
      }
    }
    return false;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="CSS classes">
  public String[] getCssClasses() {
    if (!hasAttribute(ATTR_CSS_CLASS)) {
      return new String[0];
    }
    return normalizeCssClasses(getAttribute(ATTR_CSS_CLASS)).split(" ");
  }

  public HtmlElement addCssClass(String cssClass) {

    // set as first class
    if (!hasAttribute(ATTR_CSS_CLASS)) {
      setAttribute(ATTR_CSS_CLASS, normalizeCssClasses(cssClass));
    }

    // avoid duplicates
    for (String c : getCssClasses()) {
      if (c.equals(cssClass)) {
        return this;
      }
    }

    // add new class to the end
    setAttribute(ATTR_CSS_CLASS,
            normalizeCssClasses(getAttribute(ATTR_CSS_CLASS) + " " + cssClass));
    return this;
  }

  public HtmlElement removeCssClass(String cssClass) {

    if (hasAttribute(ATTR_CSS_CLASS)) {
      String[] cssClasses = getCssClasses();
      String newCssClasses = "";
      for (String s : cssClasses) {
        if (!s.equals(cssClass)) {
          newCssClasses += " " + s;
        }
      }

      newCssClasses = normalizeCssClasses(newCssClasses);

      if (newCssClasses.isEmpty()) {
        removeAttribute(ATTR_CSS_CLASS);
      } else {
        setAttribute(ATTR_CSS_CLASS, newCssClasses);
      }
    }

    return this;
  }

  public HtmlElement removeAllCssClasses() {
    removeAttribute(ATTR_CSS_CLASS);
    return this;
  }

  public HtmlElement toggleCssClass(String cssClass) {
    if (hasCssClass(cssClass)) {
      removeCssClass(cssClass);
    } else {
      addCssClass(cssClass);
    }
    return this;
  }

  private String normalizeCssClasses(String cssClasses) {
    return cssClasses.replace("[ ]+", " ").trim();
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Managing child elements">
  public boolean hasChildNodes() {
    return this.childNodes.isEmpty();
  }

  public List<HtmlNode> getChildNodes() {
    return childNodes;
  }

  public HtmlElement addChildNode(HtmlNodeList node) {
    return addChildNodes(node.getNodes());
  }

  public HtmlElement addChildNode(HtmlNode node) {
    if (node == null) {
      return this;
    }

    node.setParentNode(this);
    getChildNodes().add(node);
    return this;
  }

  public HtmlElement addChildNodes(List<HtmlNode> nodes) {
    for (HtmlNode n : nodes) {
      addChildNode(n);
    }
    return this;
  }

  public HtmlElement addChildNodeAfter(HtmlNode node, HtmlNode predecessor) {

    if (node == null || predecessor == null) {
      return this;
    }

    int index = getChildNodes().indexOf(predecessor);
    if (index > -1) {
      node.setParentNode(this);
      getChildNodes().add(index + 1, node);
    }

    return this;
  }

  public HtmlElement addChildNodeBefore(HtmlNode node, HtmlNode successor) {

    if (node == null) {
      return this;
    }

    int index = getChildNodes().indexOf(successor);
    if (index > -1) {
      node.setParentNode(this);
      getChildNodes().add(index, node);
    }

    return this;
  }

  public HtmlElement replaceChildNode(HtmlNode target, HtmlNode replacement) {
    return this.replaceChildNode(target, Arrays.asList(replacement));
  }

  public HtmlElement replaceChildNode(HtmlNode target, List<HtmlNode> replacement) {
    if (target == null) {
      return this;
    }

    HtmlNode current = target;
    for (HtmlNode r : replacement) {
      addChildNodeAfter(r, current);
      current = r;
    }
    removeChildNode(target);

    return this;
  }

  public HtmlElement removeChildNode(HtmlNode node) {
    if (node == null) {
      return this;
    }

    node.setParentNode(null);
    getChildNodes().remove(node);
    return this;
  }

  public HtmlElement clearChildNodes() {
    Iterator<HtmlNode> it = getChildNodes().iterator();
    while (it.hasNext()) {
      removeChildNode(it.next());
    }
    return this;
  }

  public String getTextContent() {
    StringBuilder sb = new StringBuilder();
    for (HtmlNode n : getChildNodes()) {
      if (n instanceof HtmlTextNode) {
        sb.append(((HtmlTextNode)n).getText());
      }
    }
    return sb.toString();
  }

  public HtmlElement setTextContent(String content) {
    clearChildNodes();
    addChildNode(new HtmlTextNode(content));
    return this;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Selectors">
  public List<HtmlElement> getElementsByTagName(String tagName) {
    List<HtmlElement> result = new LinkedList<>();

    // get local findings
    result.addAll(getChildElementsByTagName(tagName));

    // get recursive results
    for (HtmlNode n : getChildNodes()) {
      if (n instanceof HtmlElement) {
        result.addAll(((HtmlElement) n).getElementsByTagName(tagName));
      }
    }

    return result;
  }

  public List<HtmlElement> getChildElementsByTagName(String tagName) {
    List<HtmlElement> result = new LinkedList<>();
    for (HtmlNode n : getChildNodes()) {
      if (n instanceof HtmlElement && ((HtmlElement) n).getTagName().equalsIgnoreCase(tagName)) {
        result.add((HtmlElement) n);
      }
    }
    return result;
  }

  public HtmlElement getElementById(String id) {

    if (id.equals(getId())) {
      return this;
    }

    HtmlElement result;
    for (HtmlNode n : getChildNodes()) {
      if (n instanceof HtmlElement) {
        result = ((HtmlElement) n).getElementById(id);
        if (result != null) {
          return result;
        }
      }
    }

    return null;
  }

  public HtmlElement getElementByXprsId(String xprsId) {

    if (xprsId.equals(getXprsId())) {
      return this;
    }

    HtmlElement result;
    for (HtmlNode n : getChildNodes()) {
      if (n instanceof HtmlElement) {
        result = ((HtmlElement) n).getElementByXprsId(xprsId);
        if (result != null) {
          return result;
        }
      }
    }

    return null;
  }
  //</editor-fold>
}
