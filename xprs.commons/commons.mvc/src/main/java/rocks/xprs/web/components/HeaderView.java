/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.html.HtmlTextNode;
import rocks.xprs.web.Controller;
import rocks.xprs.web.UrlBuilder;

/**
 *
 * @author borowski
 */
public class HeaderView extends Menu {

  private String logoUrl;
  private String appName;
  private Class<? extends Controller> searchPage;

  @Override
  public HtmlNode render() {

    HtmlElement baseElement = new HtmlElement("div").addCssClass("header");
    HtmlElement headerLogo = new HtmlElement("a")
            .setAttribute("href", UrlBuilder.getAbsoluteLink(getContext().getRequest(), "/"));
    if (logoUrl != null) {
      headerLogo.addChildNode(new HtmlElement("img")
              .setAttribute("src", logoUrl)
              .setAttribute("alt", appName));
      headerLogo.addChildNode(headerLogo);
    } else {
      headerLogo.setTextContent(appName);
    }
    baseElement
            .addChildNode(new HtmlElement("div")
                    .addCssClass("header-meta")
                    .addChildNode(new HtmlElement("div")
                            .addCssClass("header-logo")
                            .addChildNode(headerLogo))
                    .addChildNode(
                            new HtmlElement("div")
                                    .addCssClass("header-profile")
                                    .addChildNode(new HtmlTextNode(
                                            getContext().getUser().getName()))
                                    .addChildNode(new HtmlElement("a")
                                            .setAttribute("href", UrlBuilder.getAbsoluteLink(
                                                    getContext().getRequest(), "/logout/"))
                                            .setTextContent(t("header.logout")))));
    HtmlElement headerNavigation = new HtmlElement("div")
            .addCssClass("header-navigation")
            .addChildNode(
                    new HtmlElement("div")
                            .addCssClass("header-mainmenu")
                            .addChildNode(super.render()));

    if (searchPage != null) {
      headerNavigation.addChildNode(
              new HtmlElement("form")
                      .addCssClass("header-searchform")
                      .setAttribute("method", "GET")
                      .setAttribute("action", getContext().getUrl(searchPage))
                      .addChildNode(new HtmlElement("input")
                              .setAttribute("name", "keyword")
                              .setAttribute("value", getContext()
                                      .getGetParameters().getValue("keyword"))));
    }
    baseElement.addChildNode(headerNavigation);
    return applyDecorators(baseElement);
  }

  @Override
  public HeaderView addMenuItem(Link linkView) {
    super.addMenuItem(linkView);
    return this;
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the logoUrl
   */
  public String getLogoUrl() {
    return logoUrl;
  }

  /**
   * @param logoUrl the logoUrl to set
   * @return the current object
   */
  public HeaderView setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
    return this;
  }

  /**
   * @return the appName
   */
  public String getAppName() {
    return appName;
  }

  /**
   * @param appName the appName to set
   * @return the current object
   */
  public HeaderView setAppName(String appName) {
    this.appName = appName;
    return this;
  }

  /**
   * @return the searchPage
   */
  public Class<? extends Controller> getSearchPage() {
    return searchPage;
  }

  /**
   * @param searchPage the searchPage to set
   * @return the current object
   */
  public HeaderView setSearchPage(Class<? extends Controller> searchPage) {
    this.searchPage = searchPage;
    return this;
  }
  //</editor-fold>

}
