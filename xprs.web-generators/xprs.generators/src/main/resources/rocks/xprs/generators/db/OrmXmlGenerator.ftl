<?xml version="1.0" encoding="UTF-8"?>

<!--
  OR-Mapping file for ${project.projectName}

  This software was created with xprs.
  Have a look at https://xprs.rocks/ for more details.

-->

<entity-mappings xmlns="http://java.sun.com/xml/ns/persistence/orm"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://java.sun.com/xml/ns/persistence/orm http://java.sun.com/xml/ns/persistence/orm_1_0.xsd"
                 version="1.0">

  <description>OR mapping file for ${project.projectName}</description>
  <package>${project.basePackage}.model</package>

  <embeddable class="rocks.xprs.types.Monetary">
    <attributes>
      <basic name="amount" />
      <basic name="currency" />
    </attributes>
  </embeddable>

  <#list project.models as model >
  <mapped-superclass class="${model.project.basePackage}.model.Abstract${model.name}">
    <attributes>
    <#list model.attributes as attribute><#if attribute.isPrimaryKey() >
      <id name="${attribute.name}">
        <generated-value strategy="TABLE" generator="itemCounter" />
        <table-generator name="itemCounter" initial-value="1" allocation-size="1" />
      </id>
    </#if></#list>
    <#list model.attributes as attribute><#if (isPrimitiveType(attribute.type)) && !attribute.isPrimaryKey() && !attribute.isTransient() >
      <#switch attribute.type.name>
      <#case "LDateTime">
      <basic name="${attribute.name}">
        <temporal>TIMESTAMP</temporal>
      </basic>
      <#break>
      <#case "LDate">
      <basic name="${attribute.name}">
        <temporal>DATE</temporal>
      </basic>
      <#break>
      <#case "LTime">
      <basic name="${attribute.name}">
        <temporal>TIME</temporal>
      </basic>
      <#break>
      <#case "Monetary">
      <embedded name="${attribute.name}">
        <attribute-override name="amount">
          <column name="${attribute.name?upper_case}_AMOUNT" />
        </attribute-override>
        <attribute-override name="currency">
          <column name="${attribute.name?upper_case}_CURRENCY" />
        </attribute-override>
      </embedded>
      <#break>
      <#default>
      <basic name="${attribute.name}" />
      <#break>
    </#switch>
    </#if></#list><#list model.attributes as attribute><#if isEnum(attribute.type)>
      <basic name="${attribute.name}">
        <enumerated>STRING</enumerated>
      </basic>
    </#if></#list>
    <#list model.attributes as attribute><#if isModel(attribute.type)>
      <many-to-one name="${attribute.name}"></many-to-one>
    </#if></#list><#list model.attributes as attribute><#if attribute.isTransient() >
      <transient name="${attribute.name}" />
    </#if></#list>
    </attributes>
  </mapped-superclass>

  </#list>

  <#list project.models as model ><#if !model.isAbstract() >
  <entity class="${model.project.basePackage}.model.${model.name}"><#if model.inheritsFrom?? && !model.inheritsFrom.isAbstract()?? >
    <discriminator-value>${model.name}</discriminator-value></#if>
  </entity>

  </#if></#list>
</entity-mappings>