package rocks.xprs.languages.xprsm;

import java.io.File;
import java.util.List;
import rocks.xprs.languages.xprsm.model.Attribute;
import rocks.xprs.languages.xprsm.model.Enum;
import rocks.xprs.languages.xprsm.model.Model;
import rocks.xprs.languages.xprsm.model.OnDelete;
import rocks.xprs.languages.xprsm.model.PrimitiveMapping;
import rocks.xprs.languages.xprsm.model.Project;
import rocks.xprs.languages.xprsm.model.Type;

/**
 * Implements callback functions to read the project file.
 * @author Ralph Borowski
 */
public class ProjectListener extends XprsmBaseListener {

  private final File sourceFolder;
  private final Project project;
  private Model currentModel;
  private boolean isDebug = false;

  /**
   * Create a new project listener.
   * @param sourceFolder a source folder
   * @param project a target project
   * @param isDebug show debug messages
   */
  public ProjectListener(File sourceFolder, Project project, boolean isDebug) {
    super();
    this.sourceFolder = sourceFolder;
    this.project = project;
    this.isDebug = isDebug;
  }

  /**
   * Create a new project listener.
   * @param sourceFolder a source folder
   * @param project a target project
   */
  public ProjectListener(File sourceFolder, Project project) {
    this(sourceFolder, project, false);
  }

  /**
   * Get the project after parsing has finished.
   * @return the project
   */
  public Project getProject() {
    return this.project;
  }

  //<editor-fold defaultstate="collapsed" desc="Callback methods">
  @Override
  public void exitProject_name(XprsmParser.Project_nameContext ctx) {
    this.project.setProjectName(ctx.getText().substring(1, ctx.getText().length() - 1));
  }

  @Override
  public void exitBasepackage_statement(XprsmParser.Basepackage_statementContext ctx) {
    this.project.setBasePackage(ctx.package_name().getText().substring(1, ctx.package_name().getText().length() - 1));
  }

  @Override
  public void exitInclude_statement(XprsmParser.Include_statementContext ctx) {
    try {
      Project importedProject = ProjectReader.read(ctx.source_name().getText().substring(1, ctx.source_name().getText().length() - 1), this.sourceFolder, project.getTypes());
      project.addTypes(importedProject.getTypes());
    } catch (java.io.IOException ex) {
      java.util.logging.Logger.getLogger(ProjectListener.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
  }

  @Override
  public void exitDefine_primitive_statement(XprsmParser.Define_primitive_statementContext ctx) {
    PrimitiveMapping mapping = new PrimitiveMapping();
    mapping.setName(ctx.primitive_name().getText());
    mapping.setProject(project);
    mapping.setMappedPrimitive(project.getPrimitive(ctx.datatype().getText()));
    this.project.addType(mapping);

    if (isDebug) {
      logAddType(mapping);
    }
  }

  @Override
  public void exitDefine_enum_statement(XprsmParser.Define_enum_statementContext ctx) {
    Enum enumeration = new Enum();
    enumeration.setName(ctx.model_name().getText());
    enumeration.setProject(project);
    List<String> options = new java.util.ArrayList();
    for (XprsmParser.OptionContext c : ctx.optionlist().option()) {
      options.add(c.getText());
    }
    enumeration.setOptions(options);
    this.project.addType(enumeration);

    if (isDebug) {
      logAddType(enumeration);
    }
  }

  @Override
  public void enterDefine_model_statement(XprsmParser.Define_model_statementContext ctx) {
    this.currentModel = new Model();
  }

  @Override
  public void exitDefine_model_statement(XprsmParser.Define_model_statementContext ctx) {

    // update or create new model
    Model m;
    String modelName = ctx.model_name().getText();
    if (this.project.getModel(modelName) != null) {
      m = (Model) this.project.getModel(modelName);
    } else {
      m = new Model();
      m.setName(modelName);
      m.setProject(project);
      this.project.addType(m);

      if (isDebug) {
        logAddType(m);
      }
    }

    // copy data
    m.setName(modelName);
    m.setAbstract(ctx.K_ABSTRACT() != null);
    m.setInheritsFrom(this.currentModel.getInheritsFrom());
    m.setAttributes(this.currentModel.getAttributes());
    m.setOnDelete(this.currentModel.getOnDelete());
  }

  @Override
  public void exitInherit_from(XprsmParser.Inherit_fromContext ctx) {

    String modelName = ctx.model_name().getText();

    if (modelName != null) {

      // strip quotes (if any)
      modelName = modelName.replace("\"", "");

      // unknown model?
      if (this.project.getModel(modelName) == null) {
        Model m = new Model();
        m.setName(modelName);
        m.setProject(project);
        this.project.addType(m);

        if (isDebug) {
          logAddType(m);
        }
      }

      this.currentModel.setInheritsFrom(this.project.getModel(modelName));
    }
  }

  @Override
  public void exitField(XprsmParser.FieldContext ctx) {

    // get data type
    String typeName = ctx.datatype().datatype_name().getText();
    if (this.project.getType(typeName) == null) {
      Model m = new Model();
      m.setName(typeName);
      m.setProject(project);
      this.project.addType(m);

      if (isDebug) {
        logAddType(m);
      }
    }

    Attribute field = new Attribute();
    field.setName(ctx.fieldname().getText());
    field.setType(this.project.getType(typeName));
    if (ctx.datatype().datatype_limit() != null) {
      field.setMaxlength(Integer.parseInt(ctx.datatype().datatype_limit().getText()));
    }

    for (XprsmParser.Field_constraintContext c : ctx.field_constraint()) {
      if (!c.getTokens(XprsmParser.K_PRIMARY).isEmpty()) {
        field.setPrimaryKey(true);
      }

      if (!c.getTokens(XprsmParser.K_DEFAULT).isEmpty()) {
        field.setDefaultValue(c.default_value().getText().substring(1, c.default_value().getText().length() - 1));
      }

      if (!c.getTokens(XprsmParser.K_MATCHES).isEmpty()) {
        field.setMatches(c.regex().getText().substring(1, c.regex().getText().length() - 1));
      }

      if (!c.getTokens(XprsmParser.K_REQUIRED).isEmpty()) {
        field.setRequired(true);
      }

      if (!c.getTokens(XprsmParser.K_TRANSIENT).isEmpty()) {
        field.setTransient(true);
      }

      if (!c.getTokens(XprsmParser.K_UNIQUE).isEmpty()) {
        field.setUnique(true);
      }
    }
    this.currentModel.getAttributes().add(field);
  }

  @Override
  public void exitOn_delete(XprsmParser.On_deleteContext ctx) {
    OnDelete onDelete = new OnDelete();

    String modelName = ctx.model_name().getText();
    if (this.project.getModel(modelName) == null) {
      Model m = new Model();
      m.setName(modelName);
      m.setProject(project);
      this.project.addType(m);

      if (isDebug) {
        logAddType(m);
      }
    }
    onDelete.setRelatedModel(this.project.getModel(modelName));

    if (ctx.K_DELETE() != null) {
      onDelete.setAction(OnDelete.Action.DELETE);
    }

    if (ctx.K_NULL() != null) {
      onDelete.setAction(OnDelete.Action.SET_NULL);
    }

    currentModel.getOnDelete().add(onDelete);
  }

  @Override
  public void exitUse_statement(XprsmParser.Use_statementContext ctx) {
    this.project.getBaseModels().add((Model) this.project.getModel(ctx.model_name().getText()));
  }
  //</editor-fold>

  private void logAddType(Type type) {
    System.out.println(String.format("Adding type %s(%s).",
            type.getCanonicalName(), type.getClass().getSimpleName()));
  }
}