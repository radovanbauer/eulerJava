package euler;

import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;

import java.util.List;

import static com.google.common.math.LongMath.checkedMultiply;
import static java.math.RoundingMode.FLOOR;

public class Problem447 {
  public static void main(String[] args) {
    Runner.run(new Problem447()::solve);
  }

  // sum(r(i), i, {1,n}) = sum(sum(d, d|i && gcd(d, i/d)==1), i, {1,n}) = sum(d*count(k, gcd(d,k)==1 && k<=n/d), d, {1, n})
  //
  // 1: 1*n
  // 2: 2*(n/2 - n/4)
  // 3: 3*(n/3 - n/9)
  // 4: 4*(n/4 - n/8)
  // 5: 5*(n/5 - n/25)
  // 6: 6*(n/6 - n/12 - n/18 + n/36)

  private static final int mod = 1_000_000_007;
  private static final long inv2 = LongMod.create(2, mod).invert().n();

  public long solve() {
    long n = LongMath.checkedPow(10, 14);
    return bfSum4(n).subtract(LongMod.create(1 + n, mod).multiply(n).multiply(inv2)).n();
  }

  private LongMod bfSum4(long n) {
    long nsqrt = LongMath.sqrt(n, FLOOR);
    return count(n, nsqrt, ImmutableList.copyOf(PrimeSieve.create(nsqrt)), 0, 1L, 0);
  }

  private LongMod count(long n, long nSqrt, List<Long> primes, int nextPrime, long primeProduct, int primeCount) {
    LongMod sum = LongMod.zero(mod);

    int sign = (((primeCount & 1) == 0) ? 1 : -1);

    long primeProduct2 = checkedMultiply(primeProduct, primeProduct);
    long cutoff = nSqrt / primeProduct;
    for (long m = 1; n / (checkedMultiply(m, primeProduct2)) >= cutoff; m++) {
      sum = sum.add(LongMod.create(m, mod).multiply(primeProduct).multiply(n / (m * primeProduct2)).multiply(sign));
    }
    // floor(n / (m*primeProduct^2)) = l
    //
    // n / (m*primeProduct^2) >= l
    // n >= l * m * primeProduct^2
    // m <= floor(n / (l * primeProduct^2))
    //
    // n / (m*primeProduct^2) < l+1
    // n < (l+1)*(m*primeProduct^2)
    // m > floor(n/((l+1)*primeProduct^2))

    for (long l = 1; l < cutoff; l++) {
      long mmax = n / (checkedMultiply(l, primeProduct2));
      long mmin = n / (checkedMultiply(l + 1, primeProduct2)) + 1;
      sum = sum.add(LongMod.create(primeProduct, mod).multiply(l).multiply(mmin + mmax).multiply(mmax - mmin + 1).multiply(inv2).multiply(sign));
    }

    for (int i = nextPrime; i < primes.size(); i++) {
      long newPrimeProduct = checkedMultiply(primeProduct, primes.get(i));
      if (newPrimeProduct > nSqrt) {
        break;
      }
      sum = sum.add(count(n, nSqrt, primes, i + 1, newPrimeProduct, primeCount + 1));
    }
    return sum;
  }
}
