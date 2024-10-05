package rocks.xprs.languages.xprsm.model;

/**
 * Base class for all types (models, enums, primitives...).
 * @author Ralph Borowski
 */
public abstract class Type {

  protected String name;
  protected Project project;
  
  /**
   * Create a new Type instance.
   */
  public Type() {
    
  }
  
  /**
   * Create a new type instance and set a name.
   * @param name the name
   */
  public Type(String name) {
    this.name = name;
  }
  
  /**
   * Returns the full name of the class.
   * @return the full name (package + classname)
   */
  public String getCanonicalName() {
    return project.getBasePackage() + "|" + name;
  }

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
   * @return the project
   */
  public Project getProject() {
    return project;
  }
  
  /**
   * @param project the project to set
   */
  public void setProject(Project project) {
    this.project = project;
  }
  //</editor-fold>

}