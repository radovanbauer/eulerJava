package euler;

import java.util.stream.IntStream;

import com.google.common.math.LongMath;

public class Problem458 {

  public static void main(String[] args) {
    System.out.println(new Problem458().solve());
  }

  public long solve() {
    long mod = 1_000_000_000L;
    LongMatrix m = LongMatrix.create(new long[][] {
        {1, 1, 1, 1, 1, 1},
        {6, 1, 1, 1, 1, 1},
        {0, 5, 1, 1, 1, 1},
        {0, 0, 4, 1, 1, 1},
        {0, 0, 0, 3, 1, 1},
        {0, 0, 0, 0, 2, 1}});
    LongMatrix result = m.powMod(1_000_000_000_000L - 1L, mod)
        .multiply(LongMatrix.create(new long[][] {{7}, {0}, {0}, {0}, {0}, {0}})).mod(mod);
    return LongMath.mod(IntStream.range(0, 6).mapToLong(i -> result.element(i, 0)).sum(), mod);
  }
}
