package rocks.xprs.languages.xprsm.model;

import java.util.List;

/**
 * Represents an enum class.
 * @author Ralph Borowski
 */
public class Enum extends CompoundType {
  
  private List<String> options;

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the options
   */
  public List<String> getOptions() {
    return options;
  }
  
  /**
   * @param options the options to set
   */
  public void setOptions(List<String> options) {
    this.options = options;
  }
  //</editor-fold>
}