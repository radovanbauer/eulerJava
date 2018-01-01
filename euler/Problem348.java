package euler;

import java.math.RoundingMode;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import com.google.common.math.LongMath;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableSortedSet;

public class Problem348 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem348().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long max = 10_000_000_000L;
    ImmutableSortedSet.Builder<Long> squaresBuilder = ImmutableSortedSet.naturalOrder();
    ImmutableSortedSet.Builder<Long> cubesBuilder = ImmutableSortedSet.naturalOrder();
    for (long a = 2; a * a <= max; a++) {
      squaresBuilder.add(a * a);
    }
    for (long a = 2; a * a * a <= max; a++) {
      cubesBuilder.add(a * a * a);
    }
    ImmutableSortedSet<Long> squares = squaresBuilder.build();
    ImmutableSortedSet<Long> cubes = cubesBuilder.build();
    TreeSet<Long> res = new TreeSet<>();
    long maxSqrt = LongMath.sqrt(max, RoundingMode.FLOOR);
    for (long n = 1; n <= maxSqrt; n++) {
      char[] chars = String.valueOf(n).toCharArray();
      char[] chars1 = new char[chars.length * 2 - 1];
      char[] chars2 = new char[chars.length * 2];
      System.arraycopy(chars, 0, chars1, 0, chars.length);
      System.arraycopy(chars, 0, chars2, 0, chars.length);
      for (int i = 0; i < chars.length; i++) {
        chars1[chars1.length - i - 1] = chars[i];
        chars2[chars2.length - i - 1] = chars[i];
      }
      long n1 = Long.valueOf(new String(chars1));
      long n2 = Long.valueOf(new String(chars2));
      if (count(n1, squares, cubes) == 4) {
        res.add(n1);
      }
      if (count(n2, squares, cubes) == 4) {
        res.add(n2);
      }
    }
    return res.stream().limit(5).mapToLong(n -> n).sum();
  }

  private int count(long n, ImmutableSortedSet<Long> squares, ImmutableSortedSet<Long> cubes) {
    int count = 0;
    for (long cube : cubes) {
      long rem = n - cube;
      if (rem < 0) {
        break;
      }
      if (squares.contains(rem)) {
        count++;
      }
    }
    return count;
  }
}
