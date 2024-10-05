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
import rocks.xprs.languages.xprsm.model.Attribute;

/**
 * Freemarker method to convert xprs Attributes to getter method names.
 *
 * @author Ralph Borowski
 */
public class ToGetterMethod implements TemplateMethodModelEx {

  /**
   * Converts a xprs attribute to a getter method.
   * @param arguments a list with one xprs Attribute object
   * @return the getter (without brakets)
   * @throws TemplateModelException if no or a wrong argument is given
   */
  @Override
  public Object exec(List arguments) throws TemplateModelException {

    // check for arguments
    if (!arguments.isEmpty()) {

      // get first argument and convert to object
      if (arguments.get(0) instanceof StringModel) {
        Object wrappedObject = ((StringModel) arguments.get(0)).getWrappedObject();
        if (wrappedObject instanceof Attribute) {
          Attribute argument = (Attribute) wrappedObject;

          // if boolean don't add "get"
          if (argument.getType().getCanonicalName().equals("Boolean")) {
            if (argument.getName().startsWith("is") || argument.getName().startsWith("has")) {
              return argument.getName();
            }
            return "is" + argument.getName().substring(0, 1).toUpperCase() + argument.getName().substring(1);
          }

          // return "get" + attribute name
          return "get" + argument.getName().substring(0, 1).toUpperCase() + argument.getName().substring(1);
        }
      }

      // parameter has wrong type
      throw new TemplateModelException("[toGetter] Argument is of type " + arguments.get(0).getClass().getCanonicalName() + ". Should be rocks.xprs.languages.xprsm.model.Attribute.");
    }

    // no parameter given, throw exception
    throw new TemplateModelException("[toGetter] Argument missing.");
  }

}
