/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import rocks.xprs.html.DefaultHtmlRenderer;
import rocks.xprs.html.HtmlDocument;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.web.Context;
import rocks.xprs.web.Controller;
import rocks.xprs.web.UrlBuilder;

/**
 *
 * @author borowski
 */
public abstract class PageView extends GroupView implements Controller {

  private String title;
  private final Map<String, Stylesheet> stylesheets = new HashMap<>();
  private final Map<String, Script> scripts = new HashMap<>();
  private String redirectUrl = null;
  private boolean renderHtml = true;

  @Override
  public void init(Context context) throws Exception {
    // ignore
  }

  @Override
  public void process() throws Exception {

    // run the page life cycle
    loadModels();
    initViews();

    if (getContext().isPostback()) {
      onPost();
    } else {
      onGet();
    }

    // redirect?
    if (redirectUrl != null) {
      getContext().getResponse().sendRedirect(redirectUrl);
      return;
    }

    // render page
    if (renderHtml) {
      try ( OutputStream out = getContext().getResponse().getOutputStream();
               DefaultHtmlRenderer renderer = new DefaultHtmlRenderer(out);) {

        HtmlDocument doc = (HtmlDocument) this.render();
        renderer.render(doc);
      }
    }
  }

  public void setResponseHeader(String name, String value) {
    getContext().getResponse().setHeader(name, value);
  }

  public void setResponseHeader(String name, LocalDateTime value) {
    getContext().getResponse().setDateHeader(name,
            value.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000l);
  }

  public OutputStream getOutputStream() throws IOException {
    this.renderHtml = false;
    return getContext().getResponse().getOutputStream();
  }

  @Override
  public HtmlNode render() {

    HtmlDocument doc = new HtmlDocument();

    doc.setDoctype("<!DOCTYPE html>");

    doc.getHead().addChildNode(
            new HtmlElement("base")
                    .setAttribute("href", UrlBuilder.getAbsoluteLink(getContext().getRequest(), "/")));

    doc.setTitle(title);

    // add stylesheets
    for (Stylesheet s : stylesheets.values()) {
      HtmlElement styleElement = new HtmlElement("link")
              .setAttribute("rel", "stylesheet")
              .setAttribute("href", s.getHref());
      if (s.getMedia() != null) {
        styleElement.setAttribute("media", s.getMedia());
      }

      doc.getHead().addChildNode(styleElement);
    }

    // add script
    List<HtmlElement> bottomScripts = new LinkedList<>();
    for (Map.Entry<String, Script> entry : scripts.entrySet()) {
      HtmlElement scriptElement = new HtmlElement("script")
              .setAttribute("type", entry.getValue().getType());
      scriptElement.setId(entry.getKey());

      if (entry.getValue().getData() != null) {
        scriptElement.setTextContent(entry.getValue().getData());
        bottomScripts.add(scriptElement);
      } else {
        scriptElement.setAttribute("src", entry.getValue().getSrc());
        doc.getHead().addChildNode(scriptElement);
      }
    }

    // render body
    doc.getBody().addChildNode(super.render());

    // add scripts to the bottom
    for (HtmlElement s : bottomScripts) {
      doc.getBody().addChildNode(s);
    }

    return applyDecorators(doc);
  }

  public void redirect(String url) {
    this.redirectUrl = url;
  }

  public void redirect(Class<? extends Controller> controller, Object... parameters) {
    redirect(getContext().getUrl(controller, parameters));
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  public PageView addStylesheet(String name, Stylesheet stylesheet) {
    this.stylesheets.put(name, stylesheet);
    return this;
  }

  public PageView removeStylesheet(String name) {
    this.stylesheets.remove(name);
    return this;
  }

  public PageView addScript(String name, Script script) {
    this.scripts.put(name, script);
    return this;
  }

  public PageView removeScript(String name) {
    this.stylesheets.remove(name);
    return this;
  }
  //</editor-fold>

  public static class Stylesheet {

    private String href;
    private String media;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * @return the href
     */
    public String getHref() {
      return href;
    }

    /**
     * @param href the href to set
     * @return self
     */
    public Stylesheet setHref(String href) {
      this.href = href;
      return this;
    }

    /**
     * @return the media
     */
    public String getMedia() {
      return media;
    }

    /**
     * @param media the media to set
     * @return self
     */
    public Stylesheet setMedia(String media) {
      this.media = media;
      return this;
    }
    //</editor-fold>
  }

  public static class Script {

    private String src;
    private String data;
    private String type = "text/javascript";

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * @return the src
     */
    public String getSrc() {
      return src;
    }

    /**
     * @param src the src to set
     * @return self
     */
    public Script setSrc(String src) {
      this.data = null;
      this.src = src;
      return this;
    }

    /**
     * @return the data
     */
    public String getData() {
      return data;
    }

    /**
     * @param data the data to set
     * @return self
     */
    public Script setData(String data) {
      this.src = null;
      this.data = data;
      return this;
    }

    /**
     * @return the type
     */
    public String getType() {
      return type;
    }

    /**
     * @param type the type to set
     */
    public Script setType(String type) {
      this.type = type;
      return this;
    }
    //</editor-fold>
  }
}
