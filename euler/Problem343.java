package euler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

public class Problem343 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
//    System.out.println(new Problem343().solve());
    System.out.println(new Problem343().solve2());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int n = 100000;
    long[] primes = Longs.toArray(new FactorizationSieve(n + 1000).getAllPrimes());
    AtomicInteger counter = new AtomicInteger();
    return LongStream.rangeClosed(1, n).parallel().map(k -> {
//      List<LongFraction> sequence = new ArrayList<>();
//      LongFraction frac = LongFraction.create(1, k * k * k);
//      sequence.add(frac);
//      while (frac.d() != 1) {
//        frac = LongFraction.create(frac.n() + 1, frac.d() - 1);
//        sequence.add(frac);
//      }
//      System.out.println(k + ": " + sequence);
      int counterValue = counter.incrementAndGet();
      if ((counterValue & 0xFFFF) == 0) {
//        System.out.println(counterValue);
      }
      return Math.max(largestPrimeDivisor(primes, k + 1), largestPrimeDivisor(primes, k * k - k + 1)) - 1;
//      checkState(frac.n() == p - 1, "%s vs %s", frac.n(), p - 1);
    }).sum();
  }

  private long solve2() {
    int n = 2_000_000;
    long[] maxp1 = new long[n + 1];
    long[] num2 = new long[n + 1];
    long[] maxp2 = new long[n + 1];
    long[] rem2 = new long[n + 1];
    Map<Long, Integer> squareMap = new HashMap<>();
    FactorizationSieve sieve = new FactorizationSieve(n + 1);
    int smallPrimeLimit = Math.min(10_000, n);
    ImmutableList<Long> smallPrimes = sieve.getPrimes(smallPrimeLimit);
    for (int i = 1; i <= n; i++) {
      if ((i & 0xFFFF) == 0) {
        System.out.println(i);
      }
      long x = 1L * i * i - i + 1;
      squareMap.put(x, i);
      num2[i] = x;
      rem2[i] = x;
      for (long p : smallPrimes) {
        if (x % p == 0) {
          maxp2[i] = Math.max(maxp2[i], p);
          while (rem2[i] % p == 0) {
            rem2[i] /= p;
          }
        }
      }
    }
    for (long p : sieve.getAllPrimes()) {
      System.out.println(p);
      for (long px = p; px <= n + 1; px += p) {
        maxp1[Ints.checkedCast(px - 1)] = Math.max(maxp1[Ints.checkedCast(px - 1)], p);
      }
      if (p > smallPrimeLimit) {
        int i = -1;
        for (long px = p; px <= 1L * n * n - n + 1; px += p) {
          while (i + 1 < n && num2[i + 1] <= px) {
            i++;
          }
          if (i >= 0 && num2[i] == px) {
            maxp2[i] = Math.max(maxp2[i], p);
            while (rem2[i] % p == 0) {
              rem2[i] /= p;
            }
          }
        }
      }
    }
    long sum = 0;
    for (int i = 1; i <= n; i++) {
      sum += Math.max(maxp1[i], rem2[i] == 1 ? maxp2[i] : rem2[i]) - 1;
    }
    return sum;
  }

  private long largestPrimeDivisor(long[] primes, long n) {
    int pidx = 0;
    long d = primes[pidx];
    while (d * d <= n) {
      while (n % d == 0) {
        n /= d;
        if (n == 1) {
          return d;
        }
      }
      d = primes[++pidx];
    }
    return n;
  }
}
