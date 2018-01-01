package euler;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import com.google.common.base.Stopwatch;

public class Problem304 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem304().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public BigInteger solve() {
    long mod = 1234567891011L;
    return LongStream.rangeClosed(100_000_000_000_000L, 100_000_004_000_000L).parallel()
        .filter(n -> BigInteger.valueOf(n).isProbablePrime(20))
        .limit(100_000)
        .mapToObj(a -> fib(a, mod))
        .reduce(BigMod.create(0, mod), (i1, i2) -> i1.add(i2)).n();
  }

  private BigMod fib(long n, long mod) {
    return BigModMatrix.create(new long[][] {{1, 1}, {1, 0}}, mod).pow(n).element(1, 0);
  }
}
