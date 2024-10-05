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
import rocks.xprs.languages.xprsm.model.PrimitiveMapping;
import rocks.xprs.languages.xprsm.model.Type;

/**
 * Freemarker method to convert xprs Types to Java types.
 *
 * @author Ralph Borowski
 */
public class ToModelTypeMethod implements TemplateMethodModelEx {

  /**
   * Converts a xprs type to a Java type (simple name).
   * @param arguments a list with one xprs Type object
   * @return the Java type
   * @throws TemplateModelException if no or a wrong argument is given
   */
  @Override
  public Object exec(List arguments) throws TemplateModelException {

    // check for arguments
    if (!arguments.isEmpty()) {

      // get first argument and convert to object
      if (arguments.get(0) instanceof StringModel) {
        Object wrappedObject = ((StringModel) arguments.get(0)).getWrappedObject();
        if (wrappedObject instanceof Type) {
          Type argument = (Type) wrappedObject;

          // internal primitives Date, Time and DateTime are converted to Local*
          if (argument.getCanonicalName().equals("Date")) {
            return "Date";
          }

          if (argument.getCanonicalName().equals("Time")) {
            return "Date";
          }

          if (argument.getCanonicalName().equals("DateTime")) {
            return "Date";
          }

          if (argument.getCanonicalName().equals("LocalDate")) {
            return "LocalDate";
          }

          if (argument.getCanonicalName().equals("LocalTime")) {
            return "LocalTime";
          }

          if (argument.getCanonicalName().equals("LocalDateTime")) {
            return "LocalDateTime";
          }

          if (argument.getCanonicalName().equals("Decimal")) {
            return "BigDecimal";
          }

          if (argument.getCanonicalName().equals("Boolean")) {
            return "boolean";
          }

          // get type of primitive type mappings
          if (argument instanceof PrimitiveMapping) {
            return ((PrimitiveMapping) argument).getMappedPrimitive().getName();
          }

          // else return argument
          return argument.getName();
        }
      }

      // parameter has wrong type
      throw new TemplateModelException("[toJavaType] Argument is of type " + arguments.get(0).getClass().getCanonicalName() + ". Should be rocks.xprs.languages.xprsm.model.Type.");
    }

    // no parameter given, throw exception
    throw new TemplateModelException("[toJavaType] Argument missing.");
  }

}
