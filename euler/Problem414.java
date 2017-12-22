package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.math.LongMath.checkedMultiply;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem414 {

  private static final long MOD = LongMath.pow(10, 18);

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem414().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    return IntStream.rangeClosed(2, 300)
        .parallel()
        .mapToObj(k -> solve(6 * k + 3))
        .reduce(LongMod.zero(MOD), (a, b) -> a.add(b))
        .n();
  }

  private LongMod solve(int b) {
    LongMod sum = LongMod.zero(MOD);
    int[][] cache = new int[b][b];
    for (int x = 0; x < b; x++) {
      for (int y = 0; y <= x; y++) {
        int iter = depth(b, x, y, cache);
        if (x > y && y > 0) {
          sum = sum.add(checkedMultiply(iter, 20L * (b - x) * (6L * (x - y) * y - 1L)));
        } else if (y == 0) {
          sum = sum.add(checkedMultiply(iter, 10L * (b - x) * (2 * x - 1L)));
        } else {
          sum = sum.add(checkedMultiply(iter, 10L * (b - x) * (3 * y - 1L)));
        }
      }
    }
    return sum.subtract(1);
  }

  private int depth(int b, int x, int y, int[][] cache) {
    checkArgument(x >= y);
    if (x == 0 && y == 0) {
      return 0;
    }
    if (cache[x][y] != 0) {
      return cache[x][y];
    }
    int[] d = new int[5];
    d[0] = b - x;
    d[1] = b - 1 - y;
    d[2] = b - 1;
    d[3] = y == 0 ? b - 1 : y - 1;
    d[4] = y == 0 ? x - 1 : x;
    Arrays.sort(d);
    int nx = d[4] - d[0];
    int ny = d[3] - d[1];
    int res;
    if (nx == x && ny == y) {
      res = 1;
    } else {
      res = 1 + depth(b, nx, ny, cache);
    }
    return cache[x][y] = res;
  }
}
