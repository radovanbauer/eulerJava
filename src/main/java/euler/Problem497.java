package euler;

import java.util.Random;

public class Problem497 {

  public static void main(String[] args) {
    System.out.println(new Problem497().solve());
  }

  public long solve() {
    bf(1);
    return 0;
  }

  private long walksbf(int a, int k) {
    int n = 2 * a + k;
    long count = 0;
    next: for (long x = 0; x < (1L << n); x++) {
      if (Long.bitCount(x) == a) {
        int sum = 0;
        for (int i = 0; i < n; i++) {
          sum += (x & (1L << i)) != 0 ? 1 : -1;
          if (sum < -k) {
            continue next;
          }
        }
        count++;
      }
    }
    return count;
  }

  private void bf(int k) {
    Random random = new Random();
    long sum = 0;
    for (int i = 1;; i++) {
      sum += steps(k, random);
      System.out.println(1D * sum / i);
    }
  }

  private long steps(int k, Random random) {
    long current = 0;
    long steps = 0;
    while (current > -k) {
      current += random.nextBoolean() ? 1 : -1;
      steps++;
    }
    return steps;
  }
}
