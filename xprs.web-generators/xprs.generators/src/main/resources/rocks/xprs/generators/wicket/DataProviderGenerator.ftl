<#ftl strip_whitespace = false>/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */
package ${context.project.basePackage}.gui.dataprovider;

import ${toCanonicalFilterClassName(model)};

/**
 * A data provider for ${toPlural(model.name?uncap_first)}.
 */
public class ${model.name}DataProvider extends Abstract${model.name}DataProvider {

  public ${model.name}DataProvider(${model.name}Filter filter) {
    super(filter);
  }
  
}