package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem400 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem400().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  private static final long MOD = 1_000_000_000_000_000_000L;

  public long solve() {
    int n = 10_000;
    long g1 = 1;
    long g2 = 2;
    LongModMultiset<Long> gr1 = new LongModMultiset<>(MOD);
    gr1.add(0L, LongMod.create(1, MOD));
    LongModMultiset<Long> gr2 = new LongModMultiset<>(MOD);
    gr2.add(0L, LongMod.create(1, MOD));
    gr2.add(1L, LongMod.create(1, MOD));
    for (int i = 3; i <= n; i++) {
      LongModMultiset<Long> gr3 = new LongModMultiset<>(MOD);
      gr3.add(0L, LongMod.create(1, MOD));
      for (Map.Entry<Long, LongMod> leftEntry : gr2.asMap().entrySet()) {
        gr3.add(LongMath.checkedAdd(leftEntry.getKey() ^ g1, 1), leftEntry.getValue());
      }
      for (Map.Entry<Long, LongMod> rightEntry : gr1.asMap().entrySet()) {
        gr3.add(LongMath.checkedAdd(rightEntry.getKey() ^ g2, 1), rightEntry.getValue());
      }
      gr1 = gr2;
      gr2 = gr3;
      long g3 = LongMath.checkedAdd(g1 ^ g2, 1);
      g1 = g2;
      g2 = g3;
    }
    return gr2.count(1L).n();
  }

  private static class LongModMultiset<E> {
    private final Map<E, LongMod> counts = new HashMap<>();

    private final long mod;

    public LongModMultiset(long mod) {
      this.mod = mod;
    }

    public void add(E elem, LongMod count) {
      checkArgument(count.mod() == mod);
      if (!counts.containsKey(elem)) {
        counts.put(elem, count);
      } else {
        counts.put(elem, counts.get(elem).add(count));
      }
    }

    public LongMod count(E elem) {
      return counts.getOrDefault(elem, LongMod.zero(mod));
    }

    public Map<E, LongMod> asMap() {
      return Collections.unmodifiableMap(counts);
    }

    @Override
    public String toString() {
      return counts.toString();
    }
  }
}
