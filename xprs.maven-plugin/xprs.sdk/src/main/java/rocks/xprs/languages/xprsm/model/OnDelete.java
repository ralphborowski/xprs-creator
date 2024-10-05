package rocks.xprs.languages.xprsm.model;

/**
 * Represents the on delete statement in a model.
 * @author Ralph Borowski
 */
public class OnDelete {

  private Model relatedModel;
  private Action action;

  public static enum Action { SET_NULL, DELETE }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  public Model getRelatedModel() {
    return this.relatedModel;
  }
  
  public void setRelatedModel(Model relatedModel) {
    this.relatedModel = relatedModel;
  }
  
  public Action getAction() {
    return this.action;
  }
  
  public void setAction(Action action) {
    this.action = action;
  }
  //</editor-fold>
}
