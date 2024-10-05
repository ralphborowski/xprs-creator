package rocks.xprs.creator.maven;

import rocks.xprs.creator.generator.Context;
import rocks.xprs.languages.xprsm.model.Model;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Mojo that displayes all models in a project (mostly for debugging).
 * @author Ralph Borowski
 */
@Mojo(name = "list-models")
public class ListModelsMojo extends AbstractMojo {

  /**
   * Executes the mojo and prints a list of models in the log.
   * @param context
   * @throws MojoExecutionException
   * @throws MojoFailureException
   */
  @Override
  public void execute(Context context) throws MojoExecutionException, MojoFailureException {
    getLog().info("Available models in this project:");
    for (Model m : context.getProject().getModels()) {
      getLog().info("  * " + m.getName());
    }
  }
}