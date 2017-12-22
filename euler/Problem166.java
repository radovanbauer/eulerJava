package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem166 {

  public static void main(String[] args) {
    System.out.println(new Problem166().solve());
  }

  public long solve() {
    long cnt = 0L;
    Stopwatch stopwatch = Stopwatch.createUnstarted();
    for (int sum = 0; sum <= 4 * 9; sum++) {
      stopwatch.start();
      cnt += solve(new Grid(sum), 0);
      long elapsedSeconds = stopwatch.elapsed(TimeUnit.SECONDS);
      stopwatch.reset();
      System.out.printf("sum: %d, elapsed: %ds\n", sum, elapsedSeconds);
    }
    return cnt;
  }

  private long solve(Grid grid, int pos) {
    if (pos == 16) {
      return 1;
    }
    long cnt = 0L;
    for (int d = 0; d <= 9; d++) {
      if (grid.set(pos, d)) {
        cnt += solve(grid, pos + 1);
      }
      grid.unset(pos);
    }
    return cnt;
  }

  private static class Grid {
    private final int[] nums = new int[16];
    private final int sum;
    private final int[] horSum = new int[4];
    private final int[] verSum = new int[4];
    private int diag1Sum;
    private int diag2Sum;
    private final int[] horCnt = new int[4];
    private final int[] verCnt = new int[4];
    private int diag1Cnt;
    private int diag2Cnt;

    public Grid(int sum) {
      this.sum = sum;
      Arrays.fill(nums, -1);
    }

    public boolean set(int pos, int num) {
      checkArgument(nums[pos] == -1);
      boolean ok = true;
      nums[pos] = num;
      int x = pos / 4;
      int y = pos % 4;
      horSum[x] += num;
      horCnt[x]++;
      ok &= horSum[x] <= sum && horSum[x] + (4 - horCnt[x]) * 9 >= sum;
      verSum[y] += num;
      verCnt[y]++;
      ok &= verSum[y] <= sum && verSum[y] + (4 - verCnt[y]) * 9 >= sum;
      if (x == y) {
        diag1Sum += num;
        diag1Cnt++;
        ok &= diag1Sum <= sum && diag1Sum + (4 - diag1Cnt) * 9 >= sum;
      }
      if (x + y == 3) {
        diag2Sum += num;
        diag2Cnt++;
        ok &= diag2Sum <= sum && diag2Sum + (4 - diag2Cnt) * 9 >= sum;
      }
      return ok;
    }

    public void unset(int pos) {
      checkArgument(nums[pos] != -1);
      int num = nums[pos];
      nums[pos] = -1;
      int x = pos / 4;
      int y = pos % 4;
      horSum[x] -= num;
      horCnt[x]--;
      verSum[y] -= num;
      verCnt[y]--;
      if (x == y) {
        diag1Sum -= num;
        diag1Cnt--;
      }
      if (x + y == 3) {
        diag2Sum -= num;
        diag2Cnt--;
      }
    }

    @Override
    public String toString() {
      return Arrays.toString(nums);
    }
  }
}
