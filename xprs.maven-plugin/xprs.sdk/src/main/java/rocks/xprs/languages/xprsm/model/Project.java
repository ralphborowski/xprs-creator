package rocks.xprs.languages.xprsm.model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents a project.
 * @author Ralph Borowski
 */
public class Project {

  private String projectName;
  private String basePackage;
  private final Map<String, Type> dataDefinitions = new LinkedHashMap();
  private final List<Model> models = new LinkedList();
  private final List<Enum> enums = new LinkedList();
  private final List<PrimitiveMapping> primitiveMappings = new LinkedList();
  private final List<PrimitiveType> primitiveTypes = new LinkedList();
  private List<Model> baseModels = new LinkedList();

  /**
   * Create a new project and init primitives.
   */
  public Project() {

    // init primitives
    addType(PrimitiveType.Boolean);
    addType(PrimitiveType.Integer);
    addType(PrimitiveType.Long);
    addType(PrimitiveType.Float);
    addType(PrimitiveType.Double);
    addType(PrimitiveType.Decimal);
    addType(PrimitiveType.Character);
    addType(PrimitiveType.String);
    addType(PrimitiveType.Date);
    addType(PrimitiveType.Time);
    addType(PrimitiveType.DateTime);
    addType(PrimitiveType.LocalDate);
    addType(PrimitiveType.LocalTime);
    addType(PrimitiveType.LocalDateTime);
    addType(PrimitiveType.Monetary);
  }

  /**
   * Add a new data definition (model, enum or primitive mapping) to the project.
   * @param type a type object
   */
  public final void addType(Type type) {

    dataDefinitions.put(type.getCanonicalName(), type);

    if ((type instanceof Model)) {
      this.getModels().add((Model) type);
    }

    if ((type instanceof Enum)) {
      enums.add((Enum) type);
    }

    if ((type instanceof PrimitiveMapping)) {
      primitiveMappings.add((PrimitiveMapping) type);
    }

    if ((type instanceof PrimitiveType)) {
      primitiveTypes.add((PrimitiveType) type);
    }
  }

  public final void addTypes(List<Type> types) {

    // add all types
    for (Type t : types) {
      if (!dataDefinitions.containsKey(t.getCanonicalName())) {
        addType(t);
      }
    }
  }

  /**
   * Get type by name.
   * @param name
   * @return a type object or null if not found
   */
  public final Type getType(String name) {
    if (!dataDefinitions.containsKey(name) && !name.contains("|")) {
      name = basePackage + "|" + name;
    }
    return dataDefinitions.get(name);
  }

  /**
   * Get an unmodifiable list of all types in the project.
   * @return a list of all types
   */
  public final List<Type> getTypes() {
    return new LinkedList<>(dataDefinitions.values());
  }

  /**
   * Get a primitive by name.
   * @param name name of the primitive
   * @return a primitive or null if not found.
   */
  public final PrimitiveType getPrimitive(String name) {
    Type type = dataDefinitions.get(name);
    if (type instanceof PrimitiveType) {
      return (PrimitiveType) type;
    }
    return null;
  }

  /**
   * Get a model by name.
   * @param name name of the model
   * @return a model or null if not found
   */
  public final Model getModel(String name) {
    if (!name.contains("|")) {
      name = basePackage + "|" + name;
    }
    Type type = dataDefinitions.get(name);
    if (type != null && type instanceof Model) {
      return (Model) type;
    }
    return null;
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and setters">
  /**
   * @return the projectName
   */
  public String getProjectName() {
    return projectName;
  }

  /**
   * @param projectName the projectName to set
   */
  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  /**
   * @return the basePackage
   */
  public String getBasePackage() {
    return basePackage;
  }

  /**
   * @param basePackage the basePackage to set
   */
  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
  }

  /**
   * @return the models
   */
  public List<Model> getModels() {
    return models;
  }

  /**
   * @return the enums
   */
  public List<Enum> getEnums() {
    return enums;
  }

  /**
   * @return the primitiveMappings
   */
  public List<PrimitiveMapping> getPrimitiveMappings() {
    return primitiveMappings;
  }

  /**
   * @return the primitiveTypes
   */
  public List<PrimitiveType> getPrimitiveTypes() {
    return primitiveTypes;
  }

  /**
   * @return the baseModels
   */
  public List<Model> getBaseModels() {
    return baseModels;
  }

  /**
   * @param baseModels the baseModels to set
   */
  public void setBaseModels(List<Model> baseModels) {
    this.baseModels = baseModels;
  }
  //</editor-fold>

}
