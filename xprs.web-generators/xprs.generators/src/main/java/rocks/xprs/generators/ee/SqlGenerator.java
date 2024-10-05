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
package rocks.xprs.generators.ee;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import rocks.xprs.generators.freemarker.AbstractFreemarkerGenerator;
import rocks.xprs.languages.xprsm.model.Attribute;
import rocks.xprs.languages.xprsm.model.CompoundType;
import rocks.xprs.languages.xprsm.model.Model;
import rocks.xprs.languages.xprsm.model.PrimitiveType;

/**
 *
 * @author Ralph Borowski
 */
public class SqlGenerator extends AbstractFreemarkerGenerator {

  @Override
  public void generate(CompoundType compoundType) throws IOException {
    // do nothing
  }

  @Override
  public void close() throws IOException {

    // prepare model definitions
    LinkedHashMap<String, Model> sqlModels = new LinkedHashMap<>();

    // converting model to sql model
    for (Model m : context.getProject().getModels()) {

      // use only non-abstract models to create tables
      if (m.isAbstract()) {
        continue;
      }

      // if model doesn't inherit another model or parent model is abstract, add to list
      if (m.getInheritsFrom() == null || m.getInheritsFrom().isAbstract()) {

        // copy model, because attritute list may be changed
        // and could effect other generators;
        if (!sqlModels.containsKey(m.getCanonicalName())) {
          Model mCopy = new Model();
          mCopy.setProject(m.getProject());
          mCopy.setName(m.getName());
          mCopy.setInheritsFrom(m.getInheritsFrom());
          mCopy.setAbstract(m.isAbstract());
          mCopy.setOnDelete(m.getOnDelete());
          mCopy.getAttributes().addAll(m.getAllAttributes());
          sqlModels.put(mCopy.getCanonicalName(), mCopy);
        }
      } else if (m.getInheritsFrom() != null && !m.getInheritsFrom().isAbstract()) {

        // if parent model is not abstract, find highest non abstract model
        // in inheritance hierarchy
        Model currentModel = m.getInheritsFrom();
        Model parentModel = m;

        while (currentModel != null && !currentModel.isAbstract()) {

          // go one step up in hierarchy
          parentModel = currentModel;
          currentModel = parentModel.getInheritsFrom();
        }

        // check if parent model is already in sql model list
        if (!sqlModels.containsKey(parentModel.getCanonicalName())) {
          Model newParentModel = new Model();
          newParentModel.setProject(parentModel.getProject());
          newParentModel.setName(parentModel.getName());
          sqlModels.put(newParentModel.getCanonicalName(), newParentModel);
        }

        // add attributes to sql parent model
        Model parentSqlModel = sqlModels.get(parentModel.getCanonicalName());

        for (Attribute a : m.getAttributes()) {
          if (!parentSqlModel.hasAttribute(a.getName())) {
            parentSqlModel.getAttributes().add(a);
            System.out.println("ADDED " + a.getName() + " TO " + parentSqlModel.getName());
          }
        }

        if (!parentSqlModel.hasAttribute("DTYPE")) {
          System.out.println("ADDED DTYPE TO " + parentSqlModel.getName());
          Attribute dtype = new Attribute();
          dtype.setName("DTYPE");
          dtype.setType(PrimitiveType.String);
          dtype.setMaxlength(200);
          parentSqlModel.getAttributes().add(dtype);
        }

        System.out.println("ATTRIBUTES OF " + parentSqlModel.getName());
        for (Attribute a : parentSqlModel.getAttributes()) {
          System.out.println("  " + a.getName());
        }

        sqlModels.put(parentModel.getCanonicalName(), parentSqlModel);
      }
    }

    // prepare data map
    HashMap<String, Object> data = new HashMap<>();
    data.put("sqlModels", sqlModels.values());

    try {

      // prepare target file name
      File target = new File(context.getSourceFolder(),
          "resources/db/migrations/schema-" + context.getProject().getProjectName().toLowerCase() + ".sql");

      // prepare template file
      String templateName = this.getClass().getSimpleName() + ".ftl";

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

}
