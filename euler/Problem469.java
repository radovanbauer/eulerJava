package euler;

import java.math.BigDecimal;
import java.math.MathContext;

public class Problem469 {

  public static void main(String[] args) {
    System.out.println(new Problem469().solve());
  }

  public String solve() {
    MathContext ctx = MathContext.DECIMAL128;
    BigDecimal x0 = BigDecimal.ONE.divide(BigDecimal.valueOf(2), ctx);
    BigDecimal x1 = BigDecimal.valueOf(2).divide(BigDecimal.valueOf(3), ctx);
    int n = 3;
    while (x1.subtract(x0).abs().compareTo(BigDecimal.valueOf(1, 30)) > 0) {
      n++;
      BigDecimal x = BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(n - 2), ctx).multiply(x0, ctx)
          .add(BigDecimal.valueOf(n - 4).multiply(BigDecimal.valueOf(n - 1), ctx).multiply(x1, ctx), ctx)
          .divide(BigDecimal.valueOf(n).multiply(BigDecimal.valueOf(n - 3), ctx), ctx);
      System.out.println(n + ": " + x);
      x0 = x1;
      x1 = x;
    }
    return String.format("%.14f", x1);
  }
}
