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

import java.io.File;
import java.io.IOException;
import rocks.xprs.generators.freemarker.AbstractFreemarkerGenerator;
import rocks.xprs.generators.freemarker.ToPluralMethod;
import rocks.xprs.languages.xprsm.model.CompoundType;
import rocks.xprs.languages.xprsm.model.Model;

/**
 * Generates a data access object.
 * @author Ralph Borowski
 */
public class DaoGenerator extends AbstractFreemarkerGenerator {

  @Override
  public void generate(CompoundType compoundType) throws IOException {

    // only execute generator on models
    if (compoundType instanceof Model) {

      // prepare names
      String basePackage = context.getProject().getBasePackage();
      String abstractTemplate =
              "Abstract" + this.getClass().getSimpleName() + ".ftl";

      String inheritingTemplate = this.getClass().getSimpleName() + ".ftl";
      String abstractFileName =
              basePackage.replace(".", "/") + "/dao/"
              + "Abstract" + ToPluralMethod.toPlural(compoundType.getName()) + ".java";

      String inheritingFileName =
              "java/" + basePackage.replace(".", "/") + "/dao/"
              + ToPluralMethod.toPlural(compoundType.getName()) + ".java";

      // generate abstract sources
      generate(
              compoundType,
              abstractTemplate,
              new File(context.getTargetFolder(), abstractFileName));

      // generate inheriting class if not exists
      File inheritingFile =
              new File(context.getSourceFolder(), inheritingFileName);

      if (!inheritingFile.exists()) {
        generate(compoundType, inheritingTemplate, inheritingFile);
      }
    }
  }
}