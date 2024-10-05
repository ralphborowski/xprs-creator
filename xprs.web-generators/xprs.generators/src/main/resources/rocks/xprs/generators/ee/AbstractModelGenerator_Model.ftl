/**
 * This software was created with xprs.
 * Have a look at https://xprs.rocks/ for more details.
 */

package ${context.project.basePackage}.model;

<#if model.inheritsFrom?? == false >
import rocks.xprs.runtime.db.DataItem;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Id;</#if>
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Date;
import java.util.Objects;
<#list model.types as type><#if toCanonicalClassName(type) != '' && (!toCanonicalClassName(type)?starts_with(model.project.basePackage)) >
import ${toCanonicalClassName(type)};
</#if></#list>

/**
 * Model class for ${model.name}.
 */
@MappedSuperclass
public abstract class Abstract${model.name?cap_first} <#if model.inheritsFrom?? >extends ${toJavaType(model.inheritsFrom)}<#else>implements DataItem</#if> {

  private static final long serialVersionUID = 1L;

  <#if model.inheritsFrom?? == false >
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "${model.name?uncap_first}_id_seq")
  @SequenceGenerator(name = "${model.name?uncap_first}_id_seq", sequenceName = "${model.name?uncap_first}_id_seq", allocationSize = 1)
  private Long id;

  private String createUser;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate;

  private String modifyUser;

  @Temporal(TemporalType.TIMESTAMP)
  private Date modifyDate;
  </#if>

<#list model.attributes as attribute><#if isEnum(attribute.type)>  @Enumerated(EnumType.STRING)
</#if><#if attribute.type.name == 'Date'>  @Temporal(TemporalType.DATE)
</#if><#if attribute.type.name == 'Time'>  @Temporal(TemporalType.TIME)
</#if><#if attribute.type.name == 'DateTime'>  @Temporal(TemporalType.TIMESTAMP)
</#if><#if attribute.type.name == 'Monetary'>

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name="amount", column=@Column(name="${attribute.name?upper_case}_AMOUNT")),
    @AttributeOverride(name="currency", column=@Column(name="${attribute.name?upper_case}_CURRENCY"))
  })
</#if>  private ${toModelType(attribute.type)} ${attribute.name};
</#list>

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters"><#if model.inheritsFrom?? == false >
  /**
   * @return the id
   */
  @Override
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  @Override
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the createUser
   */
  @Override
  public String getCreateUser() {
    return this.createUser;
  }

  /**
   * @param createUser the createUser to set
   */
  @Override
  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  /**
   * @return the createDate
   */
  @Override
  public Date getCreateDate() {
    return createDate;
  }

  /**
   * @param createDate the createDate to set
   */
  @Override
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  /**
   * @return the modifyUser
   */
  @Override
  public String getModifyUser() {
    return modifyUser;
  }

  /**
   * @param modifyUser the modifyUser to set
   */
  @Override
  public void setModifyUser(String modifyUser) {
    this.modifyUser = modifyUser;
  }

  /**
   * @return the modifyDate
   */
  @Override
  public Date getModifyDate() {
    return modifyDate;
  }

  /**
   * @param modifyDate the modifyDate to set
   */
  @Override
  public void setModifyDate(Date modifyDate) {
    this.modifyDate = modifyDate;
  }</#if><#list model.attributes as attribute>
  /**
   * Getter for ${attribute.name}
   * @return the ${attribute.name}
   */<#if attribute.name == 'id' || attribute.name == 'createUser' || attribute.name == 'createDate' || attribute.name == 'modifyUser' || attribute.name == 'modifyDate'>
  @Override</#if>
  public ${toModelType(attribute.type)} ${toGetter(attribute)}() {
    return ${attribute.name};
  }

  /**
   * Setter for ${attribute.name}
   * @param ${attribute.name} the ${attribute.name} to set
   */<#if attribute.name == 'id' || attribute.name == 'createUser' || attribute.name == 'createDate' || attribute.name == 'modifyUser' || attribute.name == 'modifyDate'>
  @Override</#if>
  public void set${attribute.name?cap_first}(${toModelType(attribute.type)} ${attribute.name}) {
    this.${attribute.name} = ${attribute.name};
  }
  </#list>
  //</editor-fold>

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }

    if (o instanceof Abstract${model.name?cap_first}) {
      return Objects.equals(((${model.name?cap_first}) o).getId(), getId());
    }

    return false;
  }
}