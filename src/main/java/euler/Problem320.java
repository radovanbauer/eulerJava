package euler;

import static com.google.common.math.LongMath.checkedMultiply;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;

public class Problem320 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem320().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 1_000_000;
    long mod = 1_000_000_000_000_000_000L;
    long multiplier = 1234567890;
    FactorizationSieve sieve = new FactorizationSieve(max);
    List<Integer> primes = new ArrayList<>();
    for (int p = 2; p <= max; p++) {
      if (sieve.isPrime(p)) {
        primes.add(p);
      }
    }
    AtomicInteger counter = new AtomicInteger();
    return IntStream.rangeClosed(10, max).parallel().mapToLong(i -> {
      int counterValue = counter.incrementAndGet();
      if ((counterValue & 0xFFFF) == 0) {
        System.out.println(counterValue);
      }
      ImmutableList<Long> f1 = factorialFactorization(i, primes);
      List<Long> mins = new ArrayList<>();
      for (int j = 0; j < f1.size(); j++) {
        mins.add(checkedMultiply(checkedMultiply(f1.get(j), multiplier), primes.get(j) - 1));
      }
      long x = Collections.max(mins);
      int j = 0;
      nextX: for (;; x++) {
        for (; j < f1.size(); j++) {
          if (checkedMultiply(f1.get(j), multiplier) > getCoef(x, primes.get(j))) {
            continue nextX;
          }
        }
        break;
      }
      return x;
    }).reduce(0, (a, b) -> LongMath.mod(a + b, mod));
  }

  private ImmutableList<Long> factorialFactorization(long n, List<Integer> primes) {
    List<Long> result = new ArrayList<>();
    for (int p : primes) {
      long exp = getCoef(n, p);
      if (exp == 0) {
        break;
      }
      result.add(exp);
    }
    return ImmutableList.copyOf(result);
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
