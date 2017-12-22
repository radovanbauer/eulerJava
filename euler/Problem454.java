package euler;

import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Problem454 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem454().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long L = LongMath.pow(10, 12);
    long maxb = LongMath.sqrt(L, RoundingMode.FLOOR);
    FactorizationSieve sieve = new FactorizationSieve(maxb);
    AtomicInteger counter = new AtomicInteger();
    return LongStream.rangeClosed(1, maxb).parallel().map(b -> {
      long count = 0;
      int[] primes = Ints.toArray(sieve.primeDivisors(b));
      int[] mods = new int[primes.length];
      for (long a = 1; a < b && b * (a + b) <= L; a++) {
        boolean coprime = true;
        for (int i = 0; i < primes.length; i++) {
          mods[i]++;
          if (mods[i] == primes[i]) {
            mods[i] = 0;
            coprime = false;
          }
        }
        if (coprime) {
          count += L / (b * (a + b));
        }
      }
      int counterValue = counter.incrementAndGet();
      if ((counterValue & 0xFF) == 0) {
        System.out.println(counterValue);
      }
      return count;
    }).sum();
  }
}
