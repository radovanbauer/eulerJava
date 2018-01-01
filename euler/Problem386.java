package euler;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem386 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem386().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 100_000_000;
    long sum = 0;
    int[] expSum = new int[max + 1];
    boolean[] prime = new boolean[max + 1];
    for (int i = 2; i <= max; i++) {
      if (!prime[i]) {
        prime[i] = true;
        long iPow = i;
        while (iPow <= max) {
          for (long j = iPow; j <= max; j += iPow) {
            expSum[(int) j]++;
          }
          iPow *= i;
        }
      }
    }
    for (int i = 1; i <= max; i++) {
      for (int j = i; j <= max; j += i) {
        if ((expSum[j] >> 1) == expSum[i]) {
          sum++;
        }
      }
    }
    return sum;
  }
}
