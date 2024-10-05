package rocks.xprs.components;

import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.html.HtmlTextNode;

/**
 *
 * @author rborowski
 */
public class ButtonAction extends View {

  private enum Type {LINK, JS, BUTTON}

    private Type type;
    private Icon icon;
    private String label;
    private String name;
    private String action;
    private int position = 0;

    private ButtonAction() {

    }

    public static ButtonAction createLinkButton(String label, String href) {
      ButtonAction result = new ButtonAction();
      result.type = Type.LINK;
      result.label = label;
      result.action = href;
      return result;
    }

    public static ButtonAction createLinkButton(String label, String href, Icon icon) {
      ButtonAction result = ButtonAction.createLinkButton(label, href);
      result.icon = icon;
      return result;
    }

    public static ButtonAction createJsButton(String label, String onClick) {
      ButtonAction result = new ButtonAction();
      result.type = Type.JS;
      result.label = label;
      result.action = onClick;
      return result;
    }

    public static ButtonAction createJsButton(String label, String onClick, Icon icon) {
      ButtonAction result = ButtonAction.createJsButton(label, onClick);
      result.icon = icon;
      return result;
    }

    public static ButtonAction createButton(String label, String name, String value) {
      ButtonAction result = new ButtonAction();
      result.type = Type.BUTTON;
      result.label = label;
      result.name = name;
      result.action = value;
      return result;
    }

    public static ButtonAction createButton(String label, String name, String value, Icon icon) {
      ButtonAction result = ButtonAction.createButton(label, name, value);
      result.icon = icon;
      return result;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * @return the icon
     */
    public Icon getIcon() {
      return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(Icon icon) {
      this.icon = icon;
    }

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
     * @return the position
     */
    public int getPosition() {
      return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
      this.position = position;
    }
    //</editor-fold>

    @Override
    public HtmlNode render() {
      HtmlElement baseElement;
      switch (this.type) {
        case LINK:
          baseElement = new HtmlElement("a")
                  .setAttribute("href", action);
          break;
        case JS:
          baseElement = new HtmlElement("a")
                  .setAttribute("onclick", action);
          break;
        case BUTTON:
          baseElement = new HtmlElement("button")
                  .setAttribute("name", name)
                  .setAttribute("value", action);
          break;
        default:
          baseElement = new HtmlElement("a")
                  .setAttribute("href", action);
          break;
      }

      if (this.icon != null) {
        baseElement.addChildNode(new HtmlElement("i").addCssClass(this.icon.getName()));
      }

      baseElement.addChildNode(new HtmlTextNode(this.label));

      applyDecorators(baseElement);

      return baseElement;
    }

}
