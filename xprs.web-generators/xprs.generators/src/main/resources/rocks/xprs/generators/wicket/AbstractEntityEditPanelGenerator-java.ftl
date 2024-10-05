<#ftl strip_whitespace = false>/**
 * This software was created with xprs.
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.gui.panels.${model.name?uncap_first};

import ${toCanonicalClassName(model)};<#list model.allTypes as type><#if isModel(type) || isEnum(type)>
import ${toCanonicalClassName(type)};</#if></#list><#list model.allTypes as type><#if isModel(type)>
import ${context.project.basePackage}.gui.model.${type.name}ListModel;</#if></#list>
import ${context.project.basePackage}.gui.Labeler;<#list model.allTypes as type><#if type.name = "Decimal">
import java.math.BigDecimal;</#if></#list><#list model.allTypes as type><#if type.name = "File">
import java.util.ArrayList;</#if></#list>
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.form.*;
import org.apache.wicket.markup.html.WebMarkupContainer;<#list model.allTypes as type><#if type.name = "File">
import org.apache.wicket.markup.html.basic.Label;</#if></#list>
import org.apache.wicket.markup.html.form.*;<#list model.allTypes as type><#if type.name = "File">
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;</#if></#list>
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;<#list model.allTypes as type><#if type.name = "File">
import org.apache.wicket.model.util.ListModel;</#if></#list>
import org.apache.wicket.validation.validator.*;
import rocks.xprs.db.Entity;

/**
 * Edit panel for a new ${model.name?uncap_first}
 */
public abstract class Abstract${model.name}EditPanel extends Panel {

  private ${model.name} entity;<#list model.allAttributes as attribute><#if attribute.name != 'createDate' && attribute.name != 'createUser' && attribute.name != 'modifyDate' && attribute.name != 'modifyUser' && !attribute.primaryKey >
  private WebMarkupContainer ${attribute.name}Panel;</#if></#list><#list model.allAttributes as attribute><#if attribute.type.name = 'File'>
  private FileUploadField ${attribute.name}UploadField;</#if></#list>

  private Map<String, List<? extends Entity>> choices = new HashMap<>();

  /**
   * Creates an edit panel for a new ${model.name?uncap_first}.
   * @param id wicket:id of the panel in markup
   */
  public Abstract${model.name}EditPanel(String id) {
    super(id);
  }

  /**
   * Creates an edit panel for an existing ${model.name?uncap_first}.
   * @param id wicket:id of the panel in markup
   * @param entity the ${model.name?uncap_first} to load
   */
  public Abstract${model.name}EditPanel(final String id, ${model.name} entity) {
		super(id, new CompoundPropertyModel<>(entity));
    this.entity = entity;
	}

  @Override
  protected void onInitialize() {

    super.onInitialize();

    // add fields<#list model.allAttributes as attribute><#if attribute.name != 'createDate' && attribute.name != 'createUser' && attribute.name != 'modifyDate' && attribute.name != 'modifyUser' && !attribute.primaryKey >
    ${attribute.name}Panel = new WebMarkupContainer("${attribute.name}Panel");<#if attribute.type.name = "String" && attribute.maxlength &gt; 0>
    TextField ${attribute.name}Field = new TextField("${attribute.name}");<#if attribute.required >
    ${attribute.name}Field.setRequired(true);</#if><#if attribute.matches?? >
    ${attribute.name}Field.add(new PatternValidator(${attribute.matches}));</#if>
    ${attribute.name}Field.add(new StringValidator(0, ${attribute.maxlength}));
    ${attribute.name}Panel.add(${attribute.name}Field);<#elseif attribute.type.name = "String" >
    TextArea ${attribute.name}Field = new TextArea("${attribute.name}");<#if attribute.required >
    ${attribute.name}Field.setRequired(true);</#if><#if attribute.matches?? >
    ${attribute.name}Field.add(new PatternValidator("${attribute.matches}"));</#if>
    ${attribute.name}Panel.add(${attribute.name}Field);<#elseif attribute.type.name = "Char" >
    TextArea ${attribute.name}Field = new TextArea("${attribute.name}");<#if attribute.required >
    ${attribute.name}Field.setRequired(true);</#if><#if attribute.matches?? >
    ${attribute.name}Field.add(new PatternValidator("${attribute.matches}"));</#if>
    ${attribute.name}Field.add(new StringValidator(0, 1));
    ${attribute.name}Panel.add(${attribute.name}Field);<#elseif attribute.type.name = "Boolean" >
    CheckBox ${attribute.name}Field = new CheckBox("${attribute.name}");
    ${attribute.name}Field.setRequired(true);
    ${attribute.name}Panel.add(${attribute.name}Field);<#elseif attribute.type.name = "Integer" || attribute.type.name = "Long" || attribute.type.name = "Float" || attribute.type.name = "Double" || attribute.type.name = "Decimal" >
    NumberTextField ${attribute.name}Field = new NumberTextField("${attribute.name}", ${toJavaType(attribute.type)}.class);<#if attribute.required >
    ${attribute.name}Field.setRequired(true);</#if>
    ${attribute.name}Field.setStep(0.0001);
    ${attribute.name}Panel.add(${attribute.name}Field);<#elseif attribute.type.name = "Date" >
    DateTextField ${attribute.name}Field = new DateTextField("${attribute.name}", getString("dateFormats.date.full"));
    ${attribute.name}Field.add(new AttributeModifier("placeholder", getString("dateFormats.date.full")));
    ${attribute.name}Panel.add(${attribute.name}Field);<#elseif attribute.type.name = "Time" >
    DateTextField ${attribute.name}Field = new DateTextField("${attribute.name}", getString("dateFormats.time.full"));
    ${attribute.name}Field.add(new AttributeModifier("placeholder", getString("dateFormats.time.full")));
    ${attribute.name}Panel.add(${attribute.name}Field);<#elseif attribute.type.name = "DateTime" >
    DateTextField ${attribute.name}Field = new DateTextField("${attribute.name}", getString("dateFormats.dateTime.full"));
    ${attribute.name}Field.add(new AttributeModifier("placeholder", getString("dateFormats.dateTime.full")));
    ${attribute.name}Panel.add(${attribute.name}Field);<#elseif attribute.type.name = "File" >
    Label ${attribute.name}FilenameField = new Label("${attribute.name}.filename");
    ${attribute.name}UploadField = new FileUploadField("${attribute.name}Upload",
            new ListModel<FileUpload>(new ArrayList<FileUpload>()));
    ${attribute.name}Panel.add(${attribute.name}FilenameField);
    ${attribute.name}Panel.add(${attribute.name}UploadField); <#elseif isModel(attribute.type) >

    IModel ${attribute.name}Choices;
    if (choices.containsKey("${attribute.name}")) {
      ${attribute.name}Choices = Model.of(choices.get("${attribute.name}"));
    } else {
      ${attribute.name}Choices = new ${attribute.type.name}ListModel();
    }
    DropDownChoice<${attribute.type.name}> ${attribute.name}Field = new DropDownChoice<>("${attribute.name}",
            ${attribute.name}Choices, new IChoiceRenderer<${attribute.type.name}>() {

      @Override
      public Object getDisplayValue(${attribute.type.name} object) {
        return get${attribute.name?cap_first}Label(object);
      }

      @Override
      public String getIdValue(${attribute.type.name} object, int index) {
        return String.valueOf(object.getId());
      }

      @Override
      public ${attribute.type.name} getObject(String id,
              IModel<? extends List<? extends ${attribute.type.name}>> choices) {

        if (id != null && !id.trim().isEmpty()) {
          Long longId = Long.parseLong(id);
          for (${attribute.type.name} i : choices.getObject()) {
            if (i.getId().equals(longId)) {
              return i;
            }
          }
        }
        return null;
      }
    });<#if !attribute.required>
    ${attribute.name}Field.setNullValid(true);</#if>
    ${attribute.name}Panel.add(${attribute.name}Field);<#elseif isEnum(attribute.type) >
    DropDownChoice<${attribute.type.name}> ${attribute.name}Field = new DropDownChoice<>("${attribute.name}",
            Arrays.asList(${attribute.type.name}.values()), new IChoiceRenderer<${attribute.type.name}>() {

      @Override
      public Object getDisplayValue(${attribute.type.name} object) {
        return get${attribute.name?cap_first}Label(object);
      }

      @Override
      public String getIdValue(${attribute.type.name} object, int index) {
        return object.toString();
      }

      @Override
      public ${attribute.type.name} getObject(String id,
              IModel<? extends List<? extends ${attribute.type.name}>> choices) {
        if (id != null && !id.trim().isEmpty()) {
          return ${attribute.type.name}.valueOf(id);
        } else {
          return null;
        }
      }
    });
    ${attribute.name}Panel.add(${attribute.name}Field);</#if>
    add(${attribute.name}Panel);
    </#if></#list>
  }

  /**
   * Sets the selectable options for a field
   * @param fieldName the field's name
   * @param options the list of options
   */
  public void setChoices(String fieldName, List<? extends Entity> options) {
    choices.put(fieldName, options);
  }

  /**
   * Returns the current ${model.name?uncap_first}.
   * @return the ${model.name?uncap_first}
   */
  public ${model.name} getEntity() {
    return entity;
  }<#list model.getAllAttributes() as attribute><#if attribute.name != 'createDate' && attribute.name != 'createUser' && attribute.name != 'modifyDate' && attribute.name != 'modifyUser' && !attribute.primaryKey ><#if isModel(attribute.type)>

  /**
   * Set the ${attribute.name}.
   * @param ${attribute.name} the ${attribute.name}
   */
  public void set${attribute.name?cap_first}(${attribute.type.name} ${attribute.name}) {
    entity.set${attribute.name?cap_first}(${attribute.name});
  }

  /**
   * Returns the label for ${attribute.name}'s select option.
   * @param ${attribute.name?uncap_first} the ${attribute.name}
   * @return the label
   */
  public String get${attribute.name?cap_first}Label(${attribute.type.name} ${attribute.name}) {
    return Labeler.getInstance().for${attribute.type.name}(${attribute.name});
  }<#elseif isEnum(attribute.type)>

  /**
   * Returns the label for the ${attribute.name}'s select option.
   * @param ${attribute.name} the ${attribute.name}
   * @return the label
   */
  public String get${attribute.name?cap_first}Label(${attribute.type.name} ${attribute.name}) {
    return getString("${attribute.type.name?uncap_first}." + ${attribute.name}.toString());
  }</#if>

  /**
   * Set the ${attribute.name} input visibility.
   * @param visibility true to show, false to hide input
   */
  public void set${attribute.name?cap_first}Visible(boolean visibility) {
    ${attribute.name}Panel.setVisible(visibility);
  }</#if></#list><#list model.allAttributes as attribute><#if attribute.type.name = 'File'>

  /**
   * Returns the FileUploadField for ${attribute.name}.
   * @return the file upload field
   */
  public FileUploadField get${attribute.name?cap_first}UploadField() {
    return ${attribute.name}UploadField;
  }</#if></#list>
}