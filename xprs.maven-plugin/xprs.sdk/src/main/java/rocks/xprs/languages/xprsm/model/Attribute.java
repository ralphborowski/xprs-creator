package rocks.xprs.languages.xprsm.model;

/**
 * Represents a model's attribute.
 * @author Ralph Borowski
 */
public class Attribute {

  private String name;
  private Type type;
  private int maxlength;
  private String defaultValue;
  private String matches;
  private boolean isPrimaryKey;
  private boolean isRequired;
  private boolean isTransient;
  private boolean isUnique;

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return the type
   */
  public Type getType() {
    return type;
  }
  
  /**
   * @param type the type to set
   */
  public void setType(Type type) {
    this.type = type;
  }
  
  /**
   * @return the maxlength
   */
  public int getMaxlength() {
    return maxlength;
  }
  
  /**
   * @param maxlength the maxlength to set
   */
  public void setMaxlength(int maxlength) {
    this.maxlength = maxlength;
  }
  
  /**
   * @return the defaultValue
   */
  public String getDefaultValue() {
    return defaultValue;
  }
  
  /**
   * @param defaultValue the defaultValue to set
   */
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }
  
  /**
   * @return the matches
   */
  public String getMatches() {
    return matches;
  }
  
  /**
   * @param matches the matches to set
   */
  public void setMatches(String matches) {
    this.matches = matches;
  }
  
  /**
   * @return the isPrimaryKey
   */
  public boolean isPrimaryKey() {
    return isPrimaryKey;
  }
  
  /**
   * @param isPrimaryKey the isPrimaryKey to set
   */
  public void setPrimaryKey(boolean isPrimaryKey) {
    this.isPrimaryKey = isPrimaryKey;
  }
  
  /**
   * @return the isRequired
   */
  public boolean isRequired() {
    return isRequired;
  }
  
  /**
   * @param isRequired the isRequired to set
   */
  public void setRequired(boolean isRequired) {
    this.isRequired = isRequired;
  }
  
  /**
   * @return the isTransient
   */
  public boolean isTransient() {
    return isTransient;
  }
  
  /**
   * @param isTransient the isTransient to set
   */
  public void setTransient(boolean isTransient) {
    this.isTransient = isTransient;
  }
  
  /**
   * @return the isUnique
   */
  public boolean isUnique() {
    return isUnique;
  }

  /**
   * @param isUnique the isUnique to set
   */
  public void setUnique(boolean isUnique) {
    this.isUnique = isUnique;
  }
  //</editor-fold>
}