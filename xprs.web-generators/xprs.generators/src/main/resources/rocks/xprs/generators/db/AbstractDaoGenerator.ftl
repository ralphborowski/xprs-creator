<#ftl strip_whitespace = false>/**
 * This software was created with xprs.
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.dao;

import ${toCanonicalClassName(model)};<#list model.allTypes as type><#if isModel(type) || isEnum(type) >
import ${toCanonicalClassName(type)};</#if></#list>
import ${context.project.basePackage}.filter.${model.name}Filter;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;<#list model.allTypes as type><#if type.name == "Decimal" || type.name == "Monetary" || type.name == "Date" || type.name == "Time" >
import ${toCanonicalClassName(type)};</#if></#list>
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rocks.xprs.db.EntityDAO;
import rocks.xprs.db.EntityFilter.Order;
import rocks.xprs.db.ResultList;
import rocks.xprs.exceptions.ResourceNotFoundException;

/**
 * DAO class for ${model.name}
 */
public abstract class Abstract${toPlural(model.name)} extends <#if model.inheritsFrom?? >${toPlural(toJavaType(model.inheritsFrom))}<#else>EntityDAO</#if> {

  /**
   * Returns the class of the entities managed by this DAO.
   *
   * @return the entity type
   */
  @Override
  public Class getEntityClass() {
    return ${model.name}.class;
  }

  @Override
  public ${model.name} get(long id) throws ResourceNotFoundException {
    return (${model.name}) super.get(id);
  }

  public ${model.name} save(${model.name} item) {
    return (${model.name}) super.save(item);
  }

  @Override
  public ResultList<? extends ${model.name}> list() {
    return (ResultList<? extends ${model.name}>) super.list();
  }

  /**
   * Get a list of ${toPlural(model.name)} according to the given filter options.
   *
   * @param filter the filter options
   * @return a list of filtered ${toPlural(model.name)}
   */
  public ResultList<${model.name}> list(${model.name}Filter filter) {

    // check database connection
    if (!em.isOpen()) {
      throw new RuntimeException("Entity Manager is not open.");
    }

    // check for filter
    if (filter == null) {
      filter = new ${model.name}Filter();
    }

    // build query using criteria API
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery cq = cb.createQuery();

    // get root
    Root<${model.name}> ${model.name?uncap_first} = cq.from(${model.name}.class);
    cq.select(${model.name?uncap_first});

    // prepare where list
    ArrayList<Predicate> where = new ArrayList<>();

    <#list model.allAttributes as attribute><#if toJavaType(attribute.type) == 'LocalDate' || toJavaType(attribute.type) == 'LocalTime' || toJavaType(attribute.type) == 'LocalDateTime' >
    // filter by ${attribute.name}?
    if (filter.${toGetter(attribute)}() != null) {
      Path<${toJavaType(attribute.type)}> ${attribute.name} = ${model.name?uncap_first}.get("${attribute.name}");
      if (filter.${toGetter(attribute)}().getStart() != null) {
        where.add(cb.greaterThanOrEqualTo(${attribute.name}, filter.${toGetter(attribute)}().getStart()));
      }
      if (filter.${toGetter(attribute)}().getEnd() != null) {
        where.add(cb.lessThanOrEqualTo(${attribute.name}, filter.${toGetter(attribute)}().getEnd()));
      }
    }
    <#elseif isModel(attribute.type) || isEnum(attribute.type) || attribute.name == 'id'>
    // filter by ${attribute.name}?
    if (filter.${toPlural(toGetter(attribute))}() == null) {
      // ignore this filter
    } else if (filter.${toPlural(toGetter(attribute))}().isEmpty()) {
      return new ResultList<>(Collections.EMPTY_LIST, ${model.name}.class, 0, 0, 0);
    } else {
      Path<${toJavaType(attribute.type)}> ${attribute.name} = ${model.name?uncap_first}.get("${attribute.name}");
      where.add(${attribute.name}.in(filter.${toPlural(toGetter(attribute))}()));
    }
    <#elseif toJavaType(attribute.type) == 'String'>
    // filter by ${attribute.name}?
    if (filter.${toGetter(attribute)}() != null) {
      Path<${toJavaType(attribute.type)}> ${attribute.name} = ${model.name?uncap_first}.get("${attribute.name}");
      where.add(cb.like(${attribute.name}, filter.${toGetter(attribute)}()));
    }
    <#else>
    // filter by ${attribute.name}?
    if (filter.${toGetter(attribute)}() != null) {
      Path<${toJavaType(attribute.type)}> ${attribute.name} = ${model.name?uncap_first}.get("${attribute.name}");
      where.add(cb.equal(${attribute.name}, filter.${toGetter(attribute)}()));
    }
    </#if><#if attribute.name != "id">
    if (filter.${toGetter(attribute)}IsNull() != null) {
      Path<${toJavaType(attribute.type)}> ${attribute.name} = ${model.name?uncap_first}.get("${attribute.name}");
      if (filter.${toGetter(attribute)}IsNull()) {
        where.add(cb.isNull(${attribute.name}));
      } else {
        where.add(cb.isNotNull(${attribute.name}));
      }
    }</#if>
    </#list>

    // add where clause
    if (!where.isEmpty()) {
      cq.where(cb.and(where.toArray(new Predicate[where.size()])));
    }

    // add order
    if (filter.getOrder() != Order.UNSET || filter.getOrderBy() != null) {
      Path orderField = ${model.name?uncap_first}.get(filter.getOrderBy());

      if (filter.getOrder() == Order.ASC) {
        cq.orderBy(cb.asc(orderField));
      }

      if (filter.getOrder() == Order.DESC) {
        cq.orderBy(cb.desc(orderField));
      }
    }

    // compile query
    TypedQuery<${model.name}> typedQuery = em.createQuery(cq);
    List<${model.name}> unfilteredResult = typedQuery.getResultList();

    // get result list as sublist of all results
    int total = unfilteredResult.size();
    int offset = filter.getOffset();
    int limit = filter.getLimit() == -1 ? total : filter.getLimit();

    ResultList<${model.name}> resultList;
    if (offset > total) {
      resultList = new ResultList<>(Collections.EMPTY_LIST, ${model.name}.class,
              total, offset, limit);
    } else {
      int lowerBound = offset;
      int upperBound = (offset + limit) > total ? total : (offset + limit);
      resultList = new ResultList<>(unfilteredResult.subList(lowerBound, upperBound),
              ${model.name}.class, total, offset, limit);
    }

    // apply on load
    for (${model.name} i : resultList) {
      onLoad(i);
    }

    // build result list
    return resultList;
  }
}