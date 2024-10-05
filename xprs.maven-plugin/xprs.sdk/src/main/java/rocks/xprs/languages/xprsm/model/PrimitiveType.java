package rocks.xprs.languages.xprsm.model;

/**
 * Represents a primitive type.
 * xprs knows the following classes as primitives:
 * <ul>
 *   <li>Integer</li>
 *   <li>Long</li>
 *   <li>Float</li>
 *   <li>Double</li>
 *   <li>Decimal</li>
 *   <li>Character</li>
 *   <li>String</li>
 *   <li>Boolean</li>
 *   <li>Date</li>
 *   <li>Time</li>
 *   <li>DateTime</li>
 * </ul>
 *
 * @author Ralph Borowski
 */
public class PrimitiveType extends Type {

  public static PrimitiveType Integer = new PrimitiveType("Integer");
  public static PrimitiveType Long = new PrimitiveType("Long");
  public static PrimitiveType Float = new PrimitiveType("Float");
  public static PrimitiveType Double = new PrimitiveType("Double");
  public static PrimitiveType Decimal = new PrimitiveType("Decimal");
  public static PrimitiveType Character = new PrimitiveType("Character");
  public static PrimitiveType String = new PrimitiveType("String");
  public static PrimitiveType Boolean = new PrimitiveType("Boolean");
  public static PrimitiveType Date = new PrimitiveType("Date");
  public static PrimitiveType Time = new PrimitiveType("Time");
  public static PrimitiveType DateTime = new PrimitiveType("DateTime");
  public static PrimitiveType LocalDate = new PrimitiveType("LocalDate");
  public static PrimitiveType LocalTime = new PrimitiveType("LocalTime");
  public static PrimitiveType LocalDateTime = new PrimitiveType("LocalDateTime");
  public static PrimitiveType Monetary = new PrimitiveType("Monetary");


  /**
   * Create a new primitive type.
   */
  public PrimitiveType() {
    super();
  }

  /**
   * Create a new primitive type.
   * @param name the name of the primitive
   */
  public PrimitiveType(String name) {
    super(name);
  }

  /**
   * Returns the canonical name.
   * @return the canonical name
   */
  @Override
  public String getCanonicalName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof PrimitiveType && this.getCanonicalName().equals(((PrimitiveType)o).getCanonicalName());
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}