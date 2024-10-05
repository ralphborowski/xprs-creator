<#ftl strip_whitespace = false><${r"#"}-- 
  
  BASIC INPUT FORM FOR ${model.name?upper_case}

  requires to set the following variables
  * ${model.name?uncap_first}<#list model.allTypes as type><#if isModel(type)>
  * ${toPlural(type.name?uncap_first)}</#if></#list>

-->
<#list model.allAttributes as attribute><#if attribute.name != 'createDate' && attribute.name != 'createUser' && attribute.name != 'modifyDate' && attribute.name != 'modifyUser' && attribute.primaryKey >
<input type="hidden" name="${model.name?uncap_first}.${attribute.name}" value="${r"${"}(data.${model.name?uncap_first}.${attribute.name})!}" /></#if></#list><#list model.allAttributes as attribute><#if attribute.name != 'createDate' && attribute.name != 'createUser' && attribute.name != 'modifyDate' && attribute.name != 'modifyUser' && !attribute.primaryKey >
<#if isModel(attribute.type)>
${r"<#if"} !data.${toPlural(attribute.type.name?uncap_first)}?? >
  <input type="hidden" name="${model.name?uncap_first}.${attribute.name}" value="${r"${"}(data.${model.name?uncap_first}.${attribute.name})!}" />
${r"<#else>"}
  <div class="form-group${r"<#if"} (data.errors.${model.name?uncap_first}.${attribute.name})??> has-error${r"</#if>"}">
    <label for="${model.name?uncap_first}.${attribute.name}">${attribute.name?cap_first}</label>
    <select id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control" <#if attribute.required> required</#if>>
      <option value=""></option>${r"<#list "}data.${toPlural(attribute.type.name?uncap_first)} as item>
      <option value="${r"${item.id}"}"${r"<#if"} ((data.${model.name?uncap_first}.${attribute.name}.id)!0) = item.id> selected${r"</#if>"}>${r"${item.toLabel()}"}</option>${r"</#list>"}
    </select>
    ${r"<#if"} (data.errors.${model.name?uncap_first}.${attribute.name})??><p class="help-block">${r"${"}data.errors.${model.name?uncap_first}.${attribute.name}}</p>${r"</#if>"}
  </div>
${r"</#if>"}<#else>
<div class="form-group${r"<#if"} (data.errors.${model.name?uncap_first}.${attribute.name})??> has-error${r"</#if>"}">
  <#if attribute.type.name = 'Boolean'><div class="checkbox"><label for="${model.name?uncap_first}.${attribute.name}"><input type="checkbox" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" value="true" class="form-control" ${r"<#if ${"}(${model.name?uncap_first}.${attribute.name})!"false" } > checked${r"</#if>"}> ${attribute.name?cap_first}</label></div>
  <#else><label for="${model.name?uncap_first}.${attribute.name}">${attribute.name?cap_first}</label>
  <#if attribute.type.name = 'String' && attribute.maxlength = 0><textarea id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control"<#if attribute.required> required</#if>>${r"${"}(${model.name?uncap_first}.${attribute.name})!}</textarea>
  <#elseif attribute.type.name = 'String'><input type="text" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" value="${r"${"}(data.${model.name?uncap_first}.${attribute.name})!}" class="form-control" maxlength="${attribute.maxlength}"<#if attribute.required> required</#if><#if attribute.matches??> pattern="${attribute.matches}"</#if>>
  <#elseif attribute.type.name = 'Char'><input type="text" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" value="${r"${"}(data.${model.name?uncap_first}.${attribute.name})!}" class="form-control" maxlength="1"<#if attribute.required> required</#if><#if attribute.matches??> pattern="${attribute.matches}"</#if>>
  <#elseif attribute.type.name = 'Integer' || attribute.type.name = 'Long'><input type="number" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" value="${r"${"}(data.${model.name?uncap_first}.${attribute.name})!}" class="form-control" <#if attribute.required> required</#if>>
  <#elseif attribute.type.name = 'Float' || attribute.type.name = 'Double'><input type="number" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" value="${r"${"}(data.${model.name?uncap_first}.${attribute.name})!}" class="form-control" step="any" pattern="<#if attribute.matches??>${attribute.matches}<#else>-?[0-9]+([\,|\.][0-9]+)?</#if>"<#if attribute.required> required</#if>>
  <#elseif attribute.type.name = 'Date'><input type="date" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" value="${r"${"}data.${model.name?uncap_first}.${attribute.name}?date}" class="form-control" <#if attribute.required> required</#if>>
  <#elseif attribute.type.name = 'Time'><input type="time" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" value="${r"${"}data.${model.name?uncap_first}.${attribute.name}?time}" class="form-control" <#if attribute.required> required</#if>>
  <#elseif attribute.type.name = 'DateTime'><input type="datetime" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" value="${r"${"}data.${model.name?uncap_first}.${attribute.name}?datetime}" class="form-control" <#if attribute.required> required</#if>>
  <#elseif isEnum(attribute.type)><select id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control" <#if attribute.required> required</#if>>
    <option value=""></option><#list attribute.type.options as option>
    <option value="${option}"${r"<#if"} (data.${model.name?uncap_first}.${attribute.name})!"" = option> selected${r"</#if>"}>${option}</option></#list>
  </select>
  </#if></#if>${r"<#if"} (data.errors.${model.name?uncap_first}.${attribute.name})??><p class="help-block">${r"${"}data.errors.${model.name?uncap_first}.${attribute.name}}</p>${r"</#if>"}
</div></#if></#if></#list>