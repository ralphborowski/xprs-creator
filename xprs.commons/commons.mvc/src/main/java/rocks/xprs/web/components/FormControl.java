package rocks.xprs.components;

import java.util.List;

/**
 *
 * @author rborowski
 * @param <T> type of the value
 */
public interface FormControl<T> extends IView {

  public enum UpdateType {
    POST, GET
  }

  public FormControl updateOn(UpdateType updateType);

  public void updateModel();

  public boolean validate();

  public void addValidator(Validator v);

  public void removeValidator(Validator v);

  public boolean isValid();

  public void addError(String errorMessage);
  public List<String> getErrors();

  public FormControl<T> setHint(String hint);

  public UpdateType getUpdateType();

}
