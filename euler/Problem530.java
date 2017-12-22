package euler;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Problem530 {

  public static void main(String[] args) {
    System.out.println(new Problem530().solve());
  }

  private long solve() {
    long n = 1_000_000_000_000_000L;
    int m = Ints.checkedCast(LongMath.sqrt(n, RoundingMode.FLOOR));
    FactorizationSieve sieve = new FactorizationSieve(m);
    ArrayList<Integer> gcds = new ArrayList<>();
    IntStream.rangeClosed(1, m).forEach(i -> gcds.add(i));
    Collections.shuffle(gcds);
    return (1 + m) * m / 2 + gcds.stream().parallel().mapToLong(gcd -> {
      long sum = 0;
      for (long k = 1; k * gcd <= m; k++) {
        long maxl = n / (k * gcd * gcd);
        long count = 0;
        ImmutableList<Integer> primes = sieve.primeDivisors(k);
        for (long mask = 0; mask < (1L << primes.size()); mask++) {
          long div = 1;
          int primeCount = 0;
          for (int i = 0; i < primes.size(); i++) {
            if ((mask & (1L << i)) != 0) {
              div *= primes.get(i);
              primeCount++;
            }
          }
          count += (primeCount % 2 == 0 ? 1 : -1) * (maxl / div - k / div);
        }
        sum += 2 * gcd * count;
      }
      return sum;
    }).sum();
  }
}
