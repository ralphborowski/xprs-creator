package rocks.xprs.languages.xprsm.model;

/**
 * Represents a primitive mapping.
 * @author Ralph Borowski
 */
public class PrimitiveMapping extends Type {

  private PrimitiveType mappedPrimitive;
  
  /**
   * Returns the canonical name.
   * @return the canonical name
   */
  @Override
  public String getCanonicalName() {
    return name;
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * Get the name of the mapped primitive type.
   * @return the name of the primitive type
   */
  public PrimitiveType getMappedPrimitive() {
    return this.mappedPrimitive;
  }
  
  /**
   * Set the name of the mapped primitive type.
   * @param mappedPrimitive e.g. Integer, Long, String or Date
   */
  public void setMappedPrimitive(PrimitiveType mappedPrimitive) {
    this.mappedPrimitive = mappedPrimitive;
  }
  //</editor-fold>

}