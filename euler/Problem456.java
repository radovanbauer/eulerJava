package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.math.LongMath.checkedAdd;
import static com.google.common.math.LongMath.checkedMultiply;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.collect.BoundType;
import com.google.common.collect.ImmutableSortedMultiset;

public class Problem456 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem456().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int n = 2_000_000;
    List<List<Point>> pointLists = new ArrayList<>();
    for (int q = 0; q < 4; q++) {
      pointLists.add(new ArrayList<>());
    }
    LongMod xpow = LongMod.create(1, 32323);
    LongMod ypow = LongMod.create(1, 30103);
    for (int i = 1; i <= n; i++) {
      xpow = xpow.multiply(1248);
      ypow = ypow.multiply(8421);
      Point point = Point.create(xpow.n() - 16161, ypow.n() - 15051);
      pointLists.get(point.quadrant()).add(point);
    }

    List<ImmutableSortedMultiset<Point>> points = new ArrayList<>();
    for (int q = 0; q < 4; q++) {
      points.add(ImmutableSortedMultiset.copyOf(pointLists.get(q)));
    }

    long count = 0;

    for (int q1 = 0; q1 < 4; q1++) {
      int q2 = (q1 + 1) % 4;
      int q3 = (q1 + 2) % 4;
      for (Point q1Point : pointLists.get(q1)) {
        count = checkedAdd(count, checkedMultiply(
            points.get(q2).size(),
            points.get(q3).tailMultiset(q1Point.negate(), BoundType.OPEN).size()));
      }
    }

    for (int q1 = 0; q1 < 4; q1++) {
      int q3 = (q1 + 2) % 4;
      for (Point q1Point : pointLists.get(q1)) {
        count = checkedAdd(count, checkedMultiply(
            points.get(q3).headMultiset(q1Point.negate(), BoundType.OPEN).size(),
            points.get(q3).tailMultiset(q1Point.negate(), BoundType.OPEN).size()));
      }
    }

    return count;
  }

  @AutoValue
  abstract static class Point implements Comparable<Point> {
    abstract long x();
    abstract long y();

    static Point create(long x, long y) {
      checkArgument(x != 0 || y != 0, "%d, %d", x, y);
      return new AutoValue_Problem456_Point(x, y);
    }

    @Override
    public int compareTo(Point that) {
      int thisQuadrant = this.quadrant();
      int thatQuadrant = that.quadrant();
      if (thisQuadrant != thatQuadrant) {
        return Integer.compare(thisQuadrant, thatQuadrant);
      } else {
        return Long.compare(
            checkedMultiply(this.y(), that.x()),
            checkedMultiply(that.y(), this.x()));
      }
    }

    public Point negate() {
      return Point.create(-x(), -y());
    }

    public int quadrant() {
      if (x() > 0 && y() >= 0) {
        return 0;
      } else if (x() <= 0 && y() > 0) {
        return 1;
      } else if (x() < 0 && y() <= 0) {
        return 2;
      } else {
        return 3;
      }
    }
  }
}
