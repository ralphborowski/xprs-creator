/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import rocks.xprs.exceptions.XprsException;
import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;
import rocks.xprs.html.HtmlTextNode;

/**
 *
 * @author borowski
 * @param <T>
 */
public class FormView<T> extends GroupView {

  private static final Logger LOG = Logger.getLogger(FormView.class.getName());

  private boolean isTargeted = false;
  private boolean isFileUpload = false;
  private DefaultFormControlView.UpdateType updateType = DefaultFormControlView.UpdateType.POST;
  private final List<FormListener> listeners = new LinkedList<>();
  private boolean showFormActions = true;
  private String formActionSubmitLabel = null;
  private String formActionCancelLabel = null;
  private String formActionCancelUrl = null;

  @Override
  public FormView<T> addView(View view) {
    super.addView(view);
    if (view instanceof FileFormControl) {
      isFileUpload = true;
    }
    return this;
  }

  @Override
  public FormView<T> addView(String name, View view) {
    super.addView(name, view);
    if (view instanceof FileFormControl) {
      isFileUpload = true;
    }
    return this;
  }

  @Override
  public FormView<T> addDecorator(Decorator decorator) {
    super.addDecorator(decorator);
    return this;
  }

  @Override
  public FormView<T> addDecorator(Decorator... decorator) {
    super.addDecorator(decorator);
    return this;
  }

  @Override
  public <U extends View> FormView<T> addDecorator(Decorator decorator, Class<U> clazz) {
    super.addDecorator(decorator, clazz);
    return this;
  }

  @Override
  public FormView<T> bind(String expression) {
    super.bind(expression);
    return this;
  }

  @Override
  public FormView<T> bind(Object object, String expression) {
    super.bind(object, expression);
    return this;
  }

  @Override
  public FormView<T> removeView(View view) {
    super.removeView(view);
    return this;
  }

  @Override
  public void init() {

    String sourceForm = getContext().getPostParameters().getValue("_source_form");
    String csrfToken = getContext().getPostParameters().getValue("_csrf_token");
    isTargeted = sourceForm != null && getName() != null && sourceForm.equals(getName())
            && csrfToken != null && getContext().checkCsrfToken(csrfToken, getName());
  }

  @Override
  public void onPost() throws XprsException {
    super.onPost();
    resetSequenceNumbers();
    if (isTargeted && getContext().isPostback() && updateType == DefaultFormControlView.UpdateType.POST) {
      formSubmitted();
    }
  }

  @Override
  public void onGet() throws XprsException {
    super.onGet();
    resetSequenceNumbers();
    if (isTargeted && !getContext().isPostback() && updateType == DefaultFormControlView.UpdateType.GET) {
      formSubmitted();
    }
  }

  private void formSubmitted() {
    List<FormControl> formControlViews = this.find(FormControl.class);

    // reverse order to update child items first
    Collections.reverse(formControlViews);

    boolean isValid = true;
    for (FormControl f : formControlViews) {
      if (f.validate()) {
        f.updateModel();
      } else {
        isValid = false;
      }
    }

    // fire up events
    for (FormListener l : listeners) {
      l.onFormSubmitted(this);
      if (isValid) {
        l.onValidatePassed(this);
      } else {
        l.onValidateFailed(this);
      }
    }
  }

  @Override
  public HtmlNode render() {
    HtmlElement baseElement = new HtmlElement("form")
            .setAttribute("method", "post")
            .setAttribute("autocomplete", "off")
            .addChildNode(
                    new HtmlElement("input")
                            .setAttribute("type", "hidden")
                            .setAttribute("name", "_source_form")
                            .setAttribute("value", getName()))
            .addChildNode(
                    new HtmlElement("input")
                            .setAttribute("type", "hidden")
                            .setAttribute("name", "_csrf_token")
                            .setAttribute("value", getContext().getCsrfToken(getName())))
            .addChildNode(super.render());

    if (isFileUpload) {
      baseElement.setAttribute("enctype", "multipart/form-data");
    }

    if (isShowFormActions()) {
      HtmlElement formActionWrapper = new HtmlElement("div")
              .addCssClass("form-actions");

      HtmlElement submitButton = new HtmlElement("button")
              .addCssClass("button-primary")
              .setAttribute("type", "submit");
      if (formActionSubmitLabel != null) {
        submitButton.setTextContent(formActionSubmitLabel);
      } else {
        submitButton.setTextContent(t("form.actions.save"));
      }
      formActionWrapper.addChildNode(submitButton);

      if (formActionCancelUrl != null) {
        formActionWrapper.addChildNode(new HtmlTextNode(" "))
                .addChildNode(new HtmlTextNode(t("form.actions.or")))
                .addChildNode(new HtmlTextNode(" "));

        formActionWrapper.addChildNode(new HtmlElement("a")
                .setAttribute("href", formActionCancelUrl)
                .setTextContent(formActionCancelLabel));
      }

      baseElement.addChildNode(formActionWrapper);
    }

    return applyDecorators(baseElement);
  }

  /**
   * @return the isValid
   */
  public boolean isValid() {
    boolean isValid = true;
    List<DefaultFormControlView> formControls = find(DefaultFormControlView.class);
    for (DefaultFormControlView f : formControls) {
      isValid &= f.isValid();
    }
    return isValid;
  }

  /**
   * @return the isPostback
   */
  public boolean isPostback() {
    return isTargeted;
  }

  public FormView addListener(FormListener formListener) {
    this.listeners.add(formListener);
    return this;
  }

  public FormView removeListener(FormListener formListener) {
    this.listeners.remove(formListener);
    return this;
  }

  private void resetSequenceNumbers() {

    List<FormControl> formControlViews = this.find(FormControl.class);
    Map<String, Integer> numberMap = new HashMap<>();
    for (FormControl f : formControlViews) {
      if (!numberMap.containsKey(f.getName())) {
        numberMap.put(f.getName(), 0);
      }
      f.setSequence(numberMap.get(f.getName()));
      numberMap.put(f.getName(), f.getSequence() + 1);
    }
  }

  public static interface FormListener {

    public void onFormSubmitted(FormView form);

    public void onValidatePassed(FormView form);

    public void onValidateFailed(FormView form);

  }

  /**
   * @return the showFormActions
   */
  public boolean isShowFormActions() {
    return showFormActions;
  }

  public FormView setShowFormActions(boolean showFormActions) {
    this.showFormActions = showFormActions;
    return this;
  }

  public FormView setSubmitButton(String label) {
    this.formActionSubmitLabel = label;
    return this;
  }

  public FormView setCancelButton(String label, String url) {
    this.formActionCancelLabel = label;
    this.formActionCancelUrl = url;
    return this;
  }
}
