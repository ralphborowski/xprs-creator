/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import rocks.xprs.db.Entity;
import rocks.xprs.util.ReflectiveUtil;
import rocks.xprs.util.Strings;
import rocks.xprs.web.Controller;

/**
 *
 * @author borowski
 */
public class AutoDetailsView extends GroupView {

  private static final Logger LOG = Logger.getLogger(AutoDetailsView.class.getName());

  public static String[] DEFAULT_EXCLUDES =
          new String[] {"id", "createDate", "createUser", "modifyDate", "modifyUser"};

  private List<String> includedFields = Collections.EMPTY_LIST;
  private List<String> excludedFields = Collections.EMPTY_LIST;

  @Override
  public void initViews() {

    if (getModel() != null) {
      Class modelClass = getModel().getClass();
      KeyValueView keyValues = new KeyValueView();
      for (String fieldName : getFieldNames()) {

        Method m = ReflectiveUtil.getGetterMethod(modelClass, fieldName);
        if (m != null) {

          Class returnClass = m.getReturnType();

          View valueView = null;

          if (Entity.class.isAssignableFrom(returnClass)) {
            try {
              Entity referencedEntity = (Entity) m.invoke(getModel());
              Class<? extends Controller> referencedController =
                      DefaultPageRegister.getDetailsUrl(returnClass);

              if (referencedEntity != null && referencedController != null) {
                valueView = new Link().setLabel(this.format(referencedEntity))
                      .setUrl(getContext().getUrl(referencedController, referencedEntity.getId()));
              }
            } catch (ReflectiveOperationException ex) {
              // caught later
            }

            if (valueView == null) {
              valueView = new TextView().bind(getModel(), fieldName + ".displayName");
            }

          } else {
            valueView = new TextView().bind(getModel(), fieldName);
          }

          keyValues.addEntry(t(Strings.decapitalize(
                  modelClass.getSimpleName()) + ".attributes." + fieldName), valueView);
        }
      }

      addView(keyValues);
    }
  }

  public AutoDetailsView addIncludedField(String... fields) {
    if (includedFields == Collections.EMPTY_LIST || includedFields == null) {
      includedFields = new LinkedList<>();
    }

    includedFields.addAll(Arrays.asList(fields));
    return this;
  }

  public AutoDetailsView addExcludedField(String... fields) {
    if (excludedFields == Collections.EMPTY_LIST || excludedFields == null) {
      excludedFields = new LinkedList<>();
    }

    for (String f : fields) {
      excludedFields.add(f);
    }

    return this;
  }

  @Override
  public AutoDetailsView addView(View view) {
    super.addView(view);
    return this;
  }

  @Override
  public AutoDetailsView addView(String name, View view) {
    super.addView(name, view);
    return this;
  }

  @Override
  public AutoDetailsView bind(String expression) {
    super.bind(expression);
    return this;
  }

  @Override
  public AutoDetailsView bind(Object object, String expression) {
    super.bind(object, expression);
    return this;
  }

  @Override
  public AutoDetailsView unbind() {
    super.unbind();
    return this;
  }

  private List<String> getFieldNames() {

    Class modelClass = getModel().getClass();

    if (includedFields != Collections.EMPTY_LIST) {
      return includedFields;
    } else {
      List<String> fieldNames = new LinkedList<>();
      for (Field f : ReflectiveUtil.getFields(modelClass)) {
        if (excludedFields.contains(f.getName())) {
          continue;
        }
        fieldNames.add(f.getName());
      }
      return fieldNames;
    }
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the excludedFields
   */
  public List<String> getExcludedFields() {
    return excludedFields;
  }

  /**
   * @param excludedFields the excludedFields to set
   * @return this object
   */
  public AutoDetailsView setExcludedFields(List<String> excludedFields) {
    this.excludedFields = excludedFields;
    return this;
  }
  //</editor-fold>

}
