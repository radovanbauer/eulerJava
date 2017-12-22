package euler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import com.google.common.math.IntMath;

import com.google.common.base.Stopwatch;
import com.google.common.primitives.Ints;

public class Problem518 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem518().solve2());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  // (a + 1)(c + 1) == (b + 1)^2
  public long solve1() {
    int n = 100_000_000;
    FactorizationSieve sieve = new FactorizationSieve(n);
    return LongStream.rangeClosed(2, n).parallel().map(b -> {
      long sum = 0;
      if (sieve.isPrime(b)) {
        for (long d1 : divisorsOfProduct(b + 1, b + 1, sieve)) {
          long d2 = 1L * (b + 1) * (b + 1) / d1;
          if (d1 - 1 < b && d2 - 1 > b && d2 - 1 <= n) {
            int a = Ints.checkedCast(d1 - 1);
            int c = Ints.checkedCast(d2 - 1);
            if (sieve.isPrime(a) && sieve.isPrime(c)) {
              sum += a + b + c;
            }
          }
        }
      }
      return sum;
    }).sum();
  }

  private Collection<Long> divisorsOfProduct(long a, long b, FactorizationSieve sieve) {
    Set<Long> res = new HashSet<>();
    for (long d1 : sieve.divisors(a)) {
      for (long d2 : sieve.divisors(b)) {
        res.add(d1 * d2);
      }
    }
    return res;
  }

  // b + 1 == k/l * (a + 1), c + 1 == k^2/l^2 * (a + 1), gcd(k, l) == 1
  public long solve2() {
    int n = 100_000_000;
    FactorizationSieve sieve = new FactorizationSieve(n);
    return IntStream.rangeClosed(2, n).parallel().mapToLong(a -> {
      long sum = 0;
      if (sieve.isPrime(a)) {
        for (long d : sieve.divisors(a + 1)) {
          if (d * d <= a + 1 && (a + 1) % (d * d) == 0) {
            int l = Ints.checkedCast(d);
            for (int k = l + 1; 1L * (a + 1) * k * k <= 1L * l * l * (n + 1); k++) {
              if (IntMath.gcd(k, l) == 1) {
                int b = (a + 1) / l * k - 1;
                int c = (a + 1) / (l * l) * (k * k) - 1;
                if (sieve.isPrime(b) && sieve.isPrime(c)) {
                  sum += a + b + c;
                }
              }
            }
          }
        }
      }
      return sum;
    }).sum();
  }
}
