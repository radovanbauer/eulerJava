package euler;

public class Problem171 {

  public static void main(String[] args) {
    System.out.println(new Problem171().solve());
  }

  public long solve() {
    long sum = 0L;
    for (int d = 1; d * d <= 9 * 9 * 20; d++) {
      sum += solve(d * d, 20).sum;
    }
    return sum % 1000000000;
  }

  private Result[][] cache = new Result[2000][21];
  private long[] pow10 = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };

  private Result solve(int digitSum, int digits) {
    if (cache[digitSum][digits] != null) {
      return cache[digitSum][digits];
    }
    long count = 0L;
    long sum = 0L;
    if (digits == 0) {
      if (digitSum == 0) {
        count = 1L;
        sum = 0L;
      }
    } else {
      for (int d = 0; d <= 9 && digitSum - d * d >= 0; d++) {
        Result subResult = solve(digitSum - d * d, digits - 1);
        count += subResult.count;
        sum += subResult.sum;
        if (digits <= 9) {
          sum += d * pow10[digits - 1] * subResult.count;
        }
      }
    }
    return cache[digitSum][digits] = new Result(count, sum % 1000000000);
  }

  private static class Result {
    private final long count;
    private final long sum;

    public Result(long count, long sum) {
      this.count = count;
      this.sum = sum;
    }

    @Override
    public String toString() {
      return String.format("count=%d,sum=%d", count, sum);
    }
  }
}
