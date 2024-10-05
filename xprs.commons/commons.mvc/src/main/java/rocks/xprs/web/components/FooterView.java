/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;

/**
 *
 * @author borowski
 */
public class FooterView extends View {

  private String copyright;

  @Override
  public HtmlNode render() {
    return new HtmlElement("div")
            .addCssClass("footer")
            .setTextContent(copyright != null ? copyright : "");
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the copyright
   */
  public String getCopyright() {
    return copyright;
  }

  /**
   * @param copyright the copyright to set
   * @return the current object
   */
  public FooterView setCopyright(String copyright) {
    this.copyright = copyright;
    return this;
  }
  //</editor-fold>
}
