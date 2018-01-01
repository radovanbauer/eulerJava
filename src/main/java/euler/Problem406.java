package euler;

import com.google.auto.value.AutoValue;
import com.google.common.math.LongMath;

import java.util.ArrayList;
import java.util.List;

public class Problem406 {
  public static void main(String[] args) {
    Runner.run(new Problem406()::solve);
  }

  public String solve() {
    long max = LongMath.pow(10, 12);
    double res = 0;
    long f0 = 1;
    long f1 = 0;
    for (int k = 1; k <= 30; k++) {
      long f2 = LongMath.checkedAdd(f0, f1);
      f0 = f1;
      f1 = f2;
      res += solve(max, Math.sqrt(k), Math.sqrt(f1));
    }
    return String.format("%.8f", res);
  }

  private double solve(long max, double a, double b) {
    List<Entry> entries = new ArrayList<>();
    entries.add(Entry.create(0, a, b, 0, 0));
    entries.add(Entry.create(1, a, b, 0, 0));
    int ni = 1;
    int ai = 0;
    int bi = 0;

    while (entries.get(ni).start() <= max) {
      while ((entries.get(ai + 1).as() + 1) * a + entries.get(ai + 1).bs() * b <= entries.get(ni).cost()) {
        ai++;
      }
      while (entries.get(bi + 1).as() * a + (entries.get(bi + 1).bs() + 1) * b <= entries.get(ni).cost()) {
        bi++;
      }
      long maxa = entries.get(ai + 1).start() - 1;
      long maxb = entries.get(bi + 1).start() - 1;
      long nextn = maxa + maxb + 2;

      double nextacost = (entries.get(ai + 1).as() + 1) * a + entries.get(ai + 1).bs() * b;
      double nextbcost = entries.get(bi + 1).as() * a + (entries.get(bi + 1).bs() + 1) * b;
      if (nextbcost < nextacost) {
        bi++;
        entries.add(Entry.create(nextn, a, b, entries.get(bi).as(), entries.get(bi).bs() + 1));
      } else {
        ai++;
        entries.add(Entry.create(nextn, a, b, entries.get(ai).as() + 1, entries.get(ai).bs()));
      }
      ni++;
    }

    return entries.get(ni - 1).cost();
  }

  @AutoValue
  static abstract class Entry {
    abstract long start();
    abstract long as();
    abstract long bs();
    abstract double cost();

    static Entry create(long start, double a, double b, long as, long bs) {
      return new AutoValue_Problem406_Entry(start, as, bs, as*a + bs*b);
    }
  }
}
