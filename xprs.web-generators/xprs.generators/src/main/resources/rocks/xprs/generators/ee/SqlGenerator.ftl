--
--  DATABASE SCHEMA FOR ${project.projectName?upper_case}
--  For use with MySQL or compatible SQL dialects.
--
--  This software was created with xprs.
--  Have a look at https://xprs.rocks/ for more details.
--

<#list sqlModels as model >

--
-- ${model.name?upper_case} TABLE
--
CREATE TABLE ${model.name?upper_case} (
  ID BIGINT PRIMARY KEY NOT NULL,
  CREATEUSER VARCHAR(50) DEFAULT NULL,
  CREATEDATE DATETIME DEFAULT NULL,
  MODIFYUSER VARCHAR(50) DEFAULT NULL,
  MODIFYDATE DATETIME DEFAULT NULL,<#list model.allAttributes as attribute><#if !attribute?is_first >,</#if><#if !attribute.isTransient() && !isEnum(attribute.type) && attribute.type.name != "Monetary" >
  ${attribute.name?upper_case}<#if isModel(attribute.type)>_ID</#if> <#switch attribute.type.name>
    <#case "String"><#if attribute.maxlength gt 0>VARCHAR(${attribute.maxlength})<#else>TEXT</#if><#break>
    <#case "Character">CHAR<#break>
    <#case "Integer">INT<#break>
    <#case "Long">BIGINT<#break>
    <#case "Float">FLOAT<#break>
    <#case "Double">DOUBLE<#break>
    <#case "Boolean">TINYINT<#break>
    <#case "DateTime">DATETIME<#break>
    <#case "Date">DATE<#break>
    <#case "Time">TIME<#break>
    <#case "LocalDateTime">DATETIME<#break>
    <#case "LocalDate">DATE<#break>
    <#case "LocalTime">TIME<#break>
    <#case "Decimal">DECIMAL(16,4)<#break>
    <#default>BIGINT<#break>
  </#switch> <#if attribute.isPrimaryKey()>PRIMARY KEY NOT NULL<#else><#if attribute.getDefaultValue()??>DEFAULT ${attribute.getDefaultValue()}<#elseif attribute.isUnique()>NOT NULL<#else>DEFAULT NULL</#if></#if><#elseif isEnum(attribute.type)>
  ${attribute.name?upper_case} VARCHAR(50) DEFAULT 'UNSET'<#elseif attribute.type.name == "Monetary">
  ${attribute.name?upper_case}_AMOUNT DECIMAL(16,4) DEFAULT NULL,
  ${attribute.name?upper_case}_CURRENCY VARCHAR(5) DEFAULT NULL</#if></#list>
);
CREATE SEQUENCE ${model.name?uncap_first}_id_seq START WITH 1 INCREMENT BY 1;

<#list model.allAttributes as attribute><#if isModel(attribute.type) || attribute.isUnique() >

CREATE<#if attribute.isUnique()> UNIQUE</#if> INDEX IX_${model.name?upper_case}_${attribute.name?upper_case} ON ${model.name?upper_case}(${attribute.name?upper_case}<#if isModel(attribute.type)>_ID</#if>);</#if></#list>

</#list>