package euler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;

public class Problem475 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem475().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  private static final long MOD = 1000000007;

  public long solve() {
    return count2(new int[] {0, 0, 0, 0, 600 / 4}).n();
  }

  @AutoValue
  static abstract class Key {
    @SuppressWarnings("mutable")
    abstract int[] cnt();

    static Key create(int[] groups) {
      int[] cnt = new int[4];
      for (int group : groups) {
        if (group > 0) {
          cnt[group - 1]++;
        }
      }
      return new AutoValue_Problem475_Key(cnt);
    }
  }

  private final Map<ImmutableIntList, LongMod> cache2 = new HashMap<>();

  private LongMod count2(int[] groupCount) {
    int max = 4;
    while (max > 0 && groupCount[max] == 0) {
      max--;
    }
    if (max == 0) {
      return LongMod.create(1L, MOD);
    }
    ImmutableIntList key = ImmutableIntList.copyOf(groupCount);
    LongMod res = cache2.get(key);
    if (res != null) {
      return res;
    }
    res = LongMod.zero(MOD);

    int g1 = max;
    groupCount[g1]--;
    for (int g2 = max; g2 > 0; g2--) {
      int g2Options = groupCount[g2];
      if (g2Options > 0) {
        groupCount[g2]--;
        for (int g3 = g2; g3 > 0; g3--) {
          int g3Options = groupCount[g3];
          if (g3Options > 0) {
            groupCount[g3]--;
            groupCount[g1 - 1]++;
            groupCount[g2 - 1]++;
            groupCount[g3 - 1]++;
            res = res.add(count2(groupCount).multiply(
                g2 * g3 * (g2 == g3 ? g2Options * g3Options / 2 : g2Options * g3Options)));
            groupCount[g1 - 1]--;
            groupCount[g2 - 1]--;
            groupCount[g3 - 1]--;
            groupCount[g3]++;
          }
        }
        groupCount[g2]++;
      }
    }
    groupCount[g1]++;

    cache2.put(key, res);
    return res;
  }
}
