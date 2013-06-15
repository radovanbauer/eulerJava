package euler;

import java.math.BigInteger;

public class Problem216 {

  public static void main(String[] args) {
    System.out.println(new Problem216().solve(50000000));
  }

  public int solve(int n) {
    int cnt = 0;
    for (long i = 2; i <= n; i++) {
      if (i % 1000000 == 0) System.out.println(i);
      if (BigInteger.valueOf(2 * i * i - 1).isProbablePrime(1)) {
        cnt++;
      }
    }
    return cnt;
  }
}
