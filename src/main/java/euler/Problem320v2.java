package euler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem320v2 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem320v2().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 1_000_000;
    long mod = 1_000_000_000_000_000_000L;
    long multiplier = 1234567890;
    FactorizationSieve sieve = new FactorizationSieve(max);
    long sum = 0;
    Map<Integer, Long> nFact = new HashMap<>();
    long low = 1;
    for (int i = 1; i <= max; i++) {
      Map<Integer, Long> iFact = factorization(i, sieve);
      addFactorizations(nFact, iFact);
      for (Map.Entry<Integer, Long> entry : iFact.entrySet()) {
        int p = entry.getKey();
        long k = nFact.get(p) * multiplier;
        low = Math.max(low, k * (p - 1));
        long high = p;
        while (high <= k * (p - 1) + 1) {
          high *= p;
        }
        while (low + 1 < high) {
          long mid = (low + high) / 2;
          if (getCoef(mid, p) >= k) {
            high = mid;
          } else {
            low = mid;
          }
        }
        if (getCoef(low, p) < k) {
          low++;
        }
      }
      if (i >= 10) {
        sum = LongMath.mod(sum + low, mod);
      }
    }
    return sum;
  }

  private Map<Integer, Long> factorization(long n, FactorizationSieve sieve) {
    HashMap<Integer, Long> result = new HashMap<>();
    while (n > 1) {
      int p = sieve.smallestPrimeDivisor(n);
      long exp = 0;
      while (n % p == 0) {
        exp++;
        n /= p;
      }
      result.put(p, exp);
    }
    return result;
  }

  private void addFactorizations(Map<Integer, Long> f1, Map<Integer, Long> f2) {
    for (Map.Entry<Integer, Long> entry : f2.entrySet()) {
      if (!f1.containsKey(entry.getKey())) {
        f1.put(entry.getKey(), entry.getValue());
      } else {
        f1.put(entry.getKey(), f1.get(entry.getKey()) + entry.getValue());
      }
    }
  }

  private long getCoef(long n, int p) {
    long sum = 0;
    long m = n;
    while (m > 0) {
      sum += m % p;
      m /= p;
    }
    return (n - sum) / (p - 1);
  }
}
