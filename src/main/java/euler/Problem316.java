package euler;

import com.google.common.math.LongMath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Problem316 {

  public static void main(String[] args) {
    Runner.run(new Problem316()::solve);
  }

  public long solve() {
    long sum = 0;
    for (int n = 999999; n >= 2; n--) {
      long res = count(LongMath.pow(10, 16) / n);
      System.out.println(n + ": " + res);
      sum += res;
    }
    return sum;
  }

  private final Map<BigDecimalMatrix2, Long> cache = new HashMap<>();

  private static final MathContext CTX = new MathContext(40);
  private static final BigDecimal ONE_TENTH = new BigDecimal("0.1");

  private long count(long n) {
    String nstr = String.valueOf(n);
    int d = nstr.length();
    double[][] startArr = new double[1][d + 3];
    startArr[0][0] = 1;
    startArr[0][d + 1] = 1;
    startArr[0][d + 2] = 0;
    BigDecimalMatrix2 p = BigDecimalMatrix2.create(startArr, CTX);

    BigDecimal[][] transitionArr = new BigDecimal[d + 3][d + 3];
    for (BigDecimal[] row : transitionArr) {
      Arrays.fill(row, BigDecimal.ZERO);
    }
    for (int l = 0; l < d; l++) {
      for (int digit = 0; digit <= 9; digit++) {
        String prefix = nstr.substring(0, l) + digit;
        while (!nstr.startsWith(prefix)) {
          prefix = prefix.substring(1, prefix.length());
        }
        transitionArr[l][prefix.length()] = transitionArr[l][prefix.length()].add(ONE_TENTH);
        if (prefix.length() == d) {
          transitionArr[l][d + 1] = transitionArr[l][d + 1].subtract(ONE_TENTH);
          transitionArr[l][d + 2] = transitionArr[l][d + 2].subtract(ONE_TENTH);
        }
      }
    }
    transitionArr[d + 1][d + 1] = BigDecimal.ONE;
    transitionArr[d + 1][d + 2] = BigDecimal.ONE;
    transitionArr[d + 2][d + 2] = BigDecimal.ONE;
    BigDecimalMatrix2 transition = BigDecimalMatrix2.create(transitionArr, CTX);

    if (cache.containsKey(transition)) {
      return cache.get(transition);
    }

    BigDecimal g = BigDecimal.ZERO;
    BigDecimalMatrix2 transitionPow = transition;
    while (true) {
      transitionPow = removeSmallNumbers(transitionPow.pow(2));
      BigDecimalMatrix2 res = p.multiply(transitionPow);
      BigDecimal newg = res.element(0, d + 2);
      if (newg.subtract(g).abs().compareTo(BigDecimal.valueOf(1e-2)) < 0) {
        break;
      }
      g = newg;
    }

    long res = g.setScale(0, RoundingMode.HALF_UP).longValueExact() - (d - 2);
    cache.put(transition, res);
    return res;
  }

  private BigDecimalMatrix2 removeSmallNumbers(BigDecimalMatrix2 m) {
    BigDecimal[][] arr = new BigDecimal[m.rowCount()][m.columnCount()];
    for (int r = 0; r < m.rowCount(); r++) {
      for (int c = 0; c < m.columnCount(); c++) {
        arr[r][c] = m.element(r, c).scale() > 100000 ? BigDecimal.ZERO : m.element(r, c);
      }
    }
    return BigDecimalMatrix2.create(arr, m.getMathContext());
  }
}
