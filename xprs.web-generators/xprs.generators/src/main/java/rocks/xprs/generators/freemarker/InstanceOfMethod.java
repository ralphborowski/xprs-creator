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

/**
 * Checks if a given model inherits from a given class name.
 * @author Ralph Borowski
 */
public class InstanceOfMethod implements TemplateMethodModelEx {

  /**
   * 
   * @param arguments a list with one xprs type object and a class name as string 
   * @return
   * @throws TemplateModelException 
   */
  @Override
  public Boolean exec(List arguments) throws TemplateModelException {
    
    // require 2 arguments type and String
    if (!arguments.isEmpty() && arguments.size() == 2) {
      
      Model model = null;
      String targetClass = null;
      
      // get model argument
      Object argument0 = arguments.get(0);
      if (argument0 instanceof StringModel) {
        Object wrappedObject = ((StringModel) argument0).getWrappedObject();
        
        if (wrappedObject instanceof Model) {
          model = (Model) wrappedObject;
        }
      }
      
      // get class name
      Object argument1 = arguments.get(1);
      if (argument1 instanceof SimpleScalar) {
        targetClass = ((SimpleScalar) argument1).getAsString();
      }
        
      // check types
      return checkType(model, targetClass);
    }
    
    throw new TemplateModelException("[instanceOf] Two arguments required: "
            + "instanceOf(Model, String)");
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
