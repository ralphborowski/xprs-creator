/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.settings;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author borowski
 */
public class Setting {

  private final String value;

  public Setting(String value) {
    this.value = value;
  }

  public Boolean toBoolean() {
    if (value == null) {
      return null;
    }
    return Boolean.parseBoolean(value);
  }

  public Boolean toBoolean(Boolean defaultValue) {
    if (value == null) {
      return defaultValue;
    }
    return Boolean.parseBoolean(value);
  }

  public Integer toInteger() {
    if (value == null) {
      return null;
    }
    return Integer.parseInt(value);
  }

  public Integer toInteger(Integer defaultValue) {
    if (value == null) {
      return defaultValue;
    }
    return Integer.parseInt(value);
  }

  public Long toLong() {
    if (value == null) {
      return null;
    }
    return Long.parseLong(value);
  }

  public Long toLong(Long defaultValue) {
    if (value == null) {
      return defaultValue;
    }
    return Long.parseLong(value);
  }

  public Float toFloat() {
    if (value == null) {
      return null;
    }
    return Float.parseFloat(value);
  }

  public Float toFloat(Float defaultValue) {
    if (value == null) {
      return defaultValue;
    }
    return Float.parseFloat(value);
  }

  public Double toDouble() {
    if (value == null) {
      return null;
    }
    return Double.parseDouble(value);
  }

  public Double toDouble(Double defaultValue) {
    if (value == null) {
      return defaultValue;
    }
    return Double.parseDouble(value);
  }

  public BigDecimal toBigDecimal() {
    if (value == null) {
      return null;
    }
    return new BigDecimal(value);
  }

  public BigDecimal toBigDecimal(BigDecimal defaultValue) {
    if (value == null) {
      return defaultValue;
    }
    return new BigDecimal(value);
  }

  public Character toChar() {
    if (value == null) {
      return null;
    }
    return value.length() > 1 ? value.charAt(0) : null;
  }

  public Character toChar(Character defaultValue) {
    if (value == null) {
      return defaultValue;
    }
    return value.length() > 1 ? value.charAt(0) : null;
  }

  @Override
  public String toString() {
    if (value == null) {
      return null;
    }
    return value;
  }

  public String toString(String defaultValue) {
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  public Date toDate() {
    if (value == null) {
      return null;
    }
    try {
      return SimpleDateFormat.getDateTimeInstance().parse(value);
    } catch (ParseException ex) {
      return null;
    }
  }

  public Date toDate(Date defaultValue) {
    if (value == null) {
      return defaultValue;
    }
    try {
      return SimpleDateFormat.getDateTimeInstance().parse(value);
    } catch (ParseException ex) {
      return defaultValue;
    }
  }

}
