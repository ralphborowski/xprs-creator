package rocks.xprs.types;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 *
 * @author Ralph Borowski <ralph.borowski@borowski.it>
 */
@Embeddable
public class Monetary {

  private static final int SCALE = 4;
  private static final int PRECISION = 24;
  private static MathContext mc = new MathContext(PRECISION, RoundingMode.HALF_UP);
  private BigDecimal amount;
  private String currency;

  public Monetary() {

  }

  public Monetary(String value, String currency) {
    this.amount = new BigDecimal(value, mc);
    this.currency = currency;
  }

  public Monetary(BigDecimal amount, String currency) {
    this.amount = amount.setScale(SCALE, RoundingMode.HALF_UP);
    this.currency = currency;
  }

  public Monetary add(long summand) {
    return add(new BigDecimal(summand, mc));
  }

  public Monetary add(double summand) {
    return add(new BigDecimal(summand, mc));
  }

  public Monetary add(BigDecimal summand) {
    return new Monetary(amount.add(summand), this.getCurrency());
  }

  public Monetary add(Monetary summand) throws ArithmeticException {
    if (!getCurrency().equalsIgnoreCase(summand.getCurrency())) {
      throw new ArithmeticException(String.format("Adding %s to %s not possible. Please convert to the same currency first.",
              summand.getCurrency(), this.getCurrency()));
    }
    return add(summand.getAmount());
  }

  public Monetary subtract(long subtrahend) {
    return subtract(new BigDecimal(subtrahend));
  }

  public Monetary subtract(double subtrahend) {
    return subtract(new BigDecimal(subtrahend));
  }

  public Monetary subtract(BigDecimal subtrahend) {
    return new Monetary(amount.subtract(subtrahend), this.getCurrency());
  }

  public Monetary subtract(Monetary subtrahend) throws ArithmeticException {
    if (!getCurrency().equalsIgnoreCase(subtrahend.getCurrency())) {
      throw new ArithmeticException(String.format("Substracting %s from %s not possible. Please convert to the same currency first.",
              subtrahend.getCurrency(), this.getCurrency()));
    }
    return subtract(subtrahend.getAmount());
  }

  public Monetary multiply(long factor) {
    return multiply(new BigDecimal(factor));
  }

  public Monetary multiply(double factor) {
    return multiply(new BigDecimal(factor));
  }

  public Monetary multiply(BigDecimal factor) {
    return new Monetary(amount.multiply(factor), this.getCurrency());
  }

  public Monetary divide(long divisor) {
    return divide(new BigDecimal(divisor));
  }

  public Monetary divide(double divisor) {
    return divide(new BigDecimal(divisor));
  }

  public Monetary divide(BigDecimal divisor) {
    return new Monetary(amount.divide(divisor, mc), this.getCurrency());
  }

  public Monetary negate() {
    return new Monetary(amount.negate(), this.getCurrency());
  }

  public Monetary abs() {
    return new Monetary(amount.abs(), this.getCurrency());
  }

  public Monetary convert(BigDecimal exchangeRate, String newCurrency) {
    return new Monetary(amount.multiply(exchangeRate), newCurrency);
  }

  public boolean isNegative() {
    return this.getAmount().compareTo(BigDecimal.ZERO) < 0;
  }

  public Monetary round() {
    return new Monetary(this.getAmount().setScale(2, RoundingMode.HALF_UP), this.getCurrency());
  }

  @Override
  public boolean equals(Object o) {
    if (o != null && o instanceof Monetary) {
      Monetary m = (Monetary) o;
      return Objects.equals(m.getAmount(), getAmount())
              && Objects.equals(m.getCurrency(), getCurrency());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 47 * hash + Objects.hashCode(this.amount);
    hash = 47 * hash + Objects.hashCode(this.currency);
    return hash;
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the amount
   */
  public BigDecimal getAmount() {
    return amount;
  }

  /**
   * @param amount the amount to set
   */
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /**
   * @return the currency
   */
  public String getCurrency() {
    return currency;
  }

  /**
   * @param currency the currency to set
   */
  public void setCurrency(String currency) {
    this.currency = currency;
  }
  //</editor-fold>
}
