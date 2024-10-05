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
package rocks.xprs.generators.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import rocks.xprs.creator.generator.Context;
import rocks.xprs.creator.generator.Generator;
import rocks.xprs.languages.xprsm.model.CompoundType;
import rocks.xprs.languages.xprsm.model.Model;

/**
 * Interface for generators that uses FreeMarker templates.
 * @author Ralph Borowski
 */
public abstract class AbstractFreemarkerGenerator implements Generator {

  protected Context context;
  protected Configuration config;

  /**
   * Inits the generator. Run this method once before using the generator.
   * @param context the current project context
   */
  @Override
  public void init(Context context) {

    // store context
    this.context = context;

    // init templating config
    config = new Configuration(Configuration.VERSION_2_3_23);
    config.setClassLoaderForTemplateLoading(
            Thread.currentThread().getContextClassLoader(),
            "/" + this.getClass().getPackage().getName().replace(".", "/"));

    config.setDefaultEncoding("UTF-8");
    config.setTemplateExceptionHandler(
            TemplateExceptionHandler.RETHROW_HANDLER);
  }

  /**
   * Applies a template on a single model.
   * @param compoundType the model or enum
   * @param templateName the name of the template
   * @param target the target file
   * @throws IOException if template could not be read
   *         or output could not be written
   */
  protected void generate(CompoundType compoundType,
          String templateName, File target) throws IOException {

    try {

      // prepare data object
      HashMap<String, Object> data = new HashMap<>();

      // add as model?
      if (compoundType instanceof Model) {
        data.put("model", compoundType);
      }

      // add as enum?
      if (compoundType instanceof rocks.xprs.languages.xprsm.model.Enum) {
        data.put("enum", compoundType);
      }

      // add environment
      data.putAll(getEnvironment());

      // create parent folders
      if (!target.getParentFile().exists()) {
        target.getParentFile().mkdirs();
      }

      // compile template
      Template t = config.getTemplate(templateName);
      Writer out = new FileWriter(target);
      t.process(data, out);
    } catch (TemplateException ex) {
      throw new IOException("Template could not be processed.", ex);
    }
  }

  /**
   * Returns a map with environmental data for use in the template,
   * e.g. context, project or methods.
   * @return a map of data
   */
  protected Map<String, Object> getEnvironment() {

    // prepare result set
    HashMap<String, Object> environment = new HashMap<>();

    // add environment
    environment.put("project", context.getProject());
    environment.put("context", context);

    // register custom methods
    environment.put("toJavaType",
            new ToJavaTypeMethod());

    environment.put("toModelType",
            new ToModelTypeMethod());

    environment.put("toCanonicalClassName",
            new ToCanonicalClassNameMethod());

    environment.put("toCanonicalDaoClassName",
            new ToCanonicalDaoClassNameMethod());

    environment.put("toCanonicalFilterClassName",
            new ToCanonicalFilterClassNameMethod());

    environment.put("toCanonicalValidatorClassName",
            new ToCanonicalValidatorClassNameMethod());

    environment.put("toCanonicalDescriptorClassName",
            new ToCanonicalDescriptorClassNameMethod());

    environment.put("toSimpleDescriptorClassName",
            new ToSimpleDescriptorClassNameMethod());

    environment.put("toProjectShortName",
            new ToProjectShortName());

    environment.put("toGetter",
            new ToGetterMethod());

    environment.put("instanceOf",
            new InstanceOfMethod());

    environment.put("isModel",
            new IsModelMethod());

    environment.put("isEnum",
            new IsEnumMethod());

    environment.put("isPrimitiveType",
            new IsPrimitiveTypeMethod());

    environment.put("isLastInModelHierarchyMethod",
            new IsLastInModelHierarchyMethod());

    environment.put("toPlural",
            new ToPluralMethod());

    // return environment
    return environment;
  }

  @Override
  public void close() throws IOException {

  }
}