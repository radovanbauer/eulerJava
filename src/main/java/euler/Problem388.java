package euler;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem388 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem388().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public String solve() {
    BigInteger sum = BigInteger.ZERO;
    long n = 10_000_000_000L;
    long nsqrt = LongMath.sqrt(n, RoundingMode.FLOOR);
    // 3*Sum[EulerPhi[k]*k*S[n/k], {k, 2, Floor[Sqrt[n]]}]
    for (long k = 2; k <= nsqrt; k++) {
      sum = sum.add(BigInteger.valueOf(3)
          .multiply(totientKSum(k).subtract(totientKSum(k - 1)))
          .multiply(totientSum(n / k)));
    }
    // 3*Sum[(Sk[Floor[n/i]] - Sk[Floor[n/(i + 1)]])*S[i], {i, 1, n/Floor[Sqrt[n]] - 1}]
    for (long i = 1; (i + 1) * nsqrt <= n; i++) {
      sum = sum.add(BigInteger.valueOf(3)
          .multiply(totientSum(i))
          .multiply(totientKSum(n / i).subtract(totientKSum(n / (i + 1)))));
    }
    // 6*S[n] + 1
    sum = sum.add(BigInteger.valueOf(6).multiply(totientSum(n))).add(BigInteger.ONE);
    String sumStr = sum.toString();
    return sumStr.substring(0, 9) + sumStr.substring(sumStr.length() - 9, sumStr.length());
  }

  private final Map<Long, BigInteger> totientSumCache = new HashMap<>();

  private BigInteger totientSum(long n) {
    BigInteger res = totientSumCache.get(n);
    if (res != null) {
      return res;
    }
    // (n*(n + 1))/2
    res = BigInteger.valueOf(n % 2 == 0 ? n / 2 : n).multiply(BigInteger.valueOf(n % 2 == 0 ? n + 1 : (n + 1) / 2));
    // - Sum[S[Floor[n/i]], {i, 2, Floor[Sqrt[n]]}]
    long nSqrt = LongMath.sqrt(n, RoundingMode.FLOOR);
    for (int i = 2; i <= nSqrt; i++) {
      res = res.subtract(totientSum(n / i));
    }
    // - Sum[S[i]*(Floor[n/i] - Floor[n/(i + 1)]), {i, 1, n/Floor[Sqrt[n]] - 1}]
    for (int i = 1; (i + 1) * nSqrt <= n; i++) {
      res = res.subtract(totientSum(i).multiply(BigInteger.valueOf(n / i - n / (i + 1))));
    }
    totientSumCache.put(n, res);
    return res;
  }

  private final Map<Long, BigInteger> totientKSumCache = new HashMap<>();

  private BigInteger totientKSum(long n) {
    BigInteger res = totientKSumCache.get(n);
    if (res != null) {
      return res;
    }
    // (n*(n + 1) (2*n + 1))/6
    res = BigInteger.valueOf(n).multiply(BigInteger.valueOf(n + 1)).multiply(BigInteger.valueOf(2 * n + 1)).divide(BigInteger.valueOf(6));
    long nSqrt = LongMath.sqrt(n, RoundingMode.FLOOR);
    // - Sum[Sk[Floor[n/i]]*i, {i, 2, Floor[Sqrt[n]]}]
    for (int i = 2; i <= nSqrt; i++) {
      res = res.subtract(totientKSum(n / i).multiply(BigInteger.valueOf(i)));
    }
    // - Sum[Sk[i]*1/2 (Floor[n/i] - Floor[n/(i + 1)]) (1 + Floor[n/i] + Floor[n/(i + 1)]), {i, 1, n/Floor[Sqrt[n]] - 1}]
    for (int i = 1; (i + 1) * nSqrt <= n; i++) {
      res = res.subtract(totientKSum(i)
          .multiply(BigInteger.valueOf(n / i - n / (i + 1)))
          .multiply(BigInteger.valueOf(1 + n / i + n / (i + 1)))
          .divide(BigInteger.valueOf(2)));
    }
    totientKSumCache.put(n, res);
    return res;
  }
}
