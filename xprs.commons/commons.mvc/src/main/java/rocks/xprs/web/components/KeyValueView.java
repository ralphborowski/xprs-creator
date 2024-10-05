package rocks.xprs.components;

import java.util.LinkedList;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.html.HtmlTextNode;

/**
 *
 * @author Ralph Borowski <ralph.borowski@borowski.it>
 */
public class KeyValueView extends View {

  private LinkedList<KeyValueEntry> entries = new LinkedList<>();

  public KeyValueView addEntry(String label, View value) {
    entries.add(new KeyValueEntry(label, value));
    return this;
  }

  public KeyValueView addEntry(String label, String value) {
    return addEntry(label, new TextView().setText(value));
  }

  @Override
  public HtmlNode render() {
    HtmlElement baseElement = new HtmlElement("dl");
    for (KeyValueEntry e : entries) {
      baseElement
              .addChildNode(
                      new HtmlElement("dt").addChildNode(new HtmlTextNode(t(e.getLabel())))
              )
              .addChildNode(
                      new HtmlElement("dd").addChildNode(e.getValue().render())
              );
    }
    return baseElement;
  }

  public static class KeyValueEntry {

    private String label;
    private View value;

    public KeyValueEntry(String label, View value) {
      this.label = label;
      this.value = value;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * @return the label
     */
    public String getLabel() {
      return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
      this.label = label;
    }

    /**
     * @return the value
     */
    public View getValue() {
      return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(View value) {
      this.value = value;
    }
    //</editor-fold>
  }

}
