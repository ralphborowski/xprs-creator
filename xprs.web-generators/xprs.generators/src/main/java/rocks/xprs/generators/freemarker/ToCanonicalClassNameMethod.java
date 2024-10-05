/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.generators.freemarker;

import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import java.util.List;
import rocks.xprs.languages.xprsm.model.CompoundType;
import rocks.xprs.languages.xprsm.model.Type;

/**
 * Freemarker method to convert types to canonical names.
 *
 * @author Ralph Borowski
 */
public class ToCanonicalClassNameMethod implements TemplateMethodModelEx {

  /**
   * Converts a type string to a canonical class name.
   *
   * @param arguments a list with one xprs type object
   * @return the canonical class name or the empty string if type is a
   * primitive.
   * @throws TemplateModelException if no or a wrong argument is given
   */
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (!arguments.isEmpty()) {
      // get first argument and convert to object
      if (arguments.get(0) instanceof StringModel) {
        Object wrappedObject = ((StringModel) arguments.get(0)).getWrappedObject();
        if (wrappedObject instanceof Type) {
          Type argument = (Type) wrappedObject;

          // return date types
          if (argument.getCanonicalName().equals("Date")) {
            return "java.util.Date";
          }

          if (argument.getCanonicalName().equals("Time")) {
            return "java.util.Date";
          }

          if (argument.getCanonicalName().equals("DateTime")) {
            return "java.util.Date";
          }

          if (argument.getCanonicalName().equals("LocalDate")) {
            return "java.time.LocalDate";
          }

          if (argument.getCanonicalName().equals("LocalTime")) {
            return "java.time.LocalTime";
          }

          if (argument.getCanonicalName().equals("LocalDateTime")) {
            return "java.time.LocalDateTime";
          }

          if (argument.getCanonicalName().equals("Decimal")) {
            return "java.math.BigDecimal";
          }

          if (argument.getCanonicalName().equals("Monetary")) {
            return "rocks.xprs.types.Monetary";
          }

          // convert model names
          if (argument instanceof CompoundType) {
            return argument.getCanonicalName().replace("|", ".model.");
          }

          return "";
        } else {
          throw new TemplateModelException("[toCanonicalClassName] Argument is of type " + arguments.get(0).getClass().getCanonicalName() + ". Should be rocks.xprs.languages.xprsm.model.Type");
        }
      }
    }

    throw new TemplateModelException("[toCanonicalClassName] Argument is missing.");
  }

}
