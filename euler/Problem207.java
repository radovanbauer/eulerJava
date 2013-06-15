package euler;

import com.google.common.math.LongMath;

public class Problem207 {

  public static void main(String[] args) {
    System.out.println(new Problem207().solve());
  }

  public long solve() {
    long perfect = 0L;
    long all = 0L;
    for (long n = 2;; n++) {
      if (LongMath.isPowerOfTwo(n)) {
        perfect++;
      }
      all++;
      if (perfect * 12345 < all) {
        return n * n - n;
      }
    }
  }
}
