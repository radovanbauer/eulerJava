package euler;

import com.google.common.math.LongMath;

public class Problem202 {

  public static void main(String[] args) {
    System.out.println(new Problem202().solve(12017639147L));
  }

  public long solve(long n) {
    long cnt = 0L;
    for (long x = 0; x <= (n + 3) / 2; x++) {
      long y = (n + 3) / 2 - x;
      if ((x - y) % 3 == 0 && LongMath.gcd(x, y) == 1) {
        cnt++;
      }
    }
    return cnt;
  }
}
