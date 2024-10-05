package rocks.xprs.components;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;

/**
 *
 * @author rborowski
 */
public class FileFormControl extends DefaultFormControlView<Part, Part> {

  public FileFormControl() {
    super(Part.class);
  }

  @Override
  public Part getRawValue() {
    if (getUpdateType() == UpdateType.POST && getContext().isPostback()) {
      try {
        return getContext().getRequest().getPart(getName());
      } catch (IOException | ServletException ex) {
        Logger.getLogger(FileFormControl.class.getName()).log(
                Level.SEVERE,
                String.format("Error getting part from request for %s.", getName()),
                ex);
      }
    }

    return null;
  }

  @Override
  public Part getValue() {
    return getRawValue();
  }

  @Override
  public HtmlNode render() {
    String localId = getName() + "-" + getSequence();

    HtmlElement baseElement = new HtmlElement("div").addCssClass("form-control-group");

    if (!isValid()) {
      baseElement.addCssClass("has-error");
    }

    if (getLabel() != null) {
      baseElement.addChildNode(new HtmlElement("label")
              .setAttribute("for", localId)
              .setTextContent(t(getLabel())));
    }

    baseElement.addChildNode(new HtmlElement("input")
            .setAttribute("id", localId)
            .setAttribute("type", "file")
            .setAttribute("name", getName())
            .setTextContent(format(getValue())));

    if (!isValid()) {
      for (String error : getErrors()) {
        baseElement.addChildNode(new HtmlElement("p")
                .addCssClass("error-message")
                .setTextContent(t(error)));
      }
    }

    return applyDecorators(baseElement);
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  @Override
  public FileFormControl setLabel(String label) {
    super.setLabel(label);
    return this;
  }
  //</editor-fold>
}
