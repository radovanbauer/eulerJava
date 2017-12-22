package euler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem310 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem310().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 100_000;
    int[] rank = new int[max + 1];
    int maxRank = 0;
    for (int n = 0; n <= max; n++) {
      Set<Integer> moves = new HashSet<>();
      for (int k = 1; k * k <= n; k++) {
        moves.add(rank[n - k * k]);
      }
      for (int i = 0;; i++) {
        if (!moves.contains(i)) {
          rank[n] = i;
          break;
        }
      }
      maxRank = Math.max(maxRank, rank[n]);
    }
    int[] rankSize = new int[maxRank + 1];
    for (int n = 0; n <= max; n++) {
      rankSize[rank[n]]++;
    }
    long count = 0;
    for (int rankA = 0; rankA <= maxRank; rankA++) {
      for (int rankB = rankA; rankB <= maxRank; rankB++) {
        int rankC = rankA ^ rankB;
        if (rankC < rankB || rankC > maxRank) {
          continue;
        }
        if (rankA == rankC) {
          long s = rankSize[rankA];
          count += s * (s - 1) * (s - 2) / 6 + s * s;
        } else if (rankA == rankB) {
          long s1 = rankSize[rankA];
          long s2 = rankSize[rankC];
          count += (s1 * (s1 - 1) / 2 + s1) * s2;
        } else if (rankB == rankC) {
          long s1 = rankSize[rankB];
          long s2 = rankSize[rankA];
          count += (s1 * (s1 - 1) / 2 + s1) * s2;
        } else {
          long s1 = rankSize[rankA];
          long s2 = rankSize[rankB];
          long s3 = rankSize[rankC];
          count += s1 * s2 * s3;
        }
      }
    }
    return count;
  }
}
