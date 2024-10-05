<#ftl strip_whitespace = false>/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.gui.modules;

import ${toCanonicalClassName(model)};<#list model.allTypes as type><#if isEnum(type)>
import ${toCanonicalClassName(type)};</#if></#list>
import ${toCanonicalDaoClassName(model)};<#list model.allTypes as type><#if toCanonicalDaoClassName(type) != "">
import ${toCanonicalDaoClassName(type)};</#if></#list>
import ${toCanonicalValidatorClassName(model)};
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import rocks.xprs.runtime.db.Validator;
import rocks.xprs.runtime.exceptions.ResourceNotFoundException;
import rocks.xprs.runtime.web.FreemarkerRenderingResponse;
import rocks.xprs.runtime.web.Module;
import rocks.xprs.runtime.web.RequestContext;
import rocks.xprs.runtime.web.Response;

/**
 * Renders a input form for ${model.name?uncap_first}.
 */
public class Abstract${model.name}EditModule implements Module {

  @Override
  public Response process(RequestContext context) throws ResourceNotFoundException {
    
    try (${toPlural(model.name)} ${toPlural(model.name?uncap_first)} = new ${toPlural(model.name)}(context.getEntityManager());<#list model.allTypes as type><#if toCanonicalDaoClassName(type) != "">
            ${toPlural(type.name)} ${toPlural(type.name?uncap_first)} = new ${toPlural(type.name)}(context.getEntityManager());</#if></#list>) {
      
      // get request
      HttpServletRequest request = context.getRequest();
            
      // prepare result map
      Map<String, Object> data = new HashMap<>();
      
      // get the entity from database or create an empty one
      ${model.name} ${model.name?uncap_first};
      if (Validator.isInteger(context.getPathParameter("${model.name?uncap_first}"))) {
        ${model.name?uncap_first} = ${toPlural(model.name?uncap_first)}.get(Long.parseLong(
                context.getPathParameter("${model.name?uncap_first}")));
      } else if (Validator.isInteger(request.getParameter("id"))) {
        ${model.name?uncap_first} = ${toPlural(model.name?uncap_first)}.get(Long.parseLong(
                request.getParameter("id")));
      } else {
        ${model.name?uncap_first} = new ${model.name}();
      }
      
      // change data?    
      if (context.isPostback()) {
        
        ${model.name} userInput = new ${model.name}();
        userInput.setId(${model.name?uncap_first}.getId());<#list model.allAttributes as attribute><#if attribute.name != 'id' && attribute.name != 'createUser' && attribute.name != 'createDate' && attribute.name != 'modifyUser' && attribute.name != 'modifyDate'>
        <#if attribute.type.name == 'String'>userInput.set${attribute.name?cap_first}(request.getParameter("${model.name?uncap_first}.${attribute.name}"));
        <#elseif attribute.type.name == 'Char'>if (Validator.notEmpty(request.getParameter("${model.name?uncap_first}.${attribute.name}"))) {
          userInput.set${attribute.name?cap_first}(request.getParameter("${model.name?uncap_first}.${attribute.name}").charAt(0));
        }
        <#elseif attribute.type.name == 'Integer'>if (Validator.isInteger(request.getParameter("${model.name?uncap_first}.${attribute.name}"))) {
          userInput.set${attribute.name?cap_first}(Integer.parseInt(request.getParameter("${model.name?uncap_first}.${attribute.name}")));
        }
        <#elseif attribute.type.name == 'Long'>if (Validator.isInteger(request.getParameter("${model.name?uncap_first}.${attribute.name}"))) {
          userInput.set${attribute.name?cap_first}(Long.parseLong(request.getParameter("${model.name?uncap_first}.${attribute.name}")));
        }
        <#elseif attribute.type.name == 'Float'>if (Validator.isDecimal(request.getParameter("${model.name?uncap_first}.${attribute.name}"))) {
          userInput.set${attribute.name?cap_first}(Float.parseFloat(request.getParameter("${model.name?uncap_first}.${attribute.name}")));
        }
        <#elseif attribute.type.name == 'Double'>if (Validator.isDecimal(request.getParameter("${model.name?uncap_first}.${attribute.name}"))) {
          userInput.set${attribute.name?cap_first}(Double.parseDouble(request.getParameter("${model.name?uncap_first}.${attribute.name}")));
        }
        <#elseif attribute.type.name == 'Boolean'>if (Validator.notEmpty(request.getParameter("${model.name?uncap_first}.${attribute.name}"))) {
          userInput.set${attribute.name?cap_first}(Boolean.parseBoolean(request.getParameter("${model.name?uncap_first}.${attribute.name}")));
        }
        <#elseif attribute.type.name == 'Date'>if (Validator.isDate(request.getParameter("${model.name?uncap_first}.${attribute.name}"))) {
          try {
            userInput.set${attribute.name?cap_first}(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("${model.name?uncap_first}.${attribute.name}")));
          } catch (ParseException ex) {
            Logger.getLogger(${model.name}EditModule.class.getName()).log(Level.WARNING, null, ex);
          }
        }
        <#elseif attribute.type.name == 'Time'>if (Validator.isTime(request.getParameter("${model.name?uncap_first}.${attribute.name}"))) {
          try {
            userInput.set${attribute.name?cap_first}(new SimpleDateFormat("HH:mm").parse(request.getParameter("${model.name?uncap_first}.${attribute.name}")));
          } catch (ParseException ex) {
            Logger.getLogger(${model.name}EditModule.class.getName()).log(Level.WARNING, null, ex);
          }
        }
        <#elseif attribute.type.name == 'DateTime'>if (Validator.isDateTime(request.getParameter("${model.name?uncap_first}.${attribute.name}"))) {
          try {
            userInput.set${attribute.name?cap_first}(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(request.getParameter("${model.name?uncap_first}.${attribute.name}")));
          } catch (ParseException ex) {
            Logger.getLogger(${model.name}EditModule.class.getName()).log(Level.WARNING, null, ex);
          }
        }
        <#elseif isEnum(attribute.type)>if (Validator.notEmpty(request.getParameter("${model.name?uncap_first}.${attribute.name}"))) {
          userInput.set${attribute.name?cap_first}(${attribute.type.name}.valueOf(request.getParameter("${model.name?uncap_first}.${attribute.name}")));
        }
        <#else>if (Validator.isInteger(context.getPathParameter("${attribute.name}"))) {
          userInput.set${attribute.name?cap_first}(${toPlural(attribute.type.name?uncap_first)}.get(
                  Long.parseLong(context.getPathParameter("${attribute.name}"))));
        } else if (Validator.isInteger(request.getParameter("${model.name?uncap_first}.${attribute.name}"))) {
          userInput.set${attribute.name?cap_first}(${toPlural(attribute.type.name?uncap_first)}.get(
                  Long.parseLong(request.getParameter("${model.name?uncap_first}.${attribute.name}"))));
        }
        </#if></#if></#list>
        
        // validate
        ${model.name}Validator ${model.name?uncap_first}Validator = new ${model.name}Validator();
        Map<String, List<String>> errors = ${model.name?uncap_first}Validator.validate(userInput);
        
        // validation successful?
        if (errors.isEmpty()) {
          
          ${model.name?uncap_first}.mergeWith(userInput);
          
          // update meta data
          Date now = new Date();
          if (${model.name?uncap_first}.getCreateDate() == null) {
            ${model.name?uncap_first}.setCreateDate(now);
            ${model.name?uncap_first}.setCreateUser(
                    context.getUser() != null 
                    ? context.getUser().getUsername() : null);
          }
          
          ${model.name?uncap_first}.setModifyDate(now);
          ${model.name?uncap_first}.setModifyUser(
                    context.getUser() != null 
                    ? context.getUser().getUsername() : null);
          
          // save account
          ${model.name?uncap_first} = ${toPlural(model.name?uncap_first)}.save(${model.name?uncap_first});
        } else {
        
          // show inputs and errors
          ${model.name?uncap_first} = userInput;
          data.put("errors", errors);
        }
      }
      
      // add ${model.name?uncap_first} to view
      data.put("${model.name?uncap_first}", ${model.name?uncap_first});<#list model.allTypes as type><#if isModel(type)>
      
      if (context.getPathParameter("${type.name?uncap_first}") == null) {
        data.put("${toPlural(type.name?uncap_first)}", ${toPlural(type.name?uncap_first)}.list());
      }</#if></#list>
      
      Map<String, Object> renderContext = new HashMap<>();
      renderContext.put("context", context);
      renderContext.put("data", data);
      
      return new FreemarkerRenderingResponse(this, "${model.name}EditModule", renderContext);
    }
  }
}