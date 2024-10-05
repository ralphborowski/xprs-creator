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
package rocks.xprs.generators.db;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import rocks.xprs.generators.freemarker.AbstractFreemarkerGenerator;
import rocks.xprs.languages.xprsm.model.CompoundType;

/**
 * Generate a orm.xml file instead of JPA annotations. So you can share the
 * same POJOs without depending on JPA libraries.
 * 
 * @author Ralph Borowski
 */
public class OrmXmlGenerator extends AbstractFreemarkerGenerator {
  
  @Override
  public void generate(CompoundType compoundType) throws IOException {
    // ignore
  }
  
  @Override
  public void close() throws IOException {       
    
    try {
      
      // prepare target file
      File target = new File(context.getSourceFolder(), 
              "resources/META-INF/orm.xml");
      
      // prepare template file
      String templateName = this.getClass().getSimpleName() + ".ftl";
      
      // prepare data object
      HashMap<String, Object> data = new HashMap<>();
      
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
