package rocks.xprs.creator.maven;

import java.util.Arrays;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import rocks.xprs.creator.Builder;
import rocks.xprs.creator.generator.Context;

/**
 * Generate Mojo. Calls the builder and generates the sources. Should be included in 'generate-sources'.
 * @author Ralph Borowski
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE, requiresProject = true)
public class GenerateMojo extends AbstractMojo {

  /**
   * Calls the builder
   * @param context the project's context.
   * @throws MojoExecutionException
   * @throws MojoFailureException
   */
  @Override
  public void execute(Context context) throws MojoExecutionException, MojoFailureException {

    // show status information
    getLog().info("xprs is generating sources...");
    getLog().info("Running " + context.getGenerators().size() + " generators on " + context.getSelectedModels().size() + " models.");

    // init builder
    try (Builder builder = new Builder(context)) {

      // call generators on models
      builder.build(context.getSelectedModels(), context.getGenerators());
      getLog().info("Done.");

      // pack compiled resources
      Resource resourceFiles = new Resource();
      resourceFiles.setFiltering("false");
      resourceFiles.setDirectory(targetFolder.getAbsolutePath());
      resourceFiles.setIncludes(Arrays.asList("**"));
      resourceFiles.addExclude("**/*.java");
      mavenProject.addResource(resourceFiles);

    } catch (Exception ex) {
      throw new MojoExecutionException("Error during source generation.", ex);
    }
  }
}