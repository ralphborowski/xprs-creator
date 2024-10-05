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
package rocks.xprs.generators.gui;

import java.io.File;
import java.io.IOException;
import rocks.xprs.generators.freemarker.AbstractFreemarkerGenerator;
import rocks.xprs.languages.xprsm.model.CompoundType;
import rocks.xprs.languages.xprsm.model.Model;

/**
 * Generates a data access object.
 * @author Ralph Borowski
 */
public class EditModuleGenerator extends AbstractFreemarkerGenerator {
  
  @Override
  public void generate(CompoundType compoundType) throws IOException {
    
    // execute generator only on models
    if (compoundType instanceof Model && !((Model) compoundType).isAbstract() ) {
      
      // prepare names
      String basePackage = context.getProject().getBasePackage();
      
      String abstractTemplate = 
              "Abstract" + this.getClass().getSimpleName() + ".ftl";      
      String abstractFileName = 
              basePackage.replace(".", "/") + "/gui/modules/" 
              + "Abstract" + compoundType.getName() + "EditModule.java";
      
      String javaCodeTemplate = this.getClass().getSimpleName() + "-java.ftl";
      String javaCodeFileName = 
              "java/" + basePackage.replace(".", "/") + "/gui/modules/" 
              + compoundType.getName() + "EditModule.java";
      
      String freemarkerTemplate = this.getClass().getSimpleName() + "-fm.ftl";
      String freemarkerFileName = 
              "resources/" + basePackage.replace(".", "/") + "/gui/modules/" 
              + compoundType.getName() + "EditModule.ftl";
      
      // generate abstract sources
      generate(
              compoundType, 
              abstractTemplate, 
              new File(context.getTargetFolder(), abstractFileName));

      // generate inheriting class if not exists
      File javaCodeFile = new File(context.getSourceFolder(), javaCodeFileName);
      if (!javaCodeFile.exists()) {
        generate(compoundType, javaCodeTemplate, javaCodeFile);
      }
      
      // create freemarker template if not exists
      File freemarkerFile = new File(context.getSourceFolder(), freemarkerFileName);
      // if (!freemarkerFile.exists()) {
        generate(compoundType, freemarkerTemplate, freemarkerFile);
      // }
    }
  }
}