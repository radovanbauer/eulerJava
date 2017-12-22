package euler;

import static com.google.common.math.LongMath.checkedMultiply;
import static com.google.common.math.LongMath.mod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import com.google.common.base.Stopwatch;

public class Problem455 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem455().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    return LongStream.rangeClosed(2, 1_000_000L).parallel().map(n -> f(n)).sum();
  }

  private long f(long n) {
    long mod = 1;
    List<Long> newNumbers = new ArrayList<>();
    newNumbers.add(0L);
    newNumbers.add(1L);
    for (int d = 1; d <= 9; d++) {
      mod *= 10;
      List<Long> oldNumbers = newNumbers;
      newNumbers = new ArrayList<>();
      for (long a = 0; a < 2 * mod; a += 2 * mod / 10) {
        for (long oldNumber : oldNumbers) {
          long newNumber = a + oldNumber;
          if (powMod(n, newNumber, mod) == mod(newNumber, mod)) {
            newNumbers.add(newNumber);
          }
        }
      }
    }
    return newNumbers.stream().filter(i -> i <= 1_000_000_000L)
        .max(Comparator.naturalOrder()).orElse(0L);
  }

  private long powMod(long base, long exp, long mod) {
    if (exp == 0) {
      return 1L;
    }
    long x = powMod(base, exp / 2, mod);
    if (exp % 2 == 0) {
      return mod(checkedMultiply(x, x), mod);
    } else {
      return mod(checkedMultiply(base, mod(checkedMultiply(x, x), mod)), mod);
    }
  }
}
