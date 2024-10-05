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
package rocks.xprs.generators.rest;

import java.io.File;
import java.io.IOException;
import rocks.xprs.generators.freemarker.AbstractFreemarkerGenerator;
import rocks.xprs.languages.xprsm.model.CompoundType;
import rocks.xprs.languages.xprsm.model.Model;

/**
 * Generates a REST resource for a model.
 * @author Ralph Borowski
 */
public class RestApiGenerator extends AbstractFreemarkerGenerator {
  
  @Override
  public void generate(CompoundType compoundType) throws IOException {
    if (compoundType instanceof Model) {
      generate(
              compoundType, 
              "Abstract" + this.getClass().getSimpleName() + ".ftl", 
              new File(context.getTargetFolder(), 
                      compoundType.getProject().getBasePackage()
                              .replace(".", "/") 
                              + "/api/Abstract" + compoundType.getName() 
                              + "Resource.java"));

      // generate child class if not exists
      File inheritedClass = new File(
              context.getSourceFolder(), 
              "java/" + compoundType.getProject().getBasePackage()
                      .replace(".", "/") 
                      + "/api/" + compoundType.getName() + "Resource.java");
      
      if (!inheritedClass.exists()) {
        generate(
                compoundType, 
                this.getClass().getSimpleName() + ".ftl", 
                inheritedClass);
      }
    }
  }
}