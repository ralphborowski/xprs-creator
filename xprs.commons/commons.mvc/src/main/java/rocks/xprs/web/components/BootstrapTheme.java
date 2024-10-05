package rocks.xprs.components;

import java.util.List;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;

/**
 *
 * @author Ralph Borowski <ralph.borowski@borowski.it>
 */
public class BootstrapTheme implements Theme {

  @Override
  public void apply(PageView page) {

    page.addStylesheet("bootstrap4",
            new PageView.Stylesheet().setHref("assets/css/bootstrap.min.css"));
    page.addScript("bootstrap4",
            new PageView.Script().setSrc("assets/js/bootstrap.bundle.js"));

    // NAVIGATION
    page.addDecorator(MENU_DECORATOR, Menu.class);
    page.addDecorator(TABS_DECORATOR, TabMenu.class);
    page.addDecorator(TOOLBAR_DECORATOR, Toolbar.class);

    // TABLE
    page.addDecorator(TABLE_DECORATOR, Table.class);

    // FORMS
    page.addDecorator(BUTTON_DECORATOR, ButtonView.class);
    page.addDecorator(TEXTAREA_DECORATOR, Textarea.class);
    page.addDecorator(INPUT_DECORATOR, TextInput.class);
    page.addDecorator(SELECT_DECORATOR, Select.class);
  }

  /*

     NAVIGATION

  */
  public static final Decorator MENU_DECORATOR = new Decorator() {

    @Override
    public HtmlElement decorate(HtmlElement element) {
      List<HtmlElement> ulTags = element.getChildElementsByTagName("ul");
      if (!ulTags.isEmpty()) {
        ulTags.get(0).addCssClass("nav");

        for (HtmlElement e : ulTags.get(0).getElementsByTagName("li")) {
          e.addCssClass("nav-item");
          List<HtmlElement> links = e.getElementsByTagName("a");
          if (!links.isEmpty()) {
            links.get(0).addCssClass("nav-link");
            if (e.hasCssClass("selected")) {
              links.get(0).addCssClass("active");
              e.removeCssClass("selected");
            }
          }
        }
      }

      return element;
    }
  };

  public static final Decorator TABS_DECORATOR = new Decorator() {

    @Override
    public HtmlElement decorate(HtmlElement element) {
      element = MENU_DECORATOR.decorate(element);
      List<HtmlElement> ulTags = element.getChildElementsByTagName("ul");
      if (!ulTags.isEmpty()) {
        ulTags.get(0).addCssClass("nav-tabs");
      }
      return element;
    }
  };

  public static final Decorator TOOLBAR_DECORATOR = new Decorator() {

    @Override
    public HtmlElement decorate(HtmlElement element) {
      HtmlElement btnToolbar = new HtmlElement("div")
              .addCssClass("btn-group")
              .setAttribute("role", "toolbar");

      for (HtmlElement li : element.getElementsByTagName("li")) {
        for (HtmlNode childNode : li.getChildNodes()) {
          if (childNode instanceof HtmlElement) {
            HtmlElement button = (HtmlElement) childNode;
            if (button.getTagName().equals("a") || button.getTagName().equals("button")) {
              button.addCssClass("btn btn-outline-secondary");
            }
            btnToolbar.addChildNode(childNode);
          }
        }
      }

      return btnToolbar;
    }
  };

  /*

     TABLE

  */
  public static final Decorator TABLE_DECORATOR = new Decorator() {

    @Override
    public HtmlElement decorate(HtmlElement element) {
      List<HtmlElement> tables = element.getChildElementsByTagName("table");

      for (HtmlElement table : tables) {
        table.addCssClass("table table-striped");
        List<HtmlElement> theads = element.getChildElementsByTagName("thead");
        for (HtmlElement thead : theads) {
          thead.addCssClass("thead-light");
        }
      }

      return element;
    }
  };

  /*

     FORM

  */
  public static final Decorator BUTTON_DECORATOR = new Decorator() {

    @Override
    public HtmlElement decorate(HtmlElement element) {
      element.addCssClass("btn");
      if (element.hasAttribute("type") && element.getAttribute("type").equals("submit")) {
        element.addCssClass("btn-primary");
      } else {
        element.addCssClass("btn-secondary");
      }
      return element;
    }
  };

  public static final Decorator INPUT_DECORATOR = new Decorator() {

    @Override
    public HtmlElement decorate(HtmlElement element) {
      element.addCssClass("form-group");
      for (HtmlElement e : element.getChildElementsByTagName("input")) {
        String type = e.getAttribute("type");
        if (type == null) {
          type = "text";
        }

        if (type.equals("file")) {
          e.addCssClass("form-control-file");
        } else if (type.equals("range")) {
          e.addCssClass("form-control-range");
        } else if (!(type.equals("radio") || type.equals("checkbox"))) {
          e.addCssClass("form-control");
        }
      }

      return element;
    }
  };

  public static final Decorator SELECT_DECORATOR = new Decorator() {

    @Override
    public HtmlElement decorate(HtmlElement element) {
      element.addCssClass("form-group");

      for (HtmlElement e : element.getChildElementsByTagName("select")) {
        e.addCssClass("form-control");
      }

      return element;
    }
  };

  public static final Decorator TEXTAREA_DECORATOR = new Decorator() {

    @Override
    public HtmlElement decorate(HtmlElement element) {
      element.addCssClass("form-group");

      for (HtmlElement e : element.getChildElementsByTagName("textarea")) {
        e.addCssClass("form-control");
      }

      return element;
    }
  };

}
