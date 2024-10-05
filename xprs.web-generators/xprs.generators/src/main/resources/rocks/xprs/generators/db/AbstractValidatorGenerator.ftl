<#list model.allAttributes as attribute><#if attribute.required || attribute.matches??><#assign hasValidators="true"></#if></#list>/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.validator;

import ${toCanonicalClassName(model)};
import rocks.xprs.db.EntityValidator;<#if hasValidators?? >
import java.util.ArrayList;</#if>
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validator class for ${model.name}
 */
public abstract class Abstract${model.name?cap_first}Validator implements EntityValidator<${model.name}> {
   
  @Override
  public Map<String, List<String>> validate(${model.name} ${model.name?uncap_first}) {
    
    <#if hasValidators?? >  
    // init error map
    HashMap<String, List<String>> errors = new HashMap<>();
    
    List<String> errorMessages;
    
    <#list model.allAttributes as attribute><#if attribute.required || attribute.matches??>
    // check ${attribute.name}
    errorMessages = new ArrayList<>();
    <#if attribute.required >
    if (${model.name?uncap_first}.${toGetter(attribute)}() == null) {
      errorMessages.add("${model.name?uncap_first}.${attribute.name}.errors.required");
    }
    
    </#if>
    <#if attribute.matches?? >
    if (!${model.name?uncap_first}.${toGetter(attribute)}().matches("${attribute.matches}")) {
      errorMessages.add("${attribute.name}.errors.format");
    }
    
    </#if>
    if (!errorMessages.isEmpty()) {
      errors.put("${attribute.name}", errorMessages);
    }
    
    </#if></#list>
    // return validation result
    return errors;
    <#else>
    return new HashMap<>();
    </#if>
  }
}