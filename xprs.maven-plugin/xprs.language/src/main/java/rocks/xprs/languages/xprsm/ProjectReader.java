package rocks.xprs.languages.xprsm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import rocks.xprs.languages.xprsm.model.Project;
import rocks.xprs.languages.xprsm.model.Type;

/**
 * Wrapper class to read projects.
 * @author Ralph Borowski
 */
public class ProjectReader {

  /**
   * Read a project.
   * @param modelDescriptionFile a filename or url
   * @param sourceFolder the source folder
   * @param knownTypes types that are already known in the current context
   * @return returns a project
   * @throws IOException if the project file could not be found
   */
  public static Project read(String modelDescriptionFile, File sourceFolder, List<Type> knownTypes) throws IOException {

    // prepare input stream
    InputStream is;

    // search for project file
    if (modelDescriptionFile.matches("(http|https|ftp)://.*")) {

      // read from a network resource
      is = new URL(modelDescriptionFile).openStream();
    } else {

      // read project file from source folder
      File projectFile = new File(sourceFolder, "/xprs/" + modelDescriptionFile);
      if (projectFile.exists()) {
        is = new FileInputStream(projectFile);
      } else {

        // read project file from class path
        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(modelDescriptionFile);

        if (is == null) {

          // throw exception if project file could not be found
          throw new IOException("Project file " + modelDescriptionFile + " not found.");
        }
      }
    }

    // init target project
    Project project = new Project();
    project.addTypes(knownTypes);

    // init lexer and parse project file
    XprsmLexer lexer = new XprsmLexer(new ANTLRInputStream(is));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    XprsmParser parser = new XprsmParser(tokens);
    ProjectListener projectListener = new ProjectListener(sourceFolder, project, true);
    parser.addParseListener(projectListener);
    parser.parse();

    // close input stream
    is.close();

    // return project file
    return project;
  }
}