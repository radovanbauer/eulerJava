package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static euler.Runner.run;

public class Problem600 {

  public static void main(String[] args) {
    run(new Problem600()::solve);
  }

  public long solve() {
    long sum = 0;
    int n = 55106;

    for (int x = (n - 2) / 2; x > 0; x--) {
      for (int y = Math.min(x, n - x - 1); 2 * y >= x + 2; y--) {
        int minz = x - y + 2;
        int maxz = Math.min(y, n - x - y);
        checkState(minz <= maxz);
        sum += halfSum(minz, maxz) - (maxz - minz + 1) * ((x - y + 1) / 2)
            + (((x - y) & 1) != 0 ? (maxz + 1) / 2 - (minz / 2) : 0);
      }
    }

    return sum;
  }

  private int halfSum(int from, int to) {
    checkArgument(from <= to);
    if (from == to) {
      return from / 2;
    }
    if ((from & 1) != 0) {
      return from / 2 + halfSum(from + 1, to);
    }
    if ((to & 1) == 0) {
      return to / 2 + halfSum(from, to - 1);
    }
    int fromHalf = from / 2;
    int toHalf = (to - 1) / 2;
    return (fromHalf + toHalf) * (toHalf - fromHalf + 1);
  }
}
