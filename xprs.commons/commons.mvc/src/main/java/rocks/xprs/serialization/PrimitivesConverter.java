/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.serialization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import rocks.xprs.types.Monetary;

/**
 *
 * @author borowski
 */
public class PrimitivesConverter extends Converter {

  private final Locale locale;
  private final DateFormat dateTimeFormat;
  private final NumberFormat numberFormat;
  private final NumberFormat currencyFormat;
  private final DateTimeFormatter dateFormatter;
  private final DateTimeFormatter timeFormatter;
  private final DateTimeFormatter dateTimeFormatter;

  public PrimitivesConverter(Locale locale) {
    super(locale);
    this.locale = locale;
    dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
    numberFormat = NumberFormat.getNumberInstance(locale);
    currencyFormat = NumberFormat.getCurrencyInstance(locale);
    currencyFormat.setMinimumFractionDigits(2);
    currencyFormat.setMaximumFractionDigits(2);

    String datePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            FormatStyle.MEDIUM, null, Chronology.ofLocale(locale), locale);
    dateFormatter = DateTimeFormatter.ofPattern(datePattern, locale);

    String timePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            null, FormatStyle.MEDIUM, Chronology.ofLocale(locale), locale);
    timeFormatter = DateTimeFormatter.ofPattern(timePattern, locale);

    String dateTimePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            FormatStyle.MEDIUM, FormatStyle.MEDIUM, Chronology.ofLocale(locale), locale);
    dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern, locale);
  }

  @Override
  public List<Class> canFormat() {
    return Arrays.asList(Integer.class, Long.class, Short.class, Float.class, Double.class,
            BigInteger.class, BigDecimal.class, Character.class, String.class, Boolean.class,
            Date.class, Monetary.class, LocalDate.class, LocalTime.class, LocalDateTime.class);
  }

  @Override
  public String format(Object object) {
    if (object == null) {
      return "";
    }

    if (object instanceof String || object instanceof Character) {
      return object.toString();
    }

    if (object instanceof Enum) {
      return ((Enum) object).toString();
    }

    if (object instanceof Boolean) {
      return object.toString();
    }

    if (object instanceof Date) {
      return dateTimeFormat.format((Date) object);
    }

    if (object instanceof LocalDate) {
      return ((LocalDate) object).format(dateFormatter);
    }

    if (object instanceof LocalTime) {
      return ((LocalTime) object).format(timeFormatter);
    }

    if (object instanceof LocalDateTime) {
      return ((LocalDateTime) object).format(dateTimeFormatter);
    }

    if (object instanceof Monetary) {
      Monetary monetary = (Monetary) object;
      monetary = monetary.round();
      return String.format(locale, "%.2f %s",
              monetary.getAmount(),
              monetary.getCurrency());
    }

    return numberFormat.format(object);
  }

  @Override
  public <T> T parse(String string, Class<T> targetClass) {

    if (string == null || string.isEmpty()) {
      return null;
    }

    if (targetClass.equals(String.class)) {
      return targetClass.cast(string);
    } else if (targetClass.equals(Character.class)) {
      return targetClass.cast(string.charAt(0));
    } else if (targetClass.equals(Boolean.class)) {
      return targetClass.cast((Boolean.parseBoolean(string)));
    }

    try {
      if (targetClass.equals(Date.class)) {
        return targetClass.cast(dateTimeFormat.parse(string));
      } else if (targetClass.equals(LocalDate.class)) {
        return (T) LocalDate.parse(string, dateFormatter);
      } else if (targetClass.equals(LocalTime.class)) {
        return (T) LocalTime.parse(string, timeFormatter);
      } else if (targetClass.equals(LocalDateTime.class)) {
        return (T) LocalDateTime.parse(string, dateTimeFormatter);
      }

      Number number = numberFormat.parse(string);

      if (targetClass.equals(Integer.class)) {
        return targetClass.cast(number.intValue());
      } else if (targetClass.equals(Long.class)) {
        return targetClass.cast(number.longValue());
      } else if (targetClass.equals(Short.class)) {
        return targetClass.cast(number.shortValue());
      } else if (targetClass.equals(Float.class)) {
        return targetClass.cast(number.floatValue());
      } else if (targetClass.equals(Double.class)) {
        return targetClass.cast(number.doubleValue());
      } else if (targetClass.equals(BigInteger.class)) {
        return targetClass.cast(new BigInteger(String.valueOf(number)));
      } else if (targetClass.equals(BigDecimal.class)) {
        return targetClass.cast(new BigDecimal(String.valueOf(number)));
      }
    } catch (ParseException ex) {
      // ignore wrong formats
    }
    return null;
  }

}
