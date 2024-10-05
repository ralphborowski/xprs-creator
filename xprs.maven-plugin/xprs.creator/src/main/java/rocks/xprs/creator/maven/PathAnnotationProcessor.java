/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.creator.maven;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 *
 * @author rborowski
 */
@SupportedAnnotationTypes("rocks.xprs.web.Path")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class PathAnnotationProcessor extends AbstractProcessor {

  private Set<String> classNames = new HashSet<>();

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment re) {

    // get all annoted classes
    for (TypeElement annotation : annotations) {
      Set<? extends Element> annotatedElements = re.getElementsAnnotatedWith(annotation);

      for (Element e : annotatedElements) {
        if (e.getKind().isClass()) {
          classNames.add(((TypeElement) e).getQualifiedName().toString());
        }
      }
    }

    // generate Application class
    if (re.processingOver()) {
      String applicationClass = processingEnv.getOptions().get("applicationClass");

      if (applicationClass == null) {
        return false;
      }

      try {
        JavaFileObject applicationFile = processingEnv.getFiler()
                .createSourceFile(applicationClass + "Final");
        try (PrintWriter writer = new PrintWriter(applicationFile.openWriter())) {

          writer.println("/* GENERATED CODE. ANY CHANGES WILL BE OVERWRITTEN ON RECOMPILE. */");

          // package
          String simpleClassName = applicationClass;
          if (applicationClass.contains(".")) {
            writer.println(String.format("package %s;", applicationClass
                    .substring(0, applicationClass.lastIndexOf("."))));
            simpleClassName = applicationClass.substring(applicationClass.lastIndexOf(".") + 1);
            writer.println();
          }

          // class
          writer.printf("public class %sFinal extends %s {",
                  simpleClassName, applicationClass);
          writer.println();
          writer.println();

          writer.println("  @Override");
          writer.println("  public void setup() {");
          for (String c : classNames) {
            writer.println(String.format("    register(%s.class);", c));
          }
          writer.println("    super.setup();");
          writer.println("  }");
          writer.println("}");
        }

      } catch (IOException ex) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                String.format("Could not write application file $sFinal: %s",
                        applicationClass, ex.getMessage()));
      }
    }

    return true;
  }

}
