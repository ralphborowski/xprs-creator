package rocks.xprs.components;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rocks.xprs.util.ReflectiveUtil;

/**
 *
 * @author rborowski
 */
public abstract class DefaultFormControlView<T, U> extends View implements FormControl {

  private static final Logger LOG = Logger.getLogger(DefaultFormControlView.class.getName());

  private UpdateType updateType = UpdateType.POST;
  private String label;

  private final List<Validator<U>> validators = new LinkedList<>();
  private final List<String> errors = new LinkedList<>();
  private final Class<T> type;
  private String hint;

  public DefaultFormControlView(Class<T> type) {
    this.type = type;
  }

  public abstract U getRawValue();

  public abstract T getValue();

  @Override
  public DefaultFormControlView updateOn(UpdateType updateType) {
    this.updateType = updateType;
    return this;
  }

  @Override
  public void updateModel() {

    if ((getUpdateType() == UpdateType.POST && !getContext().isPostback())
            || (getUpdateType() == UpdateType.GET && getContext().isPostback())) {

      return;
    }

    Object targetObject = getModel();
    String expression = getExpression();
    if (targetObject == null || getExpression() == null) {
      return;
    }

    if (expression.contains(".")) {
      int lastDot = expression.lastIndexOf(".");
      String getPath = getExpression().substring(0, lastDot);
      targetObject = ReflectiveUtil.getValue(targetObject, getPath, getSequence());
      expression = expression.substring(lastDot + 1);
    }

    if (targetObject != null) {
      Method getterMethod = ReflectiveUtil.getGetterMethod(targetObject.getClass(), expression);
      if (getterMethod != null) {
        Method setterMethod = ReflectiveUtil.getSetterMethod(
                targetObject.getClass(), expression, getterMethod.getReturnType());
        if (setterMethod != null) {
          try {
            setterMethod.invoke(targetObject, getValue());
          } catch (ReflectiveOperationException ex) {
            LOG.log(Level.SEVERE,
                    String.format("Unable to access method %s in target object %s",
                            setterMethod.getName(), targetObject.getClass().getName()),
                    ex);
          } catch (IllegalArgumentException ex) {
            LOG.log(Level.SEVERE,
                    String.format("Runtime exception occured on access to method %s in target object %s with value %s.",
                            setterMethod.getName(), targetObject.getClass().getName(), String.valueOf(getValue())),
                    ex);
            throw ex;
          }
        }
      }
    }
  }

  @Override
  public boolean validate() {
    boolean isValid = true;
    for (Validator v : validators) {
      String errorMessage = v.isValid(this.getExpression(), this.getRawValue());
      if (errorMessage != null) {
        addError(errorMessage);
        isValid = false;
      }
    }
    return isValid;
  }

  public List<Validator<U>> getValidators() {
    return validators;
  }

  @Override
  public void addValidator(Validator v) {
    validators.add(v);
  }

  @Override
  public void removeValidator(Validator v) {
    validators.remove(v);
  }

  @Override
  public boolean isValid() {
    return errors.isEmpty();
  }

  @Override
  public void addError(String errorMessage) {
    errors.add(errorMessage);
  }

  @Override
  public List<String> getErrors() {
    return errors;
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @param label the label to set
   * @return the current label
   */
  public DefaultFormControlView setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * @return the type
   */
  public Class<T> getType() {
    return type;
  }

  /**
   * @return the hint
   */
  public String getHint() {
    return hint;
  }

  @Override
  public DefaultFormControlView<T, U> setHint(String hint) {
    this.hint = hint;
    return this;
  }

  /**
   * @return the updateType
   */
  @Override
  public UpdateType getUpdateType() {
    return updateType;
  }
  //</editor-fold>

}
