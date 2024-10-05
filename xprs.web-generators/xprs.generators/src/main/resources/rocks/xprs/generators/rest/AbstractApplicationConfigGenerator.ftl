/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${project.basePackage}.api;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * ApplicationConfig class for ${toProjectShortName(project.basePackage)?lowerCase}
 */
public class AbstractApplicationConfig extends Application {

  public AbstractApplicationConfig() {
    super();
  }
  
  @Override
  public Set<Class<?>> getClasses() {
    HashSet<Class<?>> classes = new HashSet<>();<#list project.models as model >
    classes.add(${model.name}Resource.class);</#list>
    
    return classes;
  }
}


