package rocks.xprs.components;

import rocks.xprs.exceptions.XprsException;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.web.Context;

/**
 *
 * @author rborowski
 */
public interface IView {

  public View addDecorator(Decorator... decorator);
  public String applyExpression(String expression);
  public View bind(String expression);
  public View bind(Object object, String expression);
  public String format(Object o);
  public Context getContext();
  public Object getModel();
  public String getName();
  public View getParent();
  public View getRootView();
  public int getSequence();
  public void init();
  public void initViews();
  public boolean isVisible();
  public void loadModels() throws XprsException;
  public void onGet() throws XprsException;
  public void onPost() throws XprsException;
  public HtmlNode render();
  public void setName(String name);
  public void setParent(View parent);
  public void setSequence(int sequence);
  public View setVisible(boolean isVisible);
  public String t(String key);
  public View unbind();

}
