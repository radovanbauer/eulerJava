package euler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;

public class Problem544 {

  private static final long MOD = 1_000_000_007L;

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem544().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int rows = 10;
    int columns = 9;
    int n = 1112131415;
    LongMod[] f = IntStream.rangeClosed(0, rows * columns).parallel()
        .mapToObj(colors -> count(
            ImmutableList.copyOf(Collections.nCopies(columns + 1, 0)),
            1, rows * columns, colors, new HashMap<>()))
        .toArray(LongMod[]::new);
    f[0] = LongMod.zero(MOD);
    LongMod[] g = new LongMod[rows * columns + 1];
    g[0] = LongMod.zero(MOD);
    for (int colors = 1; colors <= rows * columns; colors++) {
      g[colors] = f[colors];
      for (int i = colors - 1; i >= 1; i--) {
        g[colors] = g[colors].subtract(binomial(colors, i).multiply(g[i]));
      }
    }
    LongMod sum = LongMod.zero(MOD);
    for (int colors = 1; colors <= rows * columns; colors++) {
      sum = sum.add(g[colors].multiply(binomial(n + 1, colors + 1)));
    }
    return sum.n();
  }

  private LongMod binomial(long n, long k) {
    if (n < 0 || k < 0 || k > n) {
      return LongMod.zero(MOD);
    } else {
      LongMod result = LongMod.create(1, MOD);
      for (int i = 0; i < k; i++) {
        result = result.multiply(n - i).divide(i + 1);
      }
      return result;
    }
  }

  @AutoValue
  static abstract class Key {
    abstract ImmutableList<Integer> row();
    abstract int current();
    abstract int remaining();
    abstract int colors();

    static Key create(List<Integer> row, int current, int remaining, int colors) {
      return new AutoValue_Problem544_Key(ImmutableList.copyOf(row), current, remaining, colors);
    }
  }

  private LongMod count(List<Integer> row, int current, int remaining, int colors, Map<Key, LongMod> cache) {
    if (remaining == 0) {
      return LongMod.create(1, MOD);
    } else if (colors == 0) {
      return LongMod.zero(MOD);
    } else {
      row = normalize(row);
      Key key = Key.create(row, current, remaining, colors);
      LongMod res = cache.get(key);
      if (res != null) {
        return res;
      }
      res = LongMod.zero(MOD);
      HashSet<Integer> rowSet = new HashSet<>(row);
      rowSet.remove(0);
      ArrayList<Integer> newRow = new ArrayList<Integer>(row);
      for (int newColor : rowSet) {
        if (newColor != row.get(current - 1).intValue() && newColor != row.get(current).intValue()) {
          newRow.set(current, newColor);
          int newCurrent = current % (row.size() - 1) + 1;
          res = res.add(count(newRow, newCurrent, remaining - 1, colors, cache));
        }
      }
      int unusedColor = 1;
      while (rowSet.contains(unusedColor)) {
        unusedColor++;
      }
      if (unusedColor <= colors) {
        int newColor = unusedColor;
        if (newColor != row.get(current - 1).intValue() && newColor != row.get(current).intValue()) {
          newRow.set(current, newColor);
          int newCurrent = current % (row.size() - 1) + 1;
          res = res.add(count(newRow, newCurrent, remaining - 1, colors, cache).multiply(colors - rowSet.size()));
        }
      }
      cache.put(key, res);
      return res;
    }
  }

  private List<Integer> normalize(List<Integer> row) {
    List<Integer> res = new ArrayList<>();
    int next = 1;
    Map<Integer, Integer> mapping = new HashMap<>();
    for (int i = 0; i < row.size(); i++) {
      if (row.get(i).intValue() == 0) {
        res.add(0);
      } else {
        if (!mapping.containsKey(row.get(i))) {
          mapping.put(row.get(i), next++);
        }
        res.add(mapping.get(row.get(i)));
      }
    }
    return res;
  }
}
