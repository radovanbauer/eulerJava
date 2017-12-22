package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;

public class Problem411v2 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem411v2().solve());
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
    IntervalTree<Integer> maxByY = new IntervalTree<>(0, n - 1);
    maxByY.set(0, n - 1, 0);
    for (int i = 0; i < points.size(); i++) {
      max[i] = maxByY.get(points.get(i).y()) + 1;
      int low = points.get(i).y();
      int high = n;
      while (high - low > 1) {
        int mid = low + (high - low) / 2;
        if (maxByY.get(mid) > max[i]) {
          high = mid;
        } else {
          low = mid;
        }
      }
      maxByY.set(points.get(i).y(), low, max[i]);
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
      return new AutoValue_Problem411v2_Point(x, y);
    }

    @Override
    public String toString() {
      return "[" + x() + "," + y() + "]";
    }
  }

  private static class IntervalTree<V> {
    private final Node<V> root;

    public IntervalTree(int start, int end) {
      root = new Node<V>(start, end);
    }

    public V get(int key) {
      return root.get(key);
    }

    public void set(int from, int to, V value) {
      root.set(from, to, value);
    }

    private static class Node<V> {
      private final int min, max;
      private V value;
      private Node<V> left, right;

      public Node(int min, int max) {
        this.min = min;
        this.max = max;
      }

      public V get(int key) {
        if (left != null) {
          if (key <= left.max) {
            return left.get(key);
          } else {
            return right.get(key);
          }
        } else {
          return value;
        }
      }

      public void set(int from, int to, V value) {
        checkArgument(this.min <= from);
        checkArgument(from <= to);
        checkArgument(to <= this.max);
        if (from == this.min && to == this.max) {
          this.left = null;
          this.right = null;
          this.value = value;
        } else {
          if (left == null) {
            int mid = this.min + (this.max - this.min) / 2;
            left = new Node<V>(this.min, mid);
            left.value = this.value;
            right = new Node<V>(mid + 1, this.max);
            right.value = this.value;
          }
          if (left.max >= from) {
            left.set(from, Math.min(left.max, to), value);
          }
          if (right.min <= to) {
            right.set(Math.max(right.min, from), to, value);
          }
        }
      }
    }
  }
}
