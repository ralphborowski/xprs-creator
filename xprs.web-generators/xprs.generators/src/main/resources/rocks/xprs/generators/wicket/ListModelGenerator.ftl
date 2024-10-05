<#ftl strip_whitespace = false>/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */
package ${context.project.basePackage}.gui.model;

import ${toCanonicalFilterClassName(model)};

/**
 * A detachable model for ${model.name} lists.
 * Use if a component requires an IModel<List> for binding, 
 * for example DropDownChoice.
 */
public class ${model.name}ListModel extends Abstract${model.name}ListModel {

  public ${model.name}ListModel() {
    super();
  }

  public ${model.name}ListModel(${model.name}Filter filter) {
    super(filter);
  }
  
}