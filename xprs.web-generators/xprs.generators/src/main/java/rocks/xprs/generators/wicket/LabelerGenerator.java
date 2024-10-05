/*
 * The MIT License
 *
 * Copyright 2016 Ralph Borowski.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package rocks.xprs.generators.wicket;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import rocks.xprs.generators.freemarker.AbstractFreemarkerGenerator;
import rocks.xprs.generators.localization.ResourceFile;
import rocks.xprs.languages.xprsm.model.Attribute;
import rocks.xprs.languages.xprsm.model.CompoundType;
import rocks.xprs.languages.xprsm.model.Model;

/**
 * Generates a data provider for use with Apache Wicket.
 *
 * @author Ralph Borowski
 */
public class LabelerGenerator extends AbstractFreemarkerGenerator {

  @Override
  public void close() throws IOException {

    // prepare names
    String basePackage = context.getProject().getBasePackage();
    
    try {

      // prepare abstract file
      String abstractTemplate
              = "Abstract" + this.getClass().getSimpleName() + ".ftl";
      String abstractFileName = basePackage.replace(".", "/")
              + "/gui/AbstractLabeler.java";
      File abstractFile = new File(context.getTargetFolder(), abstractFileName);

      // prepare inherited file
      String inheritedTemplate = this.getClass().getSimpleName() + ".ftl";
      String inheritedFileName
              = "java/" + basePackage.replace(".", "/") + "/gui/Labeler.java";
      File inheritedFile = new File(context.getSourceFolder(), inheritedFileName);

      // generate abstract class
      HashMap<String, Object> data = new HashMap<>();

      // add environment
      data.putAll(getEnvironment());

      // create parent folders
      if (!abstractFile.getParentFile().exists()) {
        abstractFile.getParentFile().mkdirs();
      }

      // compile template
      Template t = config.getTemplate(abstractTemplate);
      Writer out = new FileWriter(abstractFile);
      t.process(data, out);

      // generate inheriting class if not exists
      if (!inheritedFile.exists()) {

        // create parent folders
        if (!inheritedFile.getParentFile().exists()) {
          inheritedFile.getParentFile().mkdirs();
        }

        // compile template
        t = config.getTemplate(inheritedTemplate);
        out = new FileWriter(inheritedFile);
        t.process(data, out);
      }

    } catch (TemplateException ex) {
      throw new IOException("Template could not be processed.", ex);
    }

    try (ResourceFile resourceFile = new ResourceFile(new File(context.getSourceFolder(),
            "resources/bundles/model.xml"))) {

      // add resources for models
      for (Model m : context.getProject().getModels()) {

        String modelName = m.getName().substring(0, 1).toLowerCase()
                + m.getName().substring(1);

        // add resources for model
        resourceFile.add(modelName + ".model.name",
                ResourceFile.separateCamelCase(m.getName()));
        resourceFile.add(modelName + ".model.label",
                ResourceFile.separateCamelCase(m.getName()) + " %d");

        // add resources for attributes
        for (Attribute a : m.getAllAttributes()) {

          // get attribute name
          String attributeName = a.getName();

          // drop the "is" on boolean values
          if (attributeName.startsWith("is")) {
            attributeName = attributeName.substring(2);
          }

          // add resource
          resourceFile.add(modelName + ".attributes." + a.getName() + ".label",
                  ResourceFile.separateCamelCase(attributeName));
        }
      }

      // add resources for enums
      for (rocks.xprs.languages.xprsm.model.Enum e : context.getProject().getEnums()) {

        String enumName = e.getName().substring(0, 1).toLowerCase()
                + e.getName().substring(1);

        // add enums for options
        for (String o : e.getOptions()) {

          // add resource
          resourceFile.add(enumName + "." + o,
                  ResourceFile.convertSnakeCaseToLabel(o));
        }
      }
    }
  }

  @Override
  public void generate(CompoundType ct) throws IOException {
    // do nothing
  }
}
