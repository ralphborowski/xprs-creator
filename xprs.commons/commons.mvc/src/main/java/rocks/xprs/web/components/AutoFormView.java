/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import rocks.xprs.db.Entity;
import rocks.xprs.db.EntityDAO;
import rocks.xprs.exceptions.ResourceNotFoundException;
import rocks.xprs.types.Monetary;
import rocks.xprs.util.ReflectiveUtil;
import rocks.xprs.util.Strings;

/**
 *
 * @author borowski
 */
public class AutoFormView extends FormView {

  private static final Logger LOG = Logger.getLogger(AutoFormView.class.getName());

  private List<String> excludedFields = Collections.EMPTY_LIST;
  private Map<String, List<? extends Entity>> domain = new HashMap<>();
  private Map<String, Class<? extends View>> viewRenderers = new HashMap<>();
  private Map<String, String> hints = new HashMap<>();

  @Override
  public void initViews() {

    if (getModel() != null) {
      Class modelClass = getModel().getClass();

      addDecorator(new CssClassDecorator().add("auto-form"));

      for (Field f : ReflectiveUtil.getFields(modelClass)) {

        if (excludedFields.contains(f.getName())) {
          continue;
        }

        Method m = ReflectiveUtil.getGetterMethod(modelClass, f.getName());
        if (m != null) {

          Class<? extends View> renderer = viewRenderers.get(f.getName());
          Class returnClass = m.getReturnType();

          if (renderer == null) {
            if (Entity.class.isAssignableFrom(returnClass)) {
              renderer = Select.class;
            } else if (Monetary.class.equals(returnClass)) {
              renderer = MonetaryInputView.class;
            } else if (Boolean.class.equals(returnClass)) {
              renderer = Checkbox.class;
            } else if (returnClass.isEnum()) {
              renderer = Select.class;
            } else {
              renderer = TextInput.class;
            }
          }

          if (renderer == Select.class) {
            List choices = null;
            Select.OptionRenderer optionRenderer = null;
            if (domain.containsKey(f.getName())) {
              optionRenderer = new EntityOptionRenderer(domain.get(f.getName()), getContext());
            } else if (returnClass.isEnum()) {
              optionRenderer = new EnumOptionRenderer(returnClass);
            } else if (Entity.class.isAssignableFrom(returnClass)) {
              try {
                EntityDAO dao = DataSourceRegister.getDao(returnClass);
                choices = dao.list();
                optionRenderer = new EntityOptionRenderer(choices, getContext());
              } catch (ResourceNotFoundException ex) {
                LOG.log(Level.SEVERE,
                        String.format("Data source for %s not found. Did you register "
                                + "it with the DataSourceRegister?",
                                returnClass.getCanonicalName()),
                        ex);
              }

            }

            Select selectView = new Select(returnClass)
                    .setLabel(t(Strings.decapitalize(modelClass.getSimpleName())
                            + ".attributes." + f.getName()))
                    .bind(f.getName())
                    .setOptionRenderer(optionRenderer);

            if (hints.containsKey(f.getName())) {
              selectView.setHint(hints.get(f.getName()));
            }

            if (!returnClass.isEnum()) {
              selectView.setRenderEmpty(true);
            }
            addView(f.getName(), selectView);

          } else if (renderer == MonetaryInputView.class) {
            MonetaryInputView input = new MonetaryInputView()
                    .setLabel(t(Strings.decapitalize(modelClass.getSimpleName())
                            + ".attributes." + f.getName()))
                    .bind(f.getName());

            if (hints.containsKey(f.getName())) {
              input.setHint(hints.get(f.getName()));
            }

            addView(f.getName(), input);
          } else if (renderer == TextInput.class) {
            TextInput input = new TextInput(returnClass)
                    .setLabel(t(Strings.decapitalize(modelClass.getSimpleName())
                            + ".attributes." + f.getName()))
                    .bind(f.getName());

            if (hints.containsKey(f.getName())) {
              input.setHint(hints.get(f.getName()));
            }

            addView(f.getName(), input);
          } else if (renderer == Checkbox.class) {
            Checkbox<Boolean> checkbox = new Checkbox<>(Boolean.class)
                    .setLabel(t(Strings.decapitalize(modelClass.getSimpleName())
                            + ".attributes." + f.getName()))
                    .bind(f.getName());

            if (hints.containsKey(f.getName())) {
              checkbox.setHint(hints.get(f.getName()));
            }

            addView(f.getName(), checkbox);
          } else {
            try {
              View view = renderer.getConstructor().newInstance()
                      .bind(f.getName());

              if (view instanceof DefaultFormControlView) {
                DefaultFormControlView defaultView = (DefaultFormControlView) view;
                defaultView.setLabel(
                        t(Strings.decapitalize(modelClass.getSimpleName())
                                + ".attributes." + f.getName()));

                if (hints.containsKey(f.getName())) {
                  defaultView.setHint(hints.get(f.getName()));
                }
              }

              addView(f.getName(), view);
            } catch (ReflectiveOperationException ex) {
              LOG.log(Level.SEVERE,
                      String.format("Could not create view for field %s with renderer %s. "
                              + "Does it have an empty constructor?",
                              f.getName(), renderer.getCanonicalName()),
                      ex);
            }
          }
        }
      }
    }
  }

  public AutoFormView addDomain(String field, List<? extends Entity> entities) {
    if (entities == null) {
      removeDomain(field);
    } else {
      domain.put(field, entities);
    }
    return this;
  }

  public AutoFormView removeDomain(String field) {
    domain.remove(field);
    return this;
  }

  public AutoFormView addExcludedField(String... fields) {
    if (excludedFields == Collections.EMPTY_LIST || excludedFields == null) {
      excludedFields = new LinkedList<>();
    }

    for (String f : fields) {
      excludedFields.add(f);
    }

    return this;
  }

  @Override
  public AutoFormView addListener(FormListener listener) {
    super.addListener(listener);
    return this;
  }

  @Override
  public AutoFormView addView(View view) {
    super.addView(view);
    return this;
  }

  @Override
  public AutoFormView addView(String name, View view) {
    super.addView(name, view);
    return this;
  }

  public AutoFormView setRenderer(String field, Class<? extends View> viewRenderer) {
    viewRenderers.put(field, viewRenderer);
    return this;
  }

  public AutoFormView setHint(String field, String hint) {
    hints.put(field, hint);
    return this;
  }

  @Override
  public AutoFormView bind(String expression) {
    super.bind(expression);
    return this;
  }

  @Override
  public AutoFormView bind(Object object, String expression) {
    super.bind(object, expression);
    return this;
  }

  @Override
  public AutoFormView unbind() {
    super.unbind();
    return this;
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
   */
  public AutoFormView setExcludedFields(List<String> excludedFields) {
    this.excludedFields = excludedFields;
    return this;
  }
  //</editor-fold>

}
