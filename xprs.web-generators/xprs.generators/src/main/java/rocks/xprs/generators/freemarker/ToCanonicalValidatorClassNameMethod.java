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
import rocks.xprs.languages.xprsm.model.Type;

/**
 * Freemarker method to convert types to canonical names.
 *
 * @author Ralph Borowski
 */
public class ToCanonicalValidatorClassNameMethod implements TemplateMethodModelEx {

  /**
   * Converts a type string to a canonical class name.
   *
   * @param arguments a list with one xprs type object
   * @return the canonical class name or the empty string if type is a
   * primitive.
   * @throws TemplateModelException if no or a wrong argument is given
   */
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (!arguments.isEmpty()) {
      // get first argument and convert to object
      if (arguments.get(0) instanceof StringModel) {
        Object wrappedObject = ((StringModel) arguments.get(0)).getWrappedObject();
        if (wrappedObject instanceof Type) {
          Type argument = (Type) wrappedObject;

          // convert model names
          if (argument instanceof Model) {
            return argument.getCanonicalName().replace("|", ".validator.") + "Validator";
          }

          return "";
        } else {
          throw new TemplateModelException("[toCanonicalValidatorClassName] Argument is of type " + arguments.get(0).getClass().getCanonicalName() + ". Should be rocks.xprs.languages.xprsm.model.Type");
        }
      }
    }
    
    throw new TemplateModelException("[toCanonicalValidatorClassName] Argument is missing.");
  }

}
