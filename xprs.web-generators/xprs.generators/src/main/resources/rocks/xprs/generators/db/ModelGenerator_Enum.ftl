/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.model;


/**
 * Enum for ${enum.name?uncap_first}.
 */
public enum ${enum.name?cap_first} {

  <#list enum.options as option>
    ${option}<#sep>, </#sep>
  </#list>
  
}