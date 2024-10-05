<#ftl strip_whitespace = false>/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */
package ${context.project.basePackage}.gui;

import ${context.project.basePackage}.model.*;
import org.apache.wicket.Localizer;
import org.apache.wicket.Session;

/**
 * Generates default labels used for each entity, for example in drop down lists, 
 * lists or details views.
 */
public class AbstractLabeler {

  protected final Localizer localizer = Application.get()
          .getResourceSettings().getLocalizer();
          
  protected AbstractLabeler() {
  
  }
  
  /**
   * Looks up a resource and applies a String.format() template.
   * @param key the resource to look up the template
   * @param values the replacements
   * @returns a localized resource string
   */
  public String buildString(String key, Object... values) {
    String pattern = localizer.getString(key, null);
    return String.format(Session.get().getLocale(), pattern, values);
  }<#list project.models as model>
  
  /**
   * Creates a label for ${model.name?uncap_first}s.
   * @param ${model.name?uncap_first} the ${model.name?uncap_first}
   * @returns a localized label
   */
  public String for${model.name}(${model.name} ${model.name?uncap_first}) {<#if model.hasAttribute("name")>
    return ${model.name?uncap_first}.getName();<#elseif model.hasAttribute("firstname") && model.hasAttribute("lastname")>
    return<#if model.hasAttribute("title")> ${model.name?uncap_first}.getTitle() + " " + </#if> ${model.name?uncap_first}.getFirstname() + " " + ${model.name?uncap_first}.getLastname()<#if model.hasAttribute("company")> + "(" + ${model.name?uncap_first}.getCompany() + ")"</#if>;<#elseif model.hasAttribute("title")>
    return ${model.name?uncap_first}.getTitle();<#else>
    return buildString("${model.name?uncap_first}.model.label", ${model.name?uncap_first}.getId());</#if>
  }</#list>
}