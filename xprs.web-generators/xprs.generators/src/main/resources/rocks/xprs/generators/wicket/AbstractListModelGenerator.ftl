<#ftl strip_whitespace = false>/**
 * This software was created with xprs.
 * Have a look at https://xprs.rocks/ for more details.
 */
package ${context.project.basePackage}.gui.model;

import ${toCanonicalClassName(model)};
import ${toCanonicalDaoClassName(model)};
import ${toCanonicalFilterClassName(model)};
import java.util.List;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * A detachable model for ${model.name} lists.
 * Use if a component requires an IModel<List> for binding,
 * for example DropDownChoice.
 */
public abstract class Abstract${model.name}ListModel
        extends LoadableDetachableModel<List<${model.name}>> {

  private ${model.name}Filter filter;

  public Abstract${model.name}ListModel() {
    this.filter = new ${model.name}Filter();
  }

  public Abstract${model.name}ListModel(${model.name}Filter filter) {
    this.filter = filter;
  }

  @Override
  protected List<${model.name}> load() {
    ${toPlural(model.name)} ${toPlural(model.name?uncap_first)} = new ${toPlural(model.name)}();
    return (List<${model.name}>) ${toPlural(model.name?uncap_first)}.list(filter);
  }

}