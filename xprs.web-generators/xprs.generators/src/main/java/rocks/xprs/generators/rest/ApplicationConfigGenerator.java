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

/**
 * Generate the application config class for JAX-RS. 
 * @author Ralph Borowski
 */
public class ApplicationConfigGenerator extends AbstractFreemarkerGenerator {
  
  @Override
  public void generate(CompoundType compoundType) throws IOException {
    // this method does nothing
  }
  
  @Override
  public void close() throws IOException {     
       
    // generate abstract class if not exists
    File applicationConfig = new File(
            context.getTargetFolder(), 
            context.getProject().getBasePackage().replace(".", "/") 
                    + "/api/AbstractApplicationConfig.java");
    
    generate(null, 
            "Abstract" + this.getClass().getSimpleName() + ".ftl", 
            applicationConfig);

    // generate child class if not exists
    File inheritedClass = new File(
            context.getSourceFolder(), 
            "java/" + context.getProject().getBasePackage().replace(".", "/") 
                    + "/api/ApplicationConfig.java");
    
    if (!inheritedClass.exists()) {
      generate(null, this.getClass().getSimpleName() + ".ftl", inheritedClass);
    }   
  }
}
