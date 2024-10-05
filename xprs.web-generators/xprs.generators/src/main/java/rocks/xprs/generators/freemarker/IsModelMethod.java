/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.generators.freemarker;

import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import java.util.List;
import rocks.xprs.languages.xprsm.model.Model;

/**
 * Checks if a given type is a model.
 *
 * @author Ralph Borowski
 */
public class IsModelMethod implements TemplateMethodModelEx {

  /**
   * Checks if a given type is a model.
   *
   * @param arguments a list with one xprs type object
   * @return true, id a given object is a model
   * @throws TemplateModelException if no or a wrong argument is given
   */
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (!arguments.isEmpty()) {
      // get first argument and convert to object
      if (arguments.get(0) instanceof StringModel) {
        Object wrappedObject = ((StringModel) arguments.get(0)).getWrappedObject();
        if (wrappedObject instanceof Model) {
          return true;
        } else {
          return false;
        }
      }
    }
    
    throw new TemplateModelException("[isModel] Argument is missing.");
  }

}
