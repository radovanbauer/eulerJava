package euler;

import com.google.common.math.IntMath;

public class Problem198 {

  public static void main(String[] args) {
    System.out.println(new Problem198().solve(100000000));
  }

  public long solve(int n) {
    long cnt = 0;
    for (int b = 1; 2 * b <= n; b++) {
      for (int d = 1; 2 * b * d <= n; d++) {
        int[] res = extGcd(b, d);
        if (res[0] != 1) continue;
        int c = IntMath.mod(res[1], d);
        if (c == 0) continue;
        if (100L * (2L * b * c - 1L) < 2L * b * d) {
          cnt++;
        }
      }
    }
    return cnt;
  }

  private int[] extGcd(int a, int b) {
    int origb = b;
    int x = 0, lastx = 1;
    while (b != 0) {
      int quot = a / b;
      int rem = a % b;
      a = b;
      b = rem;
      int nextx = lastx - quot * x;
      lastx = x;
      x = nextx;
    }
    return new int[] { a, lastx };
  }
}
