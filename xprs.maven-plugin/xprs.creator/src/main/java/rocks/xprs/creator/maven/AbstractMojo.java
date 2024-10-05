package rocks.xprs.creator.maven;

import com.google.common.base.Objects;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import rocks.xprs.creator.generator.Context;
import rocks.xprs.creator.generator.Generator;
import rocks.xprs.languages.xprsm.ProjectReader;
import rocks.xprs.languages.xprsm.model.CompoundType;
import rocks.xprs.languages.xprsm.model.Project;
import rocks.xprs.languages.xprsm.model.Type;

/**
 * Base class for all xprs creator mojos.
 * @author Ralph Borowski
 */
public abstract class AbstractMojo extends org.apache.maven.plugin.AbstractMojo {

  @Parameter(property = "generators", required = true)
  protected List<String> generators;

  @Parameter(property = "models", defaultValue = "all-models", required = false)
  protected String models;

  @Parameter(property = "debug", defaultValue = "false", required = false)
  protected boolean debug;

  @Parameter(property = "project", required = true, readonly = true)
  protected MavenProject mavenProject;

  @Parameter(property = "projectfile", defaultValue = "project.xprsm", required = false)
  protected String projectfile;

  @Parameter(property = "sourceFolder", defaultValue = "${project.basedir}/src/main/", required = false)
  protected File sourceFolder;

  @Parameter(property = "targetFolder", defaultValue = "${project.basedir}/target/generated-sources/xprs/", required = false)
  protected File targetFolder;

  public abstract void execute(Context paramContext) throws MojoExecutionException, MojoFailureException;

  /**
   * Reads project file and executes the given command.
   * @throws MojoExecutionException if an exception is thrown
   * @throws MojoFailureException if an error occures
   */
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {

      // read the project file
      Project project = ProjectReader.read(this.projectfile, this.sourceFolder, Collections.EMPTY_LIST);

      // prepare list of models
      List<CompoundType> selectedModelList = new LinkedList();

      if (this.models.equals("all-models")) {
        // add all models to the generator list
        for (Type m : project.getTypes()) {
          if (m instanceof CompoundType && Objects.equal(m.getProject().getBasePackage(), project.getBasePackage())) {
            selectedModelList.add((CompoundType) m);
          }
        }
      } else {

        // get models as selected on the command line
        String[] selectedModels = this.models.split(",");
        for (String s : selectedModels) {

          Type m = project.getType(s);
          if (m != null && m instanceof CompoundType) {
            selectedModelList.add((CompoundType) m);
          } else  {
            getLog().warn("Ignoring type '" + s + "'. Not found in project.");
          }
        }
      }

      // get generator classes
      List generatorList = new LinkedList();
      for (String g : this.generators) {
        try {
          Class clazz = Class.forName(g);
          if (Generator.class.isAssignableFrom(clazz)) {
            generatorList.add(clazz);
          }
        } catch (ClassNotFoundException ex) {
          getLog().warn("Class for generator '" + g + "' not found. Check classpath.");
        }
      }

      // build context
      Context context = new Context();
      context.setDebug(this.debug);
      context.setSourceFolder(this.sourceFolder);
      context.setGenerators(generatorList);
      context.setTargetFolder(this.targetFolder);
      context.setProject(project);
      context.setSelectedModels(selectedModelList);

      // add generated sources to compile path
      mavenProject.addCompileSourceRoot(targetFolder.getAbsolutePath());

      // execute mojo in project context
      execute(context);
    } catch (IOException ex) {
      throw new MojoExecutionException("Error initializing context.", ex);
    }
  }
}