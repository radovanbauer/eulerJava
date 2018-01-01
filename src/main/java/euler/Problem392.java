package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.math.BigDecimal.ONE;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.stream.IntStream;

public class Problem392 {

  public static void main(String[] args) {
    System.out.printf("%.10f\n", new Problem392().solve());
  }

  public BigDecimal solve() {
    int n = 400 / 2;
    BigDecimal[] x = new BigDecimal[n + 2];
    x[0] = BigDecimal.ZERO;
    x[n + 1] = BigDecimal.ONE;
    for (int i = 1; i <= n; i++) {
      x[i] = BigDecimal.valueOf(Math.sin(Math.PI / 2 / (n + 1) * i));
    }
    BigDecimal gain = BigDecimal.ONE.negate();
    BigDecimal prevFX = f(x);
    while (gain.compareTo(BigDecimal.ZERO) < 0) {
      BigDecimal gamma = new BigDecimal("0.01");
      BigDecimal[] newX = next(x, gamma);
      checkState(isValid(newX));
      x = newX;
      BigDecimal fX = f(x);
      gain = fX.subtract(prevFX);
      prevFX = fX;
      System.out.println(fX.multiply(BigDecimal.valueOf(4)));
    }
    return f(x).multiply(BigDecimal.valueOf(4));
  }

  private final MathContext CTX = new MathContext(17);

  private BigDecimal[] next(BigDecimal[] x, BigDecimal gamma) {
    return IntStream.range(0, x.length).parallel()
        .mapToObj(i -> i == 0 || i == x.length - 1
            ? x[i]
            : x[i].subtract(df(x, i).multiply(gamma), CTX)).toArray(BigDecimal[]::new); 
  }

  private boolean isValid(BigDecimal[] x) {
    for (int i = 1; i <= x.length - 1; i++) {
      if (x[i].compareTo(x[i - 1]) < 0) {
        return false;
      }
    }
    return true;
  }

  private BigDecimal f(BigDecimal[] x) {
    return IntStream.range(1, x.length).parallel()
        .mapToObj(i -> x[i].subtract(x[i - 1])
            .multiply(sqrt(ONE.subtract(x[i - 1].multiply(x[i - 1], CTX)), CTX), CTX))
        .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
  }

  private BigDecimal df(BigDecimal[] x, int i) {
    checkArgument(i > 0 && i < x.length - 1);
    BigDecimal x1 = x[i - 1];
    BigDecimal x2 = x[i];
    BigDecimal x3 = x[i + 1];
    return sqrt(ONE.subtract(x1.multiply(x1, CTX)), CTX)
        .add(BigDecimal.valueOf(2).multiply(x2, CTX).multiply(x2, CTX).subtract(x2.multiply(x3, CTX)).subtract(ONE)
            .divide(sqrt(ONE.subtract(x2.multiply(x2, CTX)), CTX), CTX));
  }

  private BigDecimal sqrt(BigDecimal x, MathContext ctx) {
    x = x.round(ctx);
    BigDecimal sqrt = BigDecimal.valueOf(Math.sqrt(x.doubleValue()));
    BigDecimal prev;
    do {
      prev = sqrt;
      sqrt = sqrt.add(x.divide(sqrt, ctx)).divide(BigDecimal.valueOf(2), ctx);
    } while (sqrt.compareTo(prev) != 0);
    return sqrt;
  }
}
