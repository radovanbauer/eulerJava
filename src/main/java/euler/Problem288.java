package euler;

import com.google.common.math.LongMath;

public class Problem288 {

  public static void main(String[] args) {
    System.out.println(new Problem288().solve());
  }

  public long solve() {
    int p = 61;
    int q = 10_000_000;
    long s = 290797;
    long mod = LongMath.pow(61, 10);
    long pPow = 1L;
    long factor = 0L;
    long sum = 0L;
    for (int i = 0; i <= q; i++) {
      long t = s % p;
      s = (s * s) % 50515093;
      sum = (sum + factor * t) % mod;
      factor = (factor + pPow) % mod;
      pPow = (pPow * p) % mod;
    }
    return sum;
  }
}
