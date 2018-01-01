package euler;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableSet;
import com.google.common.math.IntMath;

public class Problem368 {

  private static final MathContext CTX = new MathContext(17);
  private static final BigDecimal EPS = BigDecimal.valueOf(1, 30);

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.printf("%.10f\n", new Problem368().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public BigDecimal solve() {
    BigDecimal sum = BigDecimal.ZERO;
    for (int digits = 1;; digits++) {
      BigDecimal prevSum = sum;
      for (int d = 0; d <= 99; d++) {
        sum = sum.add(a(digits, 1, d), CTX);
      }
      if (sum.subtract(prevSum, CTX).compareTo(EPS) <= 0) {
        return sum;
      }
    }
  }

  @AutoValue
  static abstract class AKey {
    abstract int digits();
    abstract int j();
    abstract int d();

    static AKey create(int digits, int j, int d) {
      return new AutoValue_Problem368_AKey(digits, j, d);
    }
  }

  private final Map<AKey, BigDecimal> aCache = new HashMap<>();

  private BigDecimal a(int digits, int j, int d) {
    AKey key = AKey.create(digits, j, d);
    BigDecimal result = aCache.get(key);
    if (result != null) {
      return result;
    }
    result = BigDecimal.ZERO;
    if (digits == 1) {
      result = d >= 1 && d <= 9 ? BigDecimal.ONE.divide(BigDecimal.valueOf(d).pow(j, CTX), CTX) : BigDecimal.ZERO;
    } else if (digits == 2) {
      result = d >= 10 && d <= 99 ? BigDecimal.ONE.divide(BigDecimal.valueOf(d).pow(j, CTX), CTX) : BigDecimal.ZERO;
    } else {
      BigDecimal prevResult = BigDecimal.valueOf(-1);
      for (int n = 0; result.subtract(prevResult, CTX).abs().compareTo(EPS) >= 0; n++) {
        prevResult = result;
        for (int pd = 0; pd <= 9; pd++) {
          if (!ImmutableSet.of(0, 111, 222, 333, 444, 555, 666, 777, 888, 999).contains(pd * 100 + d)) {
            result = result.add(a(digits - 1, j + n, pd * 10 + d / 10)
                .multiply(factor(n, j, d % 10), CTX), CTX);
          }
        }
      }
    }
    aCache.put(key, result);
    return result;
  }

  @AutoValue
  static abstract class FactorKey {
    abstract int n();
    abstract int j();
    abstract int d();

    static FactorKey create(int n, int j, int d) {
      return new AutoValue_Problem368_FactorKey(n, j, d);
    }
  }

  private final Map<FactorKey, BigDecimal> factorCache = new HashMap<>();

  private BigDecimal factor(int n, int j, int d) {
    FactorKey cacheKey = FactorKey.create(n, j, d);
    BigDecimal result = factorCache.get(cacheKey);
    result = BigDecimal.valueOf(IntMath.pow(-1, n))
        .multiply(factorial(j + n - 1), CTX)
        .multiply(BigDecimal.valueOf(d).pow(n, CTX))
        .divide(factorial(n)
            .multiply(factorial(j - 1), CTX)
            .multiply(BigDecimal.valueOf(10).pow(j + n, CTX), CTX), CTX);
    factorCache.put(cacheKey, result);
    return result;
  }

  private final Map<Integer, BigDecimal> factorialCache = new HashMap<>();

  private BigDecimal factorial(int n) {
    BigDecimal result = factorialCache.get(n);
    if (result != null) {
      return result;
    }
    if (n == 0) {
      result = BigDecimal.ONE;
    } else {
      result = BigDecimal.valueOf(n).multiply(factorial(n - 1), CTX);
    }
    factorialCache.put(n, result);
    return result;
  }
}
