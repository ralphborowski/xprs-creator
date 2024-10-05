/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import rocks.xprs.xml.Template;
import rocks.xprs.serialization.XmlSerializer;

/**
 *
 * @author borowski
 */
public abstract class Page implements Controller {

  protected Context context;
  private HashMap<String, Object> data = new HashMap<>();
  private boolean isRedirect = false;

  @Path("/")
  public abstract void loadPage() throws Exception;

  protected void onPageLoad() {

  }

  protected void onPageLoaded() {

  }

  protected void onRender(Template t) {

  }

  protected void onRendered(String s) {

  }

  @Override
  public void init(Context context) {
    this.context = context;
  }

  @Override
  public void process() throws Exception {

    // load page
    onPageLoad();
    loadPage();
    onPageLoaded();

    URL stylesheetUrl = this.getClass().getClassLoader().getResource(
            this.getClass().getCanonicalName().replace(".", "/") + ".xsl");

    if (stylesheetUrl != null && !isRedirect) {

    File stylesheet = new File(stylesheetUrl.toURI());

      // load template
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Template t = new Template(baos);

      // add local objects
      t.setRootName("page");

      t.setStylesheet(stylesheet);
      String pageSerialized = XmlSerializer.serialize(this);
      t.bypass(pageSerialized);

      // render page
      onRender(t);
      t.render();

      if (context.getResponse().getContentType() == null
              || context.getResponse().getContentType().isEmpty()) {

        context.getResponse().setContentType("text/html;charset=UTF-8");
      }

      context.getResponse().setContentLength(baos.size());

      try {
        context.getResponse().getWriter().print(baos.toString("UTF-8"));
      } catch (IllegalStateException ex) {
        // ignore, maybe was called already in a page
      }
    }

  }

  protected boolean isPostback() {
    return context.getRequest().getMethod().equalsIgnoreCase("POST");
  }

  protected void putData(String key, Object value) {
    data.put(key, value);
  }

  public void redirect(String url) throws IOException {
    context.getResponse().sendRedirect(url);
    isRedirect = true;
  }

  public Map<String, Object> getRequest() {

    HashMap<String, Object> requestMap = new HashMap<>();

    // build request map for xml
    requestMap.put("parameters", context.getRequest().getParameterMap());
    requestMap.put("referer", context.getRequest().getHeader("Referer"));
    requestMap.put("requestUrl", context.getRequest().getRequestURL().toString());

    StringBuffer rawUrl = context.getRequest().getRequestURL();
    if (context.getRequest().getQueryString() != null) {
      rawUrl.append("?");
      rawUrl.append(context.getRequest().getQueryString());
    }
    requestMap.put("rawUrl", rawUrl.toString());
    requestMap.put("isSecure", context.getRequest().isSecure());

    requestMap.put("applicationPath", UrlBuilder.getAbsoluteLink(context.getRequest(), "/"));
    requestMap.put("contextPath", UrlBuilder.getAbsoluteLink(context.getRequest(), "/"));

    return requestMap;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public Map<String, Object> getSession() {

    // build session map for xml
    HashMap<String, Object> sessionMap = new HashMap<>();

    Enumeration<String> names = context.getRequest().getSession().getAttributeNames();
    while (names.hasMoreElements()) {
      String name = names.nextElement();
      sessionMap.put(name, context.getRequest().getSession().getAttribute(name));
    }

    return sessionMap;
  }

  public Object getUser() {
    return context.getUser();
  }

  public List<Notification> getNotifications() {
    List<Notification> notifList = new LinkedList<>(context.listNotifications());
    context.clearNotifications();
    return notifList;
  }
}
