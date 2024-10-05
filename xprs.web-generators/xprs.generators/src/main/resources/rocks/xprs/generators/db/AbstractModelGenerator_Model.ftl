/**
 * This software was created with xprs.
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.model;

<#list model.types as type><#if toCanonicalClassName(type) != '' && (!toCanonicalClassName(type)?starts_with(model.project.basePackage)) >
import ${toCanonicalClassName(type)};
</#if></#list><#if !model.inheritsFrom?? >import rocks.xprs.db.Entity;</#if>

/**
 * Model class for ${model.name}.
 */
public abstract class Abstract${model.name?cap_first} <#if model.inheritsFrom?? >extends ${toJavaType(model.inheritsFrom)}<#else>implements Entity</#if> {

  private static final long serialVersionUID = 1L;

<#list model.attributes as attribute>  private ${toJavaType(attribute.type)} ${attribute.name};
</#list>

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters"><#list model.attributes as attribute>
  /**
   * Getter for ${attribute.name}
   * @return the ${attribute.name}
   */<#if attribute.name == 'id' || attribute.name == 'createUser' || attribute.name == 'createDate' || attribute.name == 'modifyUser' || attribute.name == 'modifyDate'>
  @Override</#if>
  public ${toJavaType(attribute.type)} ${toGetter(attribute)}() {
    return ${attribute.name};
  }

  /**
   * Setter for ${attribute.name}
   * @param ${attribute.name} the ${attribute.name} to set
   */<#if attribute.name == 'id' || attribute.name == 'createUser' || attribute.name == 'createDate' || attribute.name == 'modifyUser' || attribute.name == 'modifyDate'>
  @Override</#if>
  public void set${attribute.name?cap_first}(${toJavaType(attribute.type)} ${attribute.name}) {
    this.${attribute.name} = ${attribute.name};
  }
  </#list>
  //</editor-fold>
}