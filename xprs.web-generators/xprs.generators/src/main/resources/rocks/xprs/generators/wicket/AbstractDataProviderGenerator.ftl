<#ftl strip_whitespace = false>/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */
package ${context.project.basePackage}.gui.dataprovider;

import ${toCanonicalClassName(model)};
import ${toCanonicalDaoClassName(model)};
import ${toCanonicalFilterClassName(model)};
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import rocks.xprs.runtime.db.EntityManagerFilter;

/**
 * A data provider for ${toPlural(model.name?uncap_first)}.
 */
public abstract class Abstract${model.name}DataProvider implements IDataProvider<${model.name}> {

  private final ${model.name}Filter filter;
  private List<${model.name}> ${model.name?uncap_first}List;

  public Abstract${model.name}DataProvider(${model.name}Filter filter) {
    super();
    this.filter = filter;
  }

  @Override
  public Iterator iterator(long first, long count) {

    EntityManager em = EntityManagerFilter.getEntityManager();
    filter.setOffset((int) first);
    filter.setLimit((int) count);

    try (${toPlural(model.name)} ${toPlural(model.name?uncap_first)} = new ${toPlural(model.name)}(em)) {
      ${model.name?uncap_first}List = ${toPlural(model.name?uncap_first)}.list(filter);
      return ${model.name?uncap_first}List.iterator();
    }
  }

  @Override
  public long size() {
  
    // use cache?
    if (${model.name?uncap_first}List == null) {
      EntityManager em = EntityManagerFilter.getEntityManager();
      try (${toPlural(model.name)} ${toPlural(model.name?uncap_first)} = new ${toPlural(model.name)}(em)) {
        ${model.name?uncap_first}List = ${toPlural(model.name?uncap_first)}.list(filter);
      }
    }
    
    return ${model.name?uncap_first}List.size();
  }

  @Override
  public IModel<${model.name}> model(final ${model.name} object) {

    return new LoadableDetachableModel<${model.name}>() {
      @Override
      protected ${model.name} load() {
        return object;
      }
    };
  }

  @Override
  public void detach() {
    ${model.name?uncap_first}List = null;
  }
}