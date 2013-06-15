package euler;

import static java.math.RoundingMode.FLOOR;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;

public class Problem211 {

  public static void main(String[] args) {
    System.out.println(new Problem211().solve(64000000));
  }

  public long solve(int n) {
    int[] primes = primes(IntMath.sqrt(n, FLOOR) + 10);
    long sum = 0L;
    for (int i = 1; i < n; i++) {
      if (i % 100000 == 0) System.out.println(i);
      long divSquareSum = 0L;
      for (int div : divisorsFromFactors(factors(i, primes))) {
        divSquareSum += 1L * div * div;
      }
      if (isSquare(divSquareSum)) {
        sum += i;
      }
    }
    return sum;
  }

  private boolean isSquare(long n) {
    long sqrtFloor = LongMath.sqrt(n, FLOOR);
    return sqrtFloor * sqrtFloor == n;
  }

  private int[] divisorsFromFactors(Factor[] factors) {
    ArrayList<Integer> divisors = Lists.newArrayList();
    divisorsFromFactors(factors, 0, 1, divisors);
    return Ints.toArray(divisors);
  }

  private void divisorsFromFactors(
      Factor[] factors, int k, int cur, List<Integer> divisors) {
    if (k == factors.length) {
      divisors.add(cur);
      return;
    }
    Factor factor = factors[k];
    int primePow = 1;
    for (int coef = 0; coef <= factor.coef; coef++) {
      divisorsFromFactors(factors, k + 1, cur * primePow, divisors);
      primePow *= factor.prime;
    }
  }

  private Factor[] factors(int n, int[] primes) {
    List<Factor> res = Lists.newArrayList();
    for (int p = 0;; p++) {
      if (primes[p] * primes[p] > n) {
        break;
      }
      int coef = 0;
      while (n % primes[p] == 0) {
        n /= primes[p];
        coef++;
      }
      res.add(new Factor(primes[p], coef));
    }
    if (n > 1) {
      res.add(new Factor(n, 1));
    }
    return Iterables.toArray(res, Factor.class);
  }

  private int[] primes(int n) {
    boolean[] sieve = new boolean[n];
    for (int d = 2; d < n; d++) {
      if (!sieve[d]) {
        for (long k = ((long) d) * d; k < n; k += d) {
          sieve[(int) k] = true;
        }
      }
    }
    List<Integer> primes = Lists.newArrayList();
    for (int i = 2; i < n; i++) {
      if (!sieve[i]) {
        primes.add(i);
      }
    }
    return Ints.toArray(primes);
  }

  private static class Factor {
    final int prime;
    final int coef;

    public Factor(int prime, int coef) {
      this.prime = prime;
      this.coef = coef;
    }
  }
}
