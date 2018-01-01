package euler;

import com.google.common.collect.ImmutableList;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkState;
import static java.math.BigInteger.ONE;

public class Problem574 {
  public static void main(String[] args) {
    Runner.run(new Problem574()::solve);
  }

  // 2*(2*7) + 3*(-1*7) = 7
  // 2*11 + 3*(-5) = 7
  // 2*8 + 3*(-3) = 7
  // 2*5 + 3*(-1) = 7
  // 2*2 + 3*1 = 7
  //
  // r * (a'*p + k*c) + c * (b'*p - k*r) = p
  //
  // r*a'*p + k*r*c >= c*b'*p - k*r*c
  // 2*k*r*c >= (b'*c - a'*r)*p
  // k >= (b'*c - a'*r)*p/(2*r*c) = b'*p/(2*r) - a'*p/(2*c)
  //
  // a'*p + k*c > 0
  // k > -a'*p/c
  // b'*p - k*r > 0
  // k < b'*p/r
  //
  // r*a'*p + k*r*c >= -c*b'*p + k*r*c
  // r*a'*p >= -c*b'*p
  //
  // a'*p + k*c > 0
  // k > -a'*p/c
  // b'*p - k*r < 0
  // k > b'*p/r

  public BigInteger solve() {
    FactorizationSieve sieve = new FactorizationSieve(1_000_000);
    BigInteger sum = BigInteger.ZERO;

    ImmutableList<Long> primes = sieve.getPrimes(3800);

    for (long p : primes) {
      int qidx = 0;
      while (primes.get(qidx) * primes.get(qidx) < p) {
        qidx++;
      }
      BigInteger v = null;

      for (int xmask = 0; xmask < (1 << qidx); xmask++) {
        BigInteger x = ONE;
        BigInteger y = ONE;
        for (int i = 0; i < qidx; i++) {
          if (((1 << i) & xmask) != 0) {
            x = x.multiply(BigInteger.valueOf(primes.get(i)));
          } else {
            y = y.multiply(BigInteger.valueOf(primes.get(i)));
          }
        }

        BigInteger a = y.compareTo(ONE) == 0 ? ONE : BigMod.create(x, y).invert().n();
        BigInteger b = ONE.subtract(x.multiply(a)).divide(y);

        // A + B
        {
          long k = Math.max(
              b.multiply(y)
                  .subtract(a.multiply(x)).multiply(BigInteger.valueOf(p))
                  .divide(x.multiply(y).multiply(BigInteger.valueOf(2))).longValueExact(),
              -a.multiply(BigInteger.valueOf(p)).divide(y).longValueExact());
          BigInteger A = x.multiply(BigInteger.valueOf(p).multiply(a).add(BigInteger.valueOf(k).multiply(y)));
          if (v == null || A.compareTo(v) < 0) {
            BigInteger B = y.multiply(BigInteger.valueOf(p).multiply(b).subtract(BigInteger.valueOf(k).multiply(x)));
            checkState(A.compareTo(B) >= 0);
            checkState(A.compareTo(BigInteger.ZERO) >= 0);
            if (B.compareTo(BigInteger.ZERO) > 0) {
              checkState(A.gcd(B).compareTo(ONE) == 0);
              v = A;
            }
          }
        }

        // A - B
        {
          if (x.multiply(a).compareTo(y.negate().multiply(b)) >= 0) {
            long k = Math.max(a.negate().multiply(BigInteger.valueOf(p)).subtract(y.subtract(ONE)).divide(y).longValueExact() + 1,
                b.multiply(BigInteger.valueOf(p)).subtract(x.subtract(ONE)).divide(x).longValueExact() + 1);
            BigInteger A = x.multiply(BigInteger.valueOf(p).multiply(a).add(BigInteger.valueOf(k).multiply(y)));
            if (v == null || A.compareTo(v) < 0) {
              BigInteger B = y.multiply(BigInteger.valueOf(p).multiply(b).subtract(BigInteger.valueOf(k).multiply(x)));
              checkState(A.compareTo(B.negate()) >= 0);
              checkState(A.compareTo(BigInteger.ZERO) >= 0);
              checkState(B.compareTo(BigInteger.ZERO) < 0);
              while (A.gcd(B.negate()).compareTo(ONE) != 0) {
                A = A.add(x.multiply(y));
                B = B.subtract(x.multiply(y));
              }
              v = A;
            }
          }
        }

      }
      sum = sum.add(v);
    }

    return sum;
  }
}
