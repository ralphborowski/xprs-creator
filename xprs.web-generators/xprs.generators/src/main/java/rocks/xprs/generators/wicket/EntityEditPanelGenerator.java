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

import java.io.File;
import java.io.IOException;
import rocks.xprs.generators.freemarker.AbstractFreemarkerGenerator;
import rocks.xprs.languages.xprsm.model.CompoundType;
import rocks.xprs.languages.xprsm.model.Model;

/**
 * Generates a data access object.
 * @author Ralph Borowski
 */
public class EntityEditPanelGenerator extends AbstractFreemarkerGenerator {
  
  @Override
  public void generate(CompoundType compoundType) throws IOException {
    
    // execute generator only on models
    if (compoundType instanceof Model && !((Model) compoundType).isAbstract() ) {
      
      // prepare names
      String basePackage = context.getProject().getBasePackage();
      
      String abstractTemplate = 
              "Abstract" + this.getClass().getSimpleName() + "-java.ftl";      
      String abstractFileName = 
              basePackage.replace(".", "/") + "/gui/panels/"
              + compoundType.getName().substring(0, 1).toLowerCase() 
              + compoundType.getName().substring(1)
              + "/Abstract" + compoundType.getName() + "EditPanel.java";
      
      String abstractWicketTemplate = "Abstract" + this.getClass().getSimpleName() + "-wicket.ftl";
      String abstractWicketFileName = 
              basePackage.replace(".", "/") + "/gui/panels/" 
              + compoundType.getName().substring(0, 1).toLowerCase() 
              + compoundType.getName().substring(1)
              + "/Abstract" + compoundType.getName() + "EditPanel.html";
      
      String javaCodeTemplate = this.getClass().getSimpleName() + "-java.ftl";
      String javaCodeFileName = 
              "java/" + basePackage.replace(".", "/") + "/gui/panels/" 
              + compoundType.getName().substring(0, 1).toLowerCase() 
              + compoundType.getName().substring(1)
              + "/" + compoundType.getName() + "EditPanel.java";
      
      // generate abstract sources
      generate(
              compoundType, 
              abstractTemplate, 
              new File(context.getTargetFolder(), abstractFileName));
      generate(compoundType, 
              abstractWicketTemplate, 
              new File(context.getTargetFolder(), abstractWicketFileName));

      // generate inheriting class if not exists
      File javaCodeFile = new File(context.getSourceFolder(), javaCodeFileName);
      if (!javaCodeFile.exists()) {
        generate(compoundType, javaCodeTemplate, javaCodeFile);
      }
    }
  }
}