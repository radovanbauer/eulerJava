package euler;

import static java.math.BigDecimal.ONE;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.google.common.math.LongMath;


public class Problem318 {

  public static void main(String[] args) {
    System.out.println(new Problem318().solve());
  }

  public long solve() {
    MathContext ctx = new MathContext(30);
    int max = 2011;
    long sum = 0;
    for (int p = 1; p <= max; p++) {
      System.out.println(p);
      for (int q = p + 1; p + q <= max; q++) {
        if (!isSquare(p * q)) {
          BigDecimal a1 =
              sqrt(BigDecimal.valueOf(p), ctx).add(sqrt(BigDecimal.valueOf(q), ctx)).pow(2, ctx);
          BigDecimal a2 =
              sqrt(BigDecimal.valueOf(p), ctx).add(sqrt(BigDecimal.valueOf(q), ctx)).pow(4, ctx);
          BigDecimal b1 = ONE.subtract(a1).add(a1.setScale(0, RoundingMode.FLOOR));
          BigDecimal b2 = ONE.subtract(a2).add(a2.setScale(0, RoundingMode.FLOOR));
          BigDecimal x = b1.multiply(b1).subtract(b2).abs();
          if (x.doubleValue() < 1e-15) {
            double cc = -2011 * Math.log(10) / Math.log(b1.doubleValue());
            int c = (int) cc + 1;
            sum += c;
          }
        }
      }
    }
    return sum;
  }

  private boolean isSquare(long n) {
    long sqrt = LongMath.sqrt(n, RoundingMode.FLOOR);
    return sqrt * sqrt == n;
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
