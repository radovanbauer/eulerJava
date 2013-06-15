package euler;

import java.util.Arrays;

public class Problem156 {

  public static void main(String[] args) {
    System.out.println(new Problem156().solve());
  }

  public long solve() {
    long sum = 0L;
    for (int d = 1; d <= 9; d++) {
      sum += solve(d);
    }
    return sum;
  }

  public long solve(int d) {
    long sum = 0L;
    long cnt = 0L;
    long n = 1;
    while (n < 1000000000000L) {
      if (n % 10000000 == 0) {
        System.out.println(1F * n / 1000000000000L);
      }
      int digits = 0;
      long m = n;
      while (m > 0) {
        if (m % 10 == d) {
          cnt++;
        }
        m /= 10;
        digits++;
      }
      if (cnt == n) {
        sum += n;
        n++;
      } else {
        n += Math.abs((n - cnt)) / digits;
      }
    }
    System.out.printf("%d: %d\n", d, sum);
    return sum;
  }
}
