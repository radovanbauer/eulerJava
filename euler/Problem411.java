package euler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;

public class Problem411 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem411().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    return IntStream.rangeClosed(1, 30).parallel().map(k -> IntMath.pow(k, 5)).map(this::solve).sum();
  }

  private int solve(int n) {
    Set<Point> pointSet = new HashSet<>();
    LongMod pow2 = LongMod.create(1, n);
    LongMod pow3 = LongMod.create(1, n);
    for (int i = 0; i <= 2 * n; i++) {
      pointSet.add(Point.create(Ints.checkedCast(pow2.n()), Ints.checkedCast(pow3.n())));
      pow2 = pow2.multiply(2);
      pow3 = pow3.multiply(3);
    }
    List<Point> points = new ArrayList<>(pointSet);
    Collections.sort(points, Comparator.comparing(Point::x).thenComparing(Point::y));
    int[] max = new int[points.size()];
    NavigableMap<Integer, Integer> maxByY = new TreeMap<>();
    for (int i = 0; i < points.size(); i++) {
      Entry<Integer, Integer> floorEntry = maxByY.floorEntry(points.get(i).y());
      if (floorEntry != null) {
        max[i] = floorEntry.getValue() + 1;
      } else {
        max[i] = 1;
      }
      while (true) {
        Entry<Integer, Integer> ceilingEntry = maxByY.ceilingEntry(points.get(i).y());
        if (ceilingEntry != null && ceilingEntry.getValue() <= max[i]) {
          maxByY.remove(ceilingEntry.getKey());
        } else {
          break;
        }
      }
      maxByY.put(points.get(i).y(), max[i]);
    }
    int res = Ints.max(max);
    System.out.println(n + " " + res);
    return res;
  }

  @AutoValue
  static abstract class Point {
    abstract int x();
    abstract int y();

    static Point create(int x, int y) {
      return new AutoValue_Problem411_Point(x, y);
    }

    @Override
    public String toString() {
      return "[" + x() + "," + y() + "]";
    }
  }
}
