package euler;

import com.google.common.math.BigIntegerMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.math.LongMath.checkedMultiply;
import static java.math.RoundingMode.FLOOR;

public class Problem354 {
  public static void main(String[] args) {
    Runner.run(new Problem354()::solve);
  }

  public long solve() {
    BigInteger maxD = new BigInteger("83333333333333333333333");
    long maxPrime = BigIntegerMath.sqrt(maxD.divide(BigInteger.valueOf(7 * 13).pow(4)), FLOOR).longValueExact();
    FactorizationSieve sieve = new FactorizationSieve(Math.max(maxPrime, MAX_PRODUCT));
    long[] primes = Longs.toArray(sieve.getAllPrimes());
    return count1(new int[]{4, 4, 2}, 0, primes, new HashSet<>(), sieve, BigInteger.ONE, maxD) / 2
        + count1(new int[]{24, 2}, 0, primes, new HashSet<>(), sieve, BigInteger.ONE, maxD)
        + count1(new int[]{14, 4}, 0, primes, new HashSet<>(), sieve, BigInteger.ONE, maxD)
        + count1(new int[]{74}, 0, primes, new HashSet<>(), sieve, BigInteger.ONE, maxD);
  }

  private long count1(int[] exps, int nextExp, long[] primes, Set<Long> usedPrimes, FactorizationSieve sieve, BigInteger product, BigInteger maxProduct) {
    checkArgument(product.compareTo(maxProduct) <= 0);
    if (nextExp == exps.length) {
      return count2(sieve, maxProduct.divide(product).longValueExact());
    } else {
      long res = 0;
      for (long prime : primes) {
        if (prime % 3 == 1 && !usedPrimes.contains(prime)) {
          BigInteger newProduct = product.multiply(BigInteger.valueOf(prime).pow(exps[nextExp]));
          if (newProduct.compareTo(maxProduct) > 0) {
            break;
          }
          usedPrimes.add(prime);
          res += count1(exps, nextExp + 1, primes, usedPrimes, sieve, newProduct, maxProduct);
          usedPrimes.remove(prime);
        }
      }
      return res;
    }
  }

  private long count2(FactorizationSieve sieve, long maxProduct) {
    long res = 0;
    for (long pow3 = 1; pow3 <= maxProduct; pow3 = checkedMultiply(pow3, 3)) {
      res += count3(sieve, LongMath.sqrt(maxProduct / pow3, FLOOR));
    }
    return res;
  }

  private static final int MAX_PRODUCT = 2_000_000;
  private long[] count3 = null;

  private long count3(FactorizationSieve sieve, long maxProduct) {
    checkArgument(maxProduct <= MAX_PRODUCT);
    if (count3 == null) {
      count3 = new long[MAX_PRODUCT + 1];
      for (int n = 1; n <= MAX_PRODUCT; n++) {
        count3[n] = count3[n - 1];
        Factorization factorization = sieve.factorization(n);
        if (factorization.getPrimes().stream().allMatch(p -> p % 3 == 2)) {
          count3[n]++;
        }
      }
    }
    return count3[Ints.checkedCast(maxProduct)];
  }
}
