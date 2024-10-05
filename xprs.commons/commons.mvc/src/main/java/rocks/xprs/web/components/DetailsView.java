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
public class DetailsView extends PageView {

  private final Heading heading = new Heading();
  private final Toolbar toolbar = new Toolbar();

  private View details;
  private TabMenu tabs = new TabMenu();
  private View tabContent;

  @Override
  public void initViews() {
    addView(heading);
    addView(toolbar);

    if (details != null) {
      addView(details);
    }

    if (tabs != null) {
      addView(tabs);
    }

    if (tabContent != null) {
      addView(tabContent);
    }
  }

  public DetailsView addTab(Link link) {
    tabs.addMenuItem(link);
    return this;
  }

  @Override
  public HtmlNode render() {
    HtmlElement baseElement = (HtmlElement) super.render();
    if (details != null) {
      baseElement.addChildNode(details.render());
    }

    if (!tabs.isEmpty()) {
      baseElement.addChildNode(tabs.render());
      baseElement.addChildNode(tabContent.render());
    }

    return baseElement;
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the heading
   */
  public Heading getHeading() {
    return heading;
  }

  /**
   * @return the toolbar
   */
  public Toolbar getToolbar() {
    return toolbar;
  }

  /**
   * @return the details
   */
  public View getDetails() {
    return details;
  }

  /**
   * @param details the details to set
   * @return same object
   */
  public DetailsView setDetails(View details) {
    this.details = details;
    return this;
  }

  /**
   * @return the tabs
   */
  public TabMenu getTabs() {
    return tabs;
  }

  /**
   * @param tabs the tabs to set
   * @return same object
   */
  public DetailsView setTabs(TabMenu tabs) {
    this.tabs = tabs;
    return this;
  }

  /**
   * @return the tabContent
   */
  public View getTabContent() {
    return tabContent;
  }

  /**
   * @param tabContent the tabContent to set
   * @return same object
   */
  public DetailsView setTabContent(View tabContent) {
    this.tabContent = tabContent;
    return this;
  }
  //</editor-fold>

}
