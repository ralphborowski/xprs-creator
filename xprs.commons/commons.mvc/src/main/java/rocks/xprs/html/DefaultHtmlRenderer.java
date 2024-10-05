package rocks.xprs.html;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ralph Borowski <ralph.borowski@borowski.it>
 */
public class DefaultHtmlRenderer extends HtmlRenderer {

  private static final List<String> EMPTY_TAGS = Arrays.asList("area", "base", "br", "col",
          "embed", "hr", "img", "input", "link", "meta", "param", "spurce", "track", "wbr");

  private static final List<String> EMPTY_ATTRIBUTES = Arrays.asList("allowfullscreen",
          "allowpaymentrequest", "autocomplete", "autofocus", "autoplay", "checked",
          "contenteditable", "controls", "default", "disabled", "draggable", "hidden",
          "loop", "muted", "readonly", "required", "selected", "spellcheck", "translate");

  private static final List<String> BLACKLIST_ATTRIBUTES = Arrays.asList("xprs:id");

  private final PrintWriter writer;

  public DefaultHtmlRenderer(OutputStream out) {
    this.writer = new PrintWriter(out, true, StandardCharsets.UTF_8);
  }

  public void render(HtmlNode node) throws IOException {
    if (node instanceof HtmlDocument) {
      renderHtmlDocument((HtmlDocument) node);
    } else if (node instanceof HtmlElement) {
      renderHtmlElement((HtmlElement) node);
    } else if (node instanceof HtmlTextNode) {
      renderHtmlTextNode((HtmlTextNode) node);
    } else if (node instanceof HtmlNodeList) {
      renderHtmlNodeList((HtmlNodeList) node);
    }
  }

  private void renderHtmlDocument(HtmlDocument document) throws IOException {
    if (document.getDoctype() != null) {
      writer.append(document.getDoctype()).append("\n");
    }
    renderHtmlElement(document);
  }

  private void renderHtmlElement(HtmlElement element) throws IOException {

    // don't render invisible elements
    if (!element.isVisible()) {
      return;
    }

    // open tag
    writer.append("<").append(element.getTagName());

    // render attributes
    for (Map.Entry<String, String> entry : element.getAttributes().entrySet()) {

      // ignore blacklisted attributes
      if (BLACKLIST_ATTRIBUTES.contains(entry.getKey())) {
        continue;
      }

      writer.append(" ").append(Html.escapeAttributeName(entry.getKey()));

      if (!EMPTY_ATTRIBUTES.contains(entry.getKey())) {
        writer.append("=\"").append(Html.escapeAttribute(entry.getValue())).append("\"");
      }
    }

    // close tag on empty elements
    if (EMPTY_TAGS.contains(element.getTagName())) {
      writer.append(" />");
    } else {

      // close tag if child elements are allowed
      writer.append(">");

      // render child elements (if any)
      for (HtmlNode e : element.getChildNodes()) {
        render(e);
      }

      // render close tag
      writer.append("</").append(element.getTagName()).append(">");
    }

    writer.flush();
  }

  private void renderHtmlTextNode(HtmlTextNode textNode) {
    writer.append(HtmlEncoder.encode(textNode.getText()));
  }

  private void renderHtmlNodeList(HtmlNodeList nodeList) throws IOException {
    for (HtmlNode n : nodeList.getNodes()) {
      render(n);
    }
  }

  @Override
  public void close() throws IOException {
    writer.flush();
    writer.close();
  }

}
