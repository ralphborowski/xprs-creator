/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.web;

import rocks.xprs.util.MultiValuedMap;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author borowski
 */
public class FormValidator {
  
  public static final String PATTERN_EMAIL = "[a-zA-Z0-9._+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]+";

  private final MultiValuedMap<String, String> inputData;
  private final Locale locale;
  private final MultiValuedMap<String, String> allErrors = new MultiValuedMap<>();

  public FormValidator(MultiValuedMap<String, String> inputData, Locale locale) {
    this.inputData = inputData;
    this.locale = locale;
  }

  public FieldValidator check(String fieldName) {
    return new FieldValidator(fieldName, inputData.get(fieldName));
  }

  public MultiValuedMap<String, String> getErrors() {
    return allErrors;
  }

  public class FieldValidator {

    private final MultiValuedMap<String, String> fieldErrors = new MultiValuedMap<>();
    private final String fieldname;
    private final List<String> values;

    private FieldValidator(String fieldname, List<String> values) {
      this.fieldname = fieldname;
      this.values = values;
    }

    public FieldValidator isRequired() {
      if (values == null || values.isEmpty()) {
        fieldErrors.addValue(fieldname, "required");
        return this;
      }

      for (int i = 0; i < values.size(); i++) {
        if (values.get(i) == null || values.get(i).trim().isEmpty()) {
          if (values.size() == 1) {
            fieldErrors.addValue(fieldname, "required");
          } else {
            fieldErrors.addValue(fieldname + "." + i, "required");
          }
        }
      }
      return this;
    }

    public FieldValidator isInteger() {
      if (values == null) {
        return this;
      }

      for (int i = 0; i < values.size(); i++) {

        // ignore empty values, checked by required
        if (values.get(i) == null || values.get(i).trim().isEmpty()) {
          return this;
        }

        try {
          Integer.parseInt(values.get(i));
        } catch (NumberFormatException ex) {
          if (values.size() == 1) {
            fieldErrors.addValue(fieldname, "integer");
          } else {
            fieldErrors.addValue(fieldname + "." + i, "integer");
          }
        }
      }

      return this;
    }

    public FieldValidator isLong() {
      if (values == null) {
        return this;
      }

      for (int i = 0; i < values.size(); i++) {

        // ignore empty values, checked by required
        if (values.get(i) == null || values.get(i).trim().isEmpty()) {
          return this;
        }

        try {
          Long.parseLong(values.get(i));
        } catch (NumberFormatException ex) {
          if (values.size() == 1) {
            fieldErrors.addValue(fieldname, "long");
          } else {
            fieldErrors.addValue(fieldname + "." + i, "long");
          }
        }
      }

      return this;
    }

    public FieldValidator isDouble() {
      if (values == null) {
        return this;
      }

      for (int i = 0; i < values.size(); i++) {

        // ignore empty values, checked by required
        if (values.get(i) == null || values.get(i).trim().isEmpty()) {
          return this;
        }

        try {
          NumberFormat.getNumberInstance(locale).parse(values.get(i));
        } catch (ParseException ex) {
          if (values.size() == 1) {
            fieldErrors.addValue(fieldname, "double");
          } else {
            fieldErrors.addValue(fieldname + "." + i, "double");
          }
        }
      }

      return this;
    }

    public FieldValidator isBigDecimal() {
      if (values == null) {
        return this;
      }

      for (int i = 0; i < values.size(); i++) {

        // ignore empty values, checked by required
        if (values.get(i) == null || values.get(i).trim().isEmpty()) {
          return this;
        }

        try {
          DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
          char sep = dfs.getDecimalSeparator();
          char gp = dfs.getGroupingSeparator();
          StringBuilder sb = new StringBuilder(values.get(i));
          for (int j = sb.length() - 1; j >= 0; j--) {
            if (sb.charAt(j) == gp) {
              sb.deleteCharAt(j);
            } else if (sb.charAt(j) == sep) {
              sb.setCharAt(j, '.');
            }
          }
          BigDecimal test = new BigDecimal(sb.toString());
        } catch (NumberFormatException ex) {
          if (values.size() == 1) {
            fieldErrors.addValue(fieldname, "bigDecimal");
          } else {
            fieldErrors.addValue(fieldname + "." + i, "bigDecimal");
          }
        }
      }

      return this;
    }

    public FieldValidator isDate() {
      if (values == null) {
        return this;
      }

      for (int i = 0; i < values.size(); i++) {

        // ignore empty values, checked by required
        if (values.get(i) == null || values.get(i).trim().isEmpty()) {
          return this;
        }

        try {
          DateFormat.getDateInstance(DateFormat.LONG, locale).parse(values.get(i));
        } catch (ParseException ex) {
          try {
            DateFormat.getDateInstance(DateFormat.SHORT, locale).parse(values.get(i));
          } catch (ParseException ex2) {
            if (values.size() == 1) {
              fieldErrors.addValue(fieldname, "date");
            } else {
              fieldErrors.addValue(fieldname + "." + i, "date");
            }
          }
        }
      }

      return this;
    }

    public FieldValidator isTime() {

      // ignore empty values, checked by required
      if (values == null) {
        return this;
      }

      for (int i = 0; i < values.size(); i++) {

        // ignore empty values, checked by required
        if (values.get(i) == null || values.get(i).trim().isEmpty()) {
          return this;
        }

        try {
          DateFormat.getTimeInstance(DateFormat.LONG, locale).parse(values.get(i));
        } catch (ParseException ex) {
          try {
            DateFormat.getTimeInstance(DateFormat.SHORT, locale).parse(values.get(i));
          } catch (ParseException ex2) {
            if (values.size() == 1) {
              fieldErrors.addValue(fieldname, "time");
            } else {
              fieldErrors.addValue(fieldname + "." + i, "time");
            }
          }
        }
      }

      return this;
    }

    public FieldValidator isDateTime() {
      if (values == null) {
        return this;
      }

      for (int i = 0; i < values.size(); i++) {

        // ignore empty values, checked by required
        if (values.get(i) == null || values.get(i).trim().isEmpty()) {
          return this;
        }
        
        try {
          DateFormat.getDateTimeInstance(DateFormat.LONG,
                  DateFormat.LONG, locale).parse(values.get(i));
        } catch (ParseException ex) {
          try {
            DateFormat.getDateTimeInstance(DateFormat.SHORT,
                    DateFormat.SHORT, locale).parse(values.get(i));
          } catch (ParseException ex2) {
            if (values.size() == 1) {
              fieldErrors.addValue(fieldname, "dateTime");
            } else {
              fieldErrors.addValue(fieldname + "." + i, "dateTime");
            }
          }
        }
      }

      return this;
    }
    
    public FieldValidator matches(String pattern) {
      if (values == null) {
        return this;
      }
      
      for (int i = 0; i < values.size(); i++) {

        // ignore empty values, checked by required
        if (values.get(i) == null || values.get(i).trim().isEmpty()) {
          return this;
        }
        
        if (!values.get(i).matches(pattern)) {
          if (values.size() == 1) {
              fieldErrors.addValue(fieldname, "matches");
            } else {
              fieldErrors.addValue(fieldname + "." + i, "matches");
            }
        }
      }
      return this;
    }

    public boolean isValid() {
      if (!fieldErrors.isEmpty()) {
        allErrors.putAll(fieldErrors);
      }
      return fieldErrors.isEmpty();
    }
  }
}
