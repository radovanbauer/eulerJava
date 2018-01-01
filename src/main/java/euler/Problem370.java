package euler;

import com.google.common.math.LongMath;
import com.google.common.primitives.Longs;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public class Problem370 {
  public static void main(String[] args) {
    Runner.run(new Problem370()::solve);
  }

  public long solve() {
//    long n = LongMath.pow(10, 6);
    long n = 25_000_000_000_000L;
    long nsqrt = LongMath.sqrt(n, RoundingMode.FLOOR);
    FactorizationSieve sieve = new FactorizationSieve(nsqrt);
    ArrayList<Long> ys = LongStream.rangeClosed(1, nsqrt).boxed().collect(Collectors.toCollection(ArrayList::new));
    Collections.shuffle(ys);
    return ys.stream().parallel().mapToLong(y -> {
      long count = 0;
      long xmin = (long) Math.ceil(y * (Math.sqrt(5) - 1) / 2);
      long xmax = y;
      checkState(xmin * xmin + xmin * y > y * y);
      checkState((xmin - 1) * (xmin - 1) + (xmin - 1) * y <= y * y);

      long x = xmin;
      while (x <= xmax) {
        long ratio = n / (x * x + x * y + y * y);
        if (ratio == 0) {
          break;
        }
        long nextRatio = n / ((x + 1) * (x + 1) + (x + 1) * y + y * y);
        if (nextRatio != ratio) {
          if (LongMath.gcd(x, y) == 1) {
            count += ratio;
          }
          x++;
        } else {
          long nx = (long) Math.ceil((-y + Math.sqrt((4D * n - 3 * ratio * y * y) / ratio)) / 2);
          checkState(n / (nx * nx + nx * y + y * y) < ratio, "n=%s, y=%s, ratio=%s, nx=%s", n, y, ratio, nx);
          checkState(n / ((nx - 1) * (nx - 1) + (nx - 1) * y + y * y) == ratio, "n=%s, y=%s, ratio=%s, nx=%s", n, y, ratio, nx);
          count += coprimeCount(y, x, Math.min(nx - 1, xmax), sieve) * ratio;
          x = nx;
        }
      }
      return count;
    }).sum();
  }

  private long coprimeCount(long n, long min, long max, FactorizationSieve sieve) {
    checkArgument(max >= min);
    return coprimeCount(n, max, sieve) - coprimeCount(n, min - 1, sieve);
  }

  private long coprimeCount(long n, long max, FactorizationSieve sieve) {
    return max - divCount(max, Longs.toArray(sieve.primeDivisors(n)), 0, 1, 0);
  }

  private long divCount(long max, long[] primes, int nextPrime, long product, int primeCount) {
    checkArgument(max >= 0);
    long count = 0;
    for (int next = nextPrime; next < primes.length; next++) {
      long newProduct = product * primes[next];
      int newPrimeCount = primeCount + 1;
      int sign = newPrimeCount % 2 == 1 ? 1 : -1;
      count += sign * max / newProduct;
      count += divCount(max, primes, next + 1, newProduct, newPrimeCount);
    }
    return count;
  }

}
