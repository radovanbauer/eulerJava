package euler;

public class Problem164 {

  public static void main(String[] args) {
    System.out.println(new Problem164().solve(20));
  }

  public long solve(int n) {
    long sum = 0;
    for (int d = 1; d <= 9; d++) {
      sum += count(n - 1, 9 - d, 9 - d);
    }
    return sum;
  }

  private Long[][][] cache = new Long[21][10][10];

  private long count(int n, int max2, int max1) {
    if (n == 0) {
      return 1;
    }
    if (cache[n][max2][max1] != null) {
      return cache[n][max2][max1];
    }
    long res = 0L;
    for (int d = 0; d <= Math.min(max2, max1); d++) {
      res += count(n - 1, 9 - d, max2 - d);
    }
    return cache[n][max2][max1] = res;
  }
}
