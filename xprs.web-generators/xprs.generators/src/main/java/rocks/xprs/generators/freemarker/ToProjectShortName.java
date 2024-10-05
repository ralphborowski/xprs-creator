/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.generators.freemarker;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import java.util.List;

/**
 * Freemarker method to get the last part of the basePackage.
 * 
 * @author Ralph Borowski
 */
public class ToProjectShortName implements TemplateMethodModelEx {

  /**
   * Gets the project short name as the last part of the basePackage.
   * @param arguments a list with basePackage
   * @return the short mane
   * @throws TemplateModelException if no or a wrong argument is given
   */
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    
    // check for arguments
    if (!arguments.isEmpty()) {
      
      // get first argument and convert to object
      if (arguments.get(0) instanceof SimpleScalar) {
        String[] nameParts = ((SimpleScalar)arguments.get(0)).getAsString().split("\\.");
        return nameParts[nameParts.length - 1].substring(0, 1).toUpperCase() + nameParts[nameParts.length - 1].substring(1);
      }
      
      // parameter has wrong type
      throw new TemplateModelException("[toProjectShortName] Argument is of type " + arguments.get(0).getClass().getCanonicalName() + ". Should be String.");
    }
    
    // no parameter given, throw exception
    throw new TemplateModelException("[toProjectShortName] Argument missing.");
  }
  
}
