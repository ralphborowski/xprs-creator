/**
 * This software was created with xprs.
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.filter;

import java.util.LinkedList;
import java.util.List;
import rocks.xprs.db.Range;<#if !model.inheritsFrom?? >
import rocks.xprs.db.EntityFilter;</#if><#list model.types as type><#if toCanonicalClassName(type) != "">
import ${toCanonicalClassName(type)};
</#if></#list>

/**
 * Filter class for ${model.name}
 */
public abstract class Abstract${model.name?cap_first}Filter extends <#if model.inheritsFrom?? >${toJavaType(model.inheritsFrom)}Filter<#else>EntityFilter</#if> {

  private static final long serialVersionUID = 1L;

  <#list model.attributes as attribute>
  <#if toJavaType(attribute.type) == 'Date' || toJavaType(attribute.type) == 'LocalDate' || toJavaType(attribute.type) == 'LocalTime' || toJavaType(attribute.type) == 'LocalDateTime'>private Range<${toJavaType(attribute.type)}> ${attribute.name};<#elseif isModel(attribute.type)>private List<? extends ${toJavaType(attribute.type)}>  ${toPlural(attribute.name)};<#elseif isEnum(attribute.type) || attribute.name == 'id'>private List<${toJavaType(attribute.type)}> ${toPlural(attribute.name)};<#else>private ${toJavaType(attribute.type)} ${attribute.name};</#if><#if attribute.name != 'id'>
  private Boolean ${attribute.name}IsNull;</#if>
  </#list>

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters"><#list model.attributes as attribute>
  /**
   * Getter for ${attribute.name}
   * @return the ${attribute.name}
   */
  public <#if toJavaType(attribute.type) == 'Date' || toJavaType(attribute.type) == 'LocalDate' || toJavaType(attribute.type) == 'LocalTime' || toJavaType(attribute.type) == 'LocalDateTime'>Range<${toJavaType(attribute.type)}><#elseif isModel(attribute.type)>List<? extends ${toJavaType(attribute.type)}><#elseif isEnum(attribute.type) || attribute.name == 'id'>List<${toJavaType(attribute.type)}><#else>${toJavaType(attribute.type)}</#if> <#if isModel(attribute.type) || isEnum(attribute.type) || attribute.name == 'id'>${toPlural(toGetter(attribute))}<#else>${toGetter(attribute)}</#if>() {
    return <#if isModel(attribute.type) || isEnum(attribute.type) || attribute.name == 'id'>${toPlural(attribute.name)}<#else>${attribute.name}</#if>;
  }

  /**
   * Setter for ${attribute.name}
   * @param ${attribute.name} the ${attribute.name} to set
   */
  public void set${attribute.name?cap_first}(<#if toJavaType(attribute.type) == 'Date'  || toJavaType(attribute.type) == 'LocalDate' || toJavaType(attribute.type) == 'LocalTime' || toJavaType(attribute.type) == 'LocalDateTime'>Range<${toJavaType(attribute.type)}><#else>${toJavaType(attribute.type)}</#if> ${attribute.name}) {
    <#if isModel(attribute.type) || isEnum(attribute.type) || attribute.name == 'id'>List<${toJavaType(attribute.type)}> list = new LinkedList<>();
    list.add(${attribute.name});
    ${toPlural(attribute.name)} = list;<#else>this.${attribute.name} = ${attribute.name};</#if>
  }<#if isModel(attribute.type) || isEnum(attribute.type) || attribute.name == 'id'>

  /**
   * Setter for ${attribute.name}
   * @param ${toPlural(attribute.name)} the ${toPlural(attribute.name)} to set
   */
  public void set${toPlural(attribute.name?cap_first)}(<#if isModel(attribute.type)>List<? extends ${toJavaType(attribute.type)}<#else>List<${toJavaType(attribute.type)}</#if>> ${toPlural(attribute.name)}) {
    this.${toPlural(attribute.name)} = ${toPlural(attribute.name)};
  }</#if>

  <#if attribute.name != 'id'>

  /**
   * Getter for ${attribute.name}'s null values
   * @return if only null values or non-null values should be queried, returns null if not set
   */
  public Boolean ${toGetter(attribute)}IsNull() {
    return ${attribute.name}IsNull;
  }

  /**
   * Setter for ${attribute.name}'s null values
   * @param ${attribute.name}IsNull set to true to get only results where ${attribute.name}
   *   is null or to false to get results where ${attribute.name} is not null.
   */
  public void set${attribute.name?cap_first}IsNull(Boolean ${attribute.name}IsNull) {<#if isModel(attribute.type) || isEnum(attribute.type)>
    this.${toPlural(attribute.name)} = null;<#else>
    this.${attribute.name} = null;</#if>
    this.${attribute.name}IsNull = ${attribute.name}IsNull;
  }
  </#if>
  </#list>
  //</editor-fold>
}