package euler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem378 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem378().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 60_000_000;
    long mod = 1_000_000_000_000_000_000L;
    FactorizationSieve sieve = new FactorizationSieve(max + 1);
    int[] t = new int[max + 1];
    for (int n = 1; n <= max; n++) {
      int a = n % 2 == 0 ? n / 2 : n;
      int b = (n + 1) % 2 == 0 ? (n + 1) / 2 : n + 1;
      t[n] = sieve.divisorCount(a) * sieve.divisorCount(b);
    }
    int[] counts = Arrays.stream(t).skip(1).distinct().sorted().toArray();
    Map<Integer, Integer> countMap = new HashMap<>();
    for (int i = 0; i < counts.length; i++) {
      countMap.put(counts[i], i);
    }
    long[] count1 = new long[counts.length];
    long[] count2 = new long[counts.length];
    long[] count3 = new long[counts.length];
    for (int n = 1; n <= max; n++) {
      int d = countMap.get(t[n]);
      long[] newCount1 = count1.clone();
      long[] newCount2 = count2.clone();
      long[] newCount3 = count3.clone();
      for (int c = 0; c < counts.length; c++) {
        if (d >= c) {
          newCount1[c] = (newCount1[c] + 1) % mod;
          if (d + 1 < counts.length) {
            newCount2[c] = (newCount2[c] + count1[d + 1]) % mod;
            newCount3[c] = (newCount3[c] + count2[d + 1]) % mod;
          }
        }
      }
      count1 = newCount1;
      count2 = newCount2;
      count3 = newCount3;
    }
    return count3[0];
  }
}
