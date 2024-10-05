<#ftl strip_whitespace = false>/**
 * This software was created with xprs. 
 * Have a look at https://xprs.rocks/ for more details.
 */
package ${context.project.basePackage}.gui;

/**
 * Generates default labels used for each entity, for example in drop down lists, 
 * lists or details views.
 */
public class Labeler extends AbstractLabeler {

  private static Labeler instance;

  /**
   * Returns a labeler instance.
   * @returns the labeler
   */
  public static Labeler getInstance() {
    if (instance == null) {
      instance = new Labeler();
    }
    return instance;
  }
  
  protected Labeler() {
    super();
  }
}