/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.generators.freemarker;

import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import java.util.List;
import rocks.xprs.languages.xprsm.model.Model;
import rocks.xprs.languages.xprsm.model.Project;

/**
 * Checks if given model ist the last
 *
 * @author Ralph Borowski
 */
public class IsLastInModelHierarchyMethod implements TemplateMethodModelEx {

  /**
   *
   * @param arguments a list with one xprs type object and a class name as
   * string
   * @return
   * @throws TemplateModelException
   */
  @Override
  public Boolean exec(List arguments) throws TemplateModelException {

    // require 2 arguments type and String
    if (!arguments.isEmpty() && arguments.size() == 2) {

      Model model = null;
      Project project = null;

      // get model argument
      if (arguments.get(0) instanceof StringModel) {
        Object wrappedObject = ((StringModel) arguments.get(0)).getWrappedObject();
        if (wrappedObject instanceof Project) {
          project = (Project) wrappedObject;
        }
      }

      // get model argument
      if (arguments.get(1) instanceof StringModel) {
        Object wrappedObject = ((StringModel) arguments.get(1)).getWrappedObject();
        if (wrappedObject instanceof Model) {
          model = (Model) wrappedObject;
        }
      }

      return checkIsLastMethod(project, model);

    }

    throw new TemplateModelException("[instanceOf] Two arguments required: instanceOf(Project, Model)");
  }

  private Boolean checkIsLastMethod(Project project, Model model) {

    // if project or model null, return false
    if (project == null || model == null) {
      return false;
    }

    for (Model m : project.getModels()) {      
      if (checkType(m.getInheritsFrom(), model.getName())) {
        return false;
      }
    }

    return true;
  }

  private boolean checkType(Model model, String parentClass) {

    // if model null, return false
    if (model == null) {
      return false;
    }

    // model found
    if (model.getName().equals(parentClass)) {
      return true;
    }

    // go on searching
    return checkType(model.getInheritsFrom(), parentClass);
  }

}
