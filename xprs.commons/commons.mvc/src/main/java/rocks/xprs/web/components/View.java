/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rocks.xprs.exceptions.XprsException;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.util.ReflectiveUtil;
import rocks.xprs.web.Context;

/**
 *
 * @author borowski
 */
public abstract class View implements IView {

  private View parent;
  private boolean isVisible = true;
  private String name;
  private int sequence;
  private Object model;
  private String expression;
  private boolean isModelSet = false;

  private final List<Decorator> decorators = new LinkedList<>();

  @Override
  public void init() {

  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the context
   */
  @Override
  public Context getContext() {
    return Context.get();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the sequence
   */
  @Override
  public int getSequence() {
    return sequence;
  }

  /**
   * @param sequence the sequence to set
   */
  @Override
  public void setSequence(int sequence) {
    this.sequence = sequence;
  }

  /**
   * @return the parent
   */
  @Override
  public View getParent() {
    return parent;
  }

  /**
   * @param parent the parent to set
   */
  @Override
  public void setParent(View parent) {
    this.parent = parent;
  }
  //</editor-fold>

  @Override
  public View getRootView() {
    if (getParent() != null) {
      return getParent().getRootView();
    }
    return this;
  }

  @Override
  public View addDecorator(Decorator... decorator) {
    for (Decorator d : decorator) {
      this.decorators.add(d);
    }
    return this;
  }

  protected HtmlElement applyDecorators(HtmlElement element) {
    for (Decorator d: this.decorators) {
      element = d.decorate(element);
    }
    return element;
  }

  @Override
  public String t(String key) {
    return getContext().t(key);
  }

  public String t(String key, Object... values) {
    return getContext().t(key, values);
  }

  public <T> T parse(String value, Class<T> type) {
    return getContext().parse(value, type);
  }

  @Override
  public String format(Object o) {
    return getContext().format(o);
  }

  @Override
  public String applyExpression(String expression) {
    if (model == null) {
      return expression;
    }

    if (model.getClass().isPrimitive() || model instanceof Number || model instanceof String
            || model instanceof Boolean || model instanceof Character) {
      return expression.replace("{object}", format(model));
    }

    String result = expression;
    Matcher matcher = Pattern.compile("\\{([a-zA-Z0-9_\\-.])+\\}").matcher(expression);
    while (matcher.find()) {
      String e = matcher.group(1);
      result = result.replace("{" + expression + "}",
              format(ReflectiveUtil.getValue(model, e, sequence)));
    }
    return result;
  }

  @Override
  public void loadModels() throws XprsException {

  }

  @Override
  public void initViews() {

  }

  @Override
  public void onGet() throws XprsException {

  }

  @Override
  public void onPost() throws XprsException {

  }

  @Override
  public View bind(String expression) {
    this.expression = expression;
    return this;
  }

  @Override
  public View bind(Object object, String expression) {
    this.model = object;
    this.isModelSet = true;
    this.expression = expression;
    return this;
  }

  @Override
  public View unbind() {
    this.model = null;
    this.isModelSet = false;
    this.expression = null;
    return this;
  }

  @Override
  public Object getModel() {
    if (!isModelSet && parent != null) {
      return parent.getModel();
    }
    return model;
  }


  /**
   * @return the expression
   */
  protected String getExpression() {
    return expression;
  }

  /**
   * @return the isVisible
   */
  @Override
  public boolean isVisible() {
    return isVisible;
  }

  @Override
  public View setVisible(boolean isVisible) {
    this.isVisible = isVisible;
    return this;
  }
}
