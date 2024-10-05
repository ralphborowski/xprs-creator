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

/**
 * Checks if a given type is an enum.
 * @author Ralph Borowski
 */
public class IsEnumMethod implements TemplateMethodModelEx {

  /**
   * Checks if the given type is an enum.
   *
   * @param arguments a list with one xprs type object
   * @return true, if type is an enum
   * @throws TemplateModelException if no or a wrong arguments are given
   */
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (!arguments.isEmpty()) {
      // get first argument and convert to object
      if (arguments.get(0) instanceof StringModel) {
        Object wrappedObject = ((StringModel) arguments.get(0)).getWrappedObject();
        return wrappedObject instanceof rocks.xprs.languages.xprsm.model.Enum;
      }
    }
    
    throw new TemplateModelException("[isEnum] Argument is missing.");
  }
  
}
