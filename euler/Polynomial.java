package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

public abstract class Polynomial<T extends Comparable<T>, P extends Polynomial<T, P>> {

  private final T[] coefs;

  @SuppressWarnings("unchecked")
  protected Polynomial(T[] coefs) {
    for (T coef : coefs) {
      checkNotNull(coef);
    }
    int coefCount = coefs.length;
    while (coefCount > 0 && coefs[coefCount - 1].equals(field().zero())) {
      coefCount--;
    }
    if (coefCount == 0) {
      this.coefs = (T[]) Array.newInstance(field().clazz(), 1);
      this.coefs[0] = field().zero();
    } else {
      if (coefCount == coefs.length) {
        this.coefs = coefs;
      } else {
        this.coefs = Arrays.copyOf(coefs, coefCount);
      }
    }
  }

  protected abstract Field<T> field();
  protected abstract P newInstance(T[] coefs);

  @SuppressWarnings("unchecked")
  private T[] newCoefs(int degree) {
    return (T[]) Array.newInstance(field().clazz(), degree + 1);
  }

  private P zero() {
    T[] coefs = newCoefs(0);
    coefs[0] = field().zero();
    return newInstance(coefs);
  }

  protected P term(T coef, int degree) {
    T[] coefs = newCoefs(degree);
    Arrays.fill(coefs, field().zero());
    coefs[degree] = coef;
    return newInstance(coefs);
  }

  public T coef(int n) {
    checkArgument(n >= 0);
    return n >= coefs.length ? field().zero() : coefs[n];
  }

  public int degree() {
    return coefs.length - 1;
  }

  public T evaluate(T x) {
    T result = field().zero();
    for (int n = coefs.length - 1; n >= 0; n--) {
      result = field().multiply(result, x);
      result = field().add(result, coefs[n]);
    }
    return result;
  }

  public P add(P that) {
    int maxDegree = Math.max(this.degree(), that.degree());
    T[] newCoefs = newCoefs(maxDegree);
    for (int n = 0; n <= maxDegree; n++) {
      newCoefs[n] = field().add(this.coef(n), that.coef(n));
    }
    return newInstance(newCoefs);
  }

  public P subtract(P that) {
    int maxDegree = Math.max(this.degree(), that.degree());
    T[] newCoefs = newCoefs(maxDegree);
    for (int n = 0; n <= maxDegree; n++) {
      newCoefs[n] = field().subtract(this.coef(n), that.coef(n));
    }
    return newInstance(newCoefs);
  }

  public P multiply(P that) {
    T[] newCoefs = newCoefs(this.degree() + that.degree());
    Arrays.fill(newCoefs, field().zero());
    for (int n1 = 0; n1 <= this.degree(); n1++) {
      for (int n2 = 0; n2 <= that.degree(); n2++) {
        newCoefs[n1 + n2] =
            field().add(newCoefs[n1 + n2], field().multiply(this.coef(n1), that.coef(n2)));
      }
    }
    return newInstance(newCoefs);
  }

  public P multiply(T constant) {
    T[] newCoefs = newCoefs(degree());
    for (int n = 0; n <= degree(); n++) {
      newCoefs[n] = field().multiply(this.coef(n), constant);
    }
    return newInstance(newCoefs);
  }

  public P divide(P that) {
    P zero = zero();
    checkArgument(!that.equals(zero));
    @SuppressWarnings("unchecked")
    P remaining = (P) this;
    P result = zero();
    while (!remaining.equals(zero) && remaining.degree() >= that.degree()) {
      P term = term(
          field().divide(remaining.coef(remaining.degree()), that.coef(that.degree())),
          remaining.degree() - that.degree());
      result = result.add(term);
      remaining = remaining.subtract(term.multiply(that));
    }
    return result;
  }

  public P mod(P that) {
    return this.subtract(this.divide(that).multiply(that));
  }

  public P derive() {
    if (degree() == 0) {
      return zero();
    } else {
      T[] newCoefs = newCoefs(degree() - 1);
      for (int n = 0; n < degree(); n++) {
        newCoefs[n] = field().multiply(coef(n + 1), field().get(n + 1));
      }
      return newInstance(newCoefs);
    }
  }

  public P integrate() {
    T[] newCoefs = newCoefs(degree() + 1);
    newCoefs[0] = field().zero();
    for (int n = 1; n <= degree() + 1; n++) {
      newCoefs[n] = field().divide(coef(n - 1), field().get(n));
    }
    return newInstance(newCoefs);
  }

  public T integrate(T from, T to) {
    P integral = integrate();
    return field().subtract(integral.evaluate(to), integral.evaluate(from));
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(coefs);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Polynomial) {
      Polynomial<?, ?> that = (Polynomial<?, ?>) obj;
      return Objects.deepEquals(this.coefs, that.coefs);
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (int n = coefs.length - 1; n >= 0; n--) {
      if (!coefs[n].equals(field().zero()) || coefs.length == 1) {
        if (builder.length() > 0 && coefs[n].compareTo(field().zero()) >= 0) {
          builder.append("+");
        }
        if (!coefs[n].equals(field().one()) || n == 0) {
          builder.append(coefs[n]);
        }
        if (n > 0) {
          builder.append("r");
        }
        if (n > 1) {
          builder.append("^" + n);
        }
      }
    }
    return builder.toString();
  }
}