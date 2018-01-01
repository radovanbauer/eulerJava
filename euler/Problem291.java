package euler;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import com.google.common.base.Stopwatch;

public class Problem291 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem291().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long max = 5_000_000_000_000_000L;
    return LongStream.rangeClosed(1, 50_000_000L).parallel()
        .map(n -> n * n + (n + 1) * (n + 1))
        .filter(m -> m <= max && BigInteger.valueOf(m).isProbablePrime(10))
        .count();
  }
}
