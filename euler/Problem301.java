package euler;

import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import com.google.common.base.Stopwatch;

public class Problem301 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem301().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    return LongStream.rangeClosed(1, 1L << 30).parallel().filter(n -> (n ^ (2 * n) ^ (3 * n)) == 0).count();
  }
}
