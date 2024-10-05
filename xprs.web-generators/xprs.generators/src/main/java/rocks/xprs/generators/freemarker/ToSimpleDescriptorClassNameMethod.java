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
import rocks.xprs.languages.xprsm.model.Project;

/**
 * Freemarker method to convert a project to a descriptor class name.
 *
 * @author Ralph Borowski
 */
public class ToSimpleDescriptorClassNameMethod implements TemplateMethodModelEx {

  /**
   * Converts a project to a canonical descriptor class name.
   *
   * @param arguments a list with one xprs project object
   * @return the canonical class name
   * @throws TemplateModelException if no or a wrong argument is given
   */
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    
    if (!arguments.isEmpty()) {
      // get first argument and convert to object
      if (arguments.get(0) instanceof StringModel) {
        Object wrappedObject = ((StringModel) arguments.get(0)).getWrappedObject();
        if (wrappedObject instanceof Project) {
          Project project = (Project) wrappedObject; 
          return getSimpleClassName(project);
        } else {
          throw new TemplateModelException("[toCanonicalDescriptorClassName] Argument is of type " + arguments.get(0).getClass().getCanonicalName() + ". Should be rocks.xprs.languages.xprsm.model.Project");
        }
      }
    }
    
    // no parameter given, throw exception
    throw new TemplateModelException("[toCanonicalDescriptorClassName] Argument missing.");
  }
  
  public static String getSimpleClassName(Project project) {
    // get name
    String[] nameParts = project.getBasePackage().split("\\.");
    return nameParts[nameParts.length - 1].substring(0, 1).toUpperCase() + nameParts[nameParts.length - 1].substring(1) + "Descriptor";
  }

}
