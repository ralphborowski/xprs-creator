package rocks.xprs.components;

import java.util.List;
import java.util.Locale;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.web.Notification;

/**
 *
 * @author rborowski
 */
public class NotificationView extends View {

  @Override
  public HtmlNode render() {

    List<Notification> notifications = getContext().listNotifications();
    if (!notifications.isEmpty()) {

      // create an unordered list of notifications
      HtmlElement ul = new HtmlElement("ul")
              .setAttribute("class", "notification-list");

      for (Notification n : notifications) {
        ul.addChildNode(new HtmlElement("li")
                .addCssClass("notification")
                .addCssClass("notification-" + n.getType().toString().toLowerCase(Locale.ENGLISH))
                .setTextContent(n.getMessage())
        );
      }

      // clear notifications after showing them
      getContext().clearNotifications();

      return applyDecorators(ul);
    }

    return null;
  }

}
