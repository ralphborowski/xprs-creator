package rocks.xprs.creator.generator;

import rocks.xprs.languages.xprsm.model.Project;
import java.io.File;
import java.util.List;
import rocks.xprs.languages.xprsm.model.CompoundType;

/**
 * The context a generator runs in.
 * @author Ralph Borowski
 */
public class Context {

  private Project project;
  private List<CompoundType> selectedModels;
  private File sourceFolder;
  private List<Class> generators;
  private File targetFolder;
  private boolean debug = false;

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">  
  public Project getProject() {
    return this.project;
  }
  
  public void setProject(Project project) {
    this.project = project;
  }
  
  public List<CompoundType> getSelectedModels() {
    return this.selectedModels;
  }
  
  public void setSelectedModels(List<CompoundType> selectedModels) {
    this.selectedModels = selectedModels;
  }
  
  public File getSourceFolder() {
    return this.sourceFolder;
  }
  
  public void setSourceFolder(File sourceFolder) {
    this.sourceFolder = sourceFolder;
  }
  
  public List<Class> getGenerators() {
    return this.generators;
  }
  
  public void setGenerators(List<Class> generators) {
    this.generators = generators;
  }
  
  public File getTargetFolder() {
    return this.targetFolder;
  }
  
  public void setTargetFolder(File targetFolder) {
    this.targetFolder = targetFolder;
  }
  
  public boolean isDebug() {
    return this.debug;
  }
  
  public void setDebug(boolean debug) {
    this.debug = debug;
  }
//</editor-fold>
}
