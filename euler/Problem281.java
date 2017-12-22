package euler;

import com.google.common.math.BigIntegerMath;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.math.IntMath.checkedMultiply;

public class Problem281 {

  public static void main(String[] args) {
    Runner.run(new Problem281()::solve);
  }

  // sum(t(m, d) * m * d, d|n) = (m * n)! / n!^m
  //
  // t(1, n) = 1
  // t(m, 1) = (m - 1)!
  //
  // t(2, 1) = 1 t(2, 2) = 1  t(2, 3) =
  // t(3, 1) = 2 t(3, 2) = 14

  public BigInteger solve() {
    FactorizationSieve sieve = new FactorizationSieve(1_000_000);
    BigInteger max = BigInteger.valueOf(10).pow(15);

    BigInteger total = BigInteger.ZERO;

    for (int m = 2;; m++) {
      List<BigInteger> t = new ArrayList<>();
      t.add(BigInteger.ZERO);
      for (int n = 1;; n++) {
        BigInteger f = BigInteger.ZERO;
        BigInteger sum = BigInteger.ZERO;
        for (int d : sieve.divisors(n)) {
          if (d < n) {
            sum = sum.add(t.get(d).multiply(BigInteger.valueOf(checkedMultiply(m, d))));
            f = f.add(t.get(d));
          }
        }
        BigInteger newT = BigIntegerMath.factorial(checkedMultiply(m, n))
            .divide(BigIntegerMath.factorial(n).pow(m))
            .subtract(sum)
            .divide(BigInteger.valueOf(checkedMultiply(m, n)));
        t.add(newT);
        f = f.add(newT);
        if (f.compareTo(max) <= 0) {
          total = total.add(f);
        }
        if (newT.compareTo(max) > 0) {
          if (n == 1) {
            return total;
          } else {
            break;
          }
        }
      }
    }
  }
}
