<#ftl strip_whitespace = false>/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */
package ${context.project.basePackage}.gui.model;

/**
 * A detachable model for ${model.name}.
 * Use it for displaying data in a component. In forms you should use and 
 * serialize the normal database entity, because if you create new database
 * entries it might not have an ID by now.
 */
public class ${model.name}EntityModel extends Abstract${model.name}EntityModel {

  public ${model.name}EntityModel(Long id) {
    super(id);
  }
  
}