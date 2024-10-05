<#ftl strip_whitespace = false><!--

  BASIC INPUT FORM FOR ${model.name?upper_case}

-->
<wicket:panel>
  <#list model.allAttributes as attribute><#if attribute.name != 'createDate' && attribute.name != 'createUser' && attribute.name != 'modifyDate' && attribute.name != 'modifyUser' && !attribute.primaryKey >
  <div class="form-group" wicket:id="${attribute.name}Panel"><#if attribute.type.name = 'Boolean'>
    <div class="checkbox">
      <label for="${model.name?uncap_first}.${attribute.name}">
      <input type="checkbox" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" value="true" class="form-control" wicket:id="${attribute.name}">
      <wicket:message key="${model.name?uncap_first}.attributes.${attribute.name}.label">${attribute.name?cap_first}</wicket:message>
      </label>
    </div><#else>
    <label for="${model.name?uncap_first}.${attribute.name}">
      <wicket:message key="${model.name?uncap_first}.attributes.${attribute.name}.label">${attribute.name?cap_first}</wicket:message>
    </label><#if attribute.type.name = 'String' && attribute.maxlength = 0>
    <textarea id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control" wicket:id="${attribute.name}"></textarea><#elseif attribute.type.name = 'String'>
    <input type="text" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control" wicket:id="${attribute.name}" maxlength="${attribute.maxlength}"<#if attribute.required> required</#if><#if attribute.matches??> pattern="${attribute.matches}"</#if>><#elseif attribute.type.name = 'Char'>
    <input type="text" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control" wicket:id="${attribute.name}" maxlength="1"<#if attribute.required> required</#if><#if attribute.matches??> pattern="${attribute.matches}"</#if>><#elseif attribute.type.name = 'Integer' || attribute.type.name = 'Long'>
    <input type="number" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control" wicket:id="${attribute.name}" <#if attribute.required> required</#if>><#elseif attribute.type.name = 'Float' || attribute.type.name = 'Double' || attribute.type.name = 'Decimal'>
    <input type="number" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control" wicket:id="${attribute.name}" step="any" <#if attribute.matches??>pattern="${attribute.matches}"</#if><#if attribute.required> required</#if>><#elseif attribute.type.name = 'Date'>
    <input type="text" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control" wicket:id="${attribute.name}" <#if attribute.required> required</#if>><#elseif attribute.type.name = 'Time'>
    <input type="text" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control" wicket:id="${attribute.name}" <#if attribute.required> required</#if>><#elseif attribute.type.name = 'DateTime'>
    <input type="text" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control" wicket:id="${attribute.name}" <#if attribute.required> required</#if>><#elseif attribute.type.name = 'File'>
    <div wicket:id="${attribute.name}.filename"></div>
    <input type="file" id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control" wicket:id="${attribute.name}Upload" <#if attribute.required> required</#if>><#elseif isEnum(attribute.type)>
    <select id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control" wicket:id="${attribute.name}" <#if attribute.required> required</#if>></select><#elseif isModel(attribute.type)>
    <select id="${model.name?uncap_first}.${attribute.name}" name="${model.name?uncap_first}.${attribute.name}" class="form-control" wicket:id="${attribute.name}" <#if attribute.required> required</#if>></select></#if></#if>
  </div>
  </#if></#list>
</wicket:panel>