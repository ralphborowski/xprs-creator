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
CREATE TABLE ${model.name?upper_case} (<#list model.allAttributes as attribute><#if !attribute?is_first >,</#if><#if !attribute.isTransient() && !isEnum(attribute.type) && attribute.type.name != "Monetary" >
  ${attribute.name?upper_case}<#if isModel(attribute.type)>_ID</#if> <#switch attribute.type.name>
    <#case "String"><#if attribute.maxlength gt 0>VARCHAR(${attribute.maxlength})<#else>TEXT</#if><#break>
    <#case "Character">CHAR<#break>
    <#case "Integer">INT<#break>
    <#case "Long">BIGINT<#break>
    <#case "Float">FLOAT<#break>
    <#case "Double">DOUBLE<#break>
    <#case "Boolean">TINYINT<#break>
    <#case "DateTime">DATETIME<#break>
    <#case "Date">DATETIME<#break>
    <#case "Time">DATETIME<#break>
    <#case "Decimal">DECIMAL(16,4)<#break>
    <#default>BIGINT<#break>
  </#switch> <#if attribute.isPrimaryKey()>PRIMARY KEY NOT NULL<#else><#if attribute.getDefaultValue()??>DEFAULT ${attribute.getDefaultValue()}<#elseif attribute.isUnique()>NOT NULL<#else>DEFAULT NULL</#if></#if><#elseif isEnum(attribute.type)>
  ${attribute.name?upper_case} VARCHAR(50) DEFAULT 'UNSET'<#elseif attribute.type.name == "Monetary">
  ${attribute.name?upper_case}_AMOUNT DECIMAL(16,4) DEFAULT NULL,
  ${attribute.name?upper_case}_CURRENCY VARCHAR(5) DEFAULT NULL</#if></#list>
);
<#list model.allAttributes as attribute><#if isModel(attribute.type) || attribute.isUnique() >

CREATE<#if attribute.isUnique()> UNIQUE</#if> INDEX IX_${model.name?upper_case}_${attribute.name?upper_case} ON ${model.name?upper_case}(${attribute.name?upper_case}<#if isModel(attribute.type)>_ID</#if>);</#if></#list>

</#list>

--
-- SEQUENCE TABLE
--
CREATE TABLE SEQUENCE(
  SEQ_NAME VARCHAR(50) NOT NULL,
  SEQ_COUNT BIGINT DEFAULT 0
);

INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) VALUES ('itemCounter', 0);
