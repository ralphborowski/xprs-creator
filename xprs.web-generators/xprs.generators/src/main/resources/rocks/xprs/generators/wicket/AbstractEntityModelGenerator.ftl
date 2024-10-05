<#ftl strip_whitespace = false>/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */
package ${context.project.basePackage}.gui.model;

import ${toCanonicalClassName(model)};
import ${toCanonicalDaoClassName(model)};
import org.apache.wicket.model.LoadableDetachableModel;
import rocks.xprs.runtime.db.EntityManagerFilter;
import rocks.xprs.runtime.exceptions.ResourceNotFoundException;

/**
 * A detachable model for ${model.name}.
 * Use it for displaying data in a component. In forms you should use and 
 * serialize the normal database entity, because if you create new database
 * entries it might not have an ID by now.
 */
public abstract class Abstract${model.name}EntityModel 
        extends LoadableDetachableModel<${model.name}> {

  public Abstract${model.name}EntityModel(Long id) {
  
    if (id != null) {
      try (${toPlural(model.name)} ${toPlural(model.name?uncap_first)} = 
                new ${toPlural(model.name)}(EntityManagerFilter.getEntityManager());) {
        
        super(${toPlural(model.name?uncap_first)}.get(id));
      } catch (ResourceNotFoundException ex) {
        // ignore
      }
    }
    super(null);
  }
}