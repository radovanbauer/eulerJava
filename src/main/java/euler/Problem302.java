package euler;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Longs;

import euler.Factorization.PrimeFactor;

public class Problem302 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem302().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long n = LongMath.pow(10, 18);
    FactorizationSieve sieve = new FactorizationSieve((long) (Math.pow(n, 1D / 3) + 0.5D));
    long[] primes = Longs.toArray(Lists.reverse(sieve.getAllPrimes()));
    return count(n, sieve, primes, 0, new ArrayDeque<Factorization.PrimeFactor>(), 1, 0, Factorization.create());
  }

  private long count(
      long n,
      FactorizationSieve sieve,
      long[] primes,
      int nextPrime,
      ArrayDeque<Factorization.PrimeFactor> primeFactors,
      long product,
      int expGcd,
      Factorization totientFactorization) {
    long count = 0;
    primeLoop: for (int primeIdx = nextPrime; primeIdx < primes.length; primeIdx++) {
      long prime = primes[primeIdx];
      if (product >= n / prime) {
        continue;
      }
      long newProduct = product * prime;
      for (int exp = 2;; exp++) {
        if (newProduct >= n / prime) {
          continue primeLoop;
        }
        newProduct = newProduct * prime;
        if (exp == 2 && totientFactorization.getExponent(prime) == 0) {
          continue;
        }
        int newExpGcd = IntMath.gcd(expGcd, exp);
        primeFactors.addLast(Factorization.PrimeFactor.create(prime, exp));
        Factorization newTotientFactorization = Factorization.product(
            totientFactorization,
            sieve.factorization(prime - 1),
            Factorization.create(PrimeFactor.create(prime, exp - 1)));
        if (newExpGcd == 1) {
          if (isAchilles(newTotientFactorization)) {
            count++;
          }
        }
        count += count(n, sieve, primes, primeIdx + 1, primeFactors, newProduct, newExpGcd, newTotientFactorization);
        primeFactors.removeLast();
      }
    }
    return count;
  }

  private boolean isAchilles(Factorization factorization) {
    int gcd = 0;
    for (long prime : factorization.getPrimes()) {
      if (factorization.getExponent(prime) < 2) {
        return false;
      }
      gcd = IntMath.gcd(gcd, factorization.getExponent(prime));
    }
    return gcd == 1;
  }
}
