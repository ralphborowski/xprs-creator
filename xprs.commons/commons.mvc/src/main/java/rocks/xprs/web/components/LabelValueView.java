package rocks.xprs.components;

import java.util.LinkedList;
import java.util.List;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.html.HtmlNodeList;

/**
 *
 * @author Ralph Borowski <ralph.borowski@borowski.it>
 */
public class LabelValueView extends View {

  private List<Item> items = new LinkedList<>();

  public LabelValueView addItem(Item item) {
    items.add(item);
    return this;
  }

  public LabelValueView addItem(View label, View value) {
    items.add(new Item(label, value));
    return this;
  }

  public LabelValueView addItem(String label, String value) {
    items.add(new Item(label, value));
    return this;
  }

  public LabelValueView addItem(String label, Object object, String expresion) {
    items.add(new Item(label, object, expresion));
    return this;
  }

  @Override
  public HtmlNode render() {
    HtmlElement baseElement = new HtmlElement("dl");
    for (Item i : items) {
      baseElement.addChildNode(i.render());
    }

    return applyDecorators(baseElement);
  }

  public static class Item {

    private View label;
    private View value;

    public Item(View label, View value) {
      this.label = label;
      this.value = value;
    }

    public Item(String label, String value) {
      this.label = new TextView().setText(label);
      this.value = new TextView().setText(value);
    }

    public Item(String label, Object object, String expresion) {
      this.label = new TextView().setText(label);
      this.value = new TextView().bind(object, expresion);
    }

    public HtmlNodeList render() {
      HtmlElement dt = new HtmlElement("dt").addChildNode(label.render());
      HtmlElement dd = new HtmlElement("dd").addChildNode(value.render());
      return new HtmlNodeList().add(dt).add(dd);
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * @return the label
     */
    public View getLabel() {
      return label;
    }

    /**
     * @param label the label to set
     * @return self
     */
    public Item setLabel(View label) {
      this.label = label;
      return this;
    }

    /**
     * @return the value
     */
    public View getValue() {
      return value;
    }

    /**
     * @param value the value to set
     * @return self
     */
    public Item setValue(View value) {
      this.value = value;
      return this;
    }
    //</editor-fold>

  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the items
   */
  public List<Item> getItems() {
    return items;
  }

  /**
   * @param items the items to set
   * @return self
   */
  public LabelValueView setItems(List<Item> items) {
    this.items = items;
    return this;
  }
  //</editor-fold>

}
