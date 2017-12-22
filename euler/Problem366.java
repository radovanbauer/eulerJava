package euler;

import java.math.BigInteger;

import com.google.common.math.LongMath;

public class Problem366 {

  public static void main(String[] args) {
    System.out.println(new Problem366().solve());
  }

  public long solve() {
    long n = 1_000_000_000_000_000_000L;

    long[] fib = new long[90];
    fib[0] = 0;
    fib[1] = 1;
    for (int i = 2; i < fib.length; i++) {
      fib[i] = LongMath.checkedAdd(fib[i - 1], fib[i - 2]);
    }

    long zidx = 0;
    long midx = 1;
    BigInteger sum = BigInteger.ZERO;
    for (int i = 2;; i++) {
      for (int j = i; j > 1; j -= 2) {
        if (j == i) {
          zidx++;
        } else {
          zidx += fib[j];
        }
        long max = (fib[j] - 1) / 2;
        long min = midx - zidx;
        if (max >= min) {
          midx += max - min + 1;
          if (midx > n) {
            max -= midx - n - 1;
            sum = sum.add(BigInteger.valueOf(min + max).multiply(BigInteger.valueOf(max - min + 1))
                .divide(BigInteger.valueOf(2)));
            return sum.mod(BigInteger.valueOf(100_000_000)).longValueExact();
          }
          sum = sum.add(BigInteger.valueOf(min + max).multiply(BigInteger.valueOf(max - min + 1))
              .divide(BigInteger.valueOf(2)));
        }
      }
    }
  }
}
