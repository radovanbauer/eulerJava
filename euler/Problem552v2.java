package euler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.primitives.Longs;

public class Problem552v2 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem552v2().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 300_000;
    FactorizationSieve sieve = new FactorizationSieve(max);
    long[] primes = Longs.toArray(sieve.getAllPrimes());
    long[] a = new long[primes.length];
    long[] prod = new long[primes.length];
    Arrays.fill(prod, 1);
    Set<Long> found = new HashSet<>();
    for (int i = 0; i < primes.length; i++) {
      long p = primes[i];
      long k = LongMod.create(prod[i], p).invert().multiply(i + 1 - a[i]).n();
      for (int j = i + 1; j < primes.length; j++) {
        long q = primes[j];
        a[j] = (a[j] + prod[j] * k) % q;
        if (a[j] == 0) {
          found.add(q);
        }
        prod[j] = (prod[j] * p) % q;
      }
    }
    return found.stream().reduce(0L, Long::sum);
  }
}
