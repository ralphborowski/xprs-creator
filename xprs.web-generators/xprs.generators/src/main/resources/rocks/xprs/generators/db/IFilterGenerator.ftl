/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.filter;
<#list model.types as type><#if toCanonicalClassName(type) != "">
import <#if toCanonicalClassName(type) == 'java.util.Date'>rocks.xprs.runtime.db.TimeRange<#else>${toCanonicalClassName(type)}</#if>;
</#if></#list><#if !model.inheritsFrom?? >import rocks.xprs.runtime.db.EntityFilter;</#if>

/**
 * Filter interface for ${model.name}
 */
public interface I${model.name?cap_first}Filter extends <#if model.inheritsFrom?? >I${toJavaType(model.inheritsFrom)}Filter<#else>IEntityFilter</#if> {

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters"><#list model.attributes as attribute>
  /**
   * Getter for ${attribute.name}
   * @return the ${attribute.name}
   */
  public <#if toJavaType(attribute.type) == 'Date'>TimeRange<#else>${toJavaType(attribute.type)}</#if> ${toGetter(attribute)}();
  
  /**
   * Setter for ${attribute.name}
   * @param ${attribute.name} the ${attribute.name} to set
   */
  public void set${attribute.name?cap_first}(<#if toJavaType(attribute.type) == 'Date'>TimeRange<#else>${toJavaType(attribute.type)}</#if> ${attribute.name});
  </#list>
  //</editor-fold>
}