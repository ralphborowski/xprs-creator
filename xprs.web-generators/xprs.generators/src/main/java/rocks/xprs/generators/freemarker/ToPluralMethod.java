/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.generators.freemarker;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import java.util.List;

/**
 * Freemarker method to convert a word to its plural.
 *
 * @author Ralph Borowski
 */
public class ToPluralMethod implements TemplateMethodModelEx {

  /**
   * Converts a word to its plural.
   * @param arguments a list with one one word
   * @return the plural
   * @throws TemplateModelException if no or a wrong argument is given
   */
  @Override
  public Object exec(List arguments) throws TemplateModelException {

    // check for arguments
    if (!arguments.isEmpty()) {

      // get first argument and convert to object
      if (arguments.get(0) instanceof SimpleScalar) {
        return toPlural(((SimpleScalar)arguments.get(0)).getAsString());
      }

      // parameter has wrong type
      throw new TemplateModelException("[toPluralMethod] Argument is of type " + arguments.get(0).getClass().getCanonicalName() + ". Should be String.");
    }

    // no parameter given, throw exception
    throw new TemplateModelException("[toPluralMethod] Argument missing.");
  }

  /**
   * Convert the given word to plural.
   * @param word the word
   * @return the plural form
   */
  public static String toPlural(String word) {

    char lastChar = word.charAt(word.length() - 1);

    if (lastChar == 'y' && !(word.endsWith("ay") || word.endsWith("oy") || word.endsWith("ey"))) {
      word = word.substring(0, word.length() - 1) + "ie";
    } else if (lastChar == 's' || lastChar == 'x' || lastChar == 'z' || lastChar == 'h') {
      word = word + "e";
    }

    return word + "s";
  }
}
