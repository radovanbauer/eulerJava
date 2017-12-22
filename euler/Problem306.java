package euler;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.common.base.Stopwatch;

public class Problem306 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem306().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 1_000_000;
    int[] g = new int[max + 1];
    int maxG = 0;
    for (int n = 0; n <= max; n++) {
      boolean[] moves = new boolean[maxG * 2 + 2];
      for (int split = 0; split + 1 < n; split++) {
        moves[g[split] ^ g[n - split - 2]] = true;
      }
      for (int i = 0;; i++) {
        if (!moves[i]) {
          g[n] = i;
          maxG = Math.max(maxG, i);
          break;
        }
      }
    }
    return IntStream.rangeClosed(1, max).filter(n -> g[n] != 0).count();
  }
}
