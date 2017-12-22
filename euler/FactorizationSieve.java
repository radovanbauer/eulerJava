package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.math.IntMath;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.primitives.Ints;

public class FactorizationSieve {
  private final long max;
  private final int[] smallestPrimeDivisor;
  private final int[] smallestPrimeExp;

  public FactorizationSieve(long max) {
    checkArgument(max + 1 <= Integer.MAX_VALUE);
    this.max = max;
    boolean[] nonPrimes = new boolean[Ints.checkedCast(max + 1)];
    smallestPrimeDivisor = new int[Ints.checkedCast(max + 1)];
    smallestPrimeExp = new int[Ints.checkedCast(max + 1)];
    for (int i = 2; i <= max; i++) {
      if (!nonPrimes[i]) {
        smallestPrimeDivisor[i] = i;
        smallestPrimeExp[i] = 1;
        long iPow = i;
        while (iPow <= max) {
          for (long j = (iPow == i ? 1L * i * i : iPow); j <= max; j += iPow) {
            if (!nonPrimes[(int) j]) {
              smallestPrimeExp[(int) j]++;
            }
          }
          iPow *= i;
        }
        for (long j = 1L * i * i; j <= max; j += i) {
          if (!nonPrimes[(int) j]) {
            smallestPrimeDivisor[(int) j] = i;
            nonPrimes[(int) j] = true;
          }
        }
      }
    }
  }

  public boolean isPrime(long n) {
    checkArgument(n <= max);
    if (n < 2) {
      return false;
    }
    return smallestPrimeDivisor(n) == n;
  }

  public ImmutableList<Long> getAllPrimes() {
    return getPrimes(1, max);
  }

  public ImmutableList<Long> getPrimes(long max) {
    return getPrimes(1, max);
  }

  public ImmutableList<Long> getPrimes(long min, long max) {
    checkArgument(max <= this.max && min <= max);
    ImmutableList.Builder<Long> result = ImmutableList.builder();
    for (int p = Ints.checkedCast(min); p <= max; p++) {
      if (smallestPrimeDivisor[p] == p) {
        result.add((long) p);
      }
    }
    return result.build();
  }

  public int smallestPrimeDivisor(long n) {
    checkArgument(n >= 2 && n <= max);
    return smallestPrimeDivisor[Ints.checkedCast(n)];
  }

  public int smallestPrimeExp(long n) {
    checkArgument(n >= 2 && n <= max);
    return smallestPrimeExp[Ints.checkedCast(n)];
  }

  public List<Integer> divisors(long n) {
    checkArgument(n > 0 && n <= max, "Illegal n: %s", n);
    List<Integer> result = new ArrayList<>();
    result.add(1);
    divisors(Ints.checkedCast(n), 1, result);
    Collections.sort(result);
    return ImmutableList.copyOf(result);
  }

  public List<Integer> unsortedDivisors(long n) {
    checkArgument(n > 0 && n <= max, "Illegal n: %s", n);
    List<Integer> result = new ArrayList<>();
    result.add(1);
    divisors(Ints.checkedCast(n), 1, result);
    return ImmutableList.copyOf(result);
  }

  private void divisors(int n, int k, List<Integer> result) {
    if (n == 1) {
      return;
    }
    int prime = smallestPrimeDivisor[n];
    int exp = smallestPrimeExp[n];
    int primeToExp = IntMath.pow(prime, exp);
    for (int i = 0; i <= exp; i++) {
      if (i != 0) {
        result.add(k);
      }
      divisors(n / primeToExp, k, result);
      k *= prime;
    }
  }

  public int divisorCount(long n) {
    checkArgument(n > 0 && n <= max, "Illegal n: %s", n);
    int nInt = Ints.checkedCast(n);
    int count = 1;
    while (nInt > 1) {
      int p = smallestPrimeDivisor[nInt];
      int exp = smallestPrimeExp[nInt];
      count *= exp + 1;
      nInt /= IntMath.pow(p, exp);
    }
    return count;
  }

  public ImmutableList<Integer> primeDivisors(long n) {
    checkArgument(n > 0 && n <= max, "Illegal n: %s", n);
    int nInt = Ints.checkedCast(n);
    ImmutableList.Builder<Integer> divisors = ImmutableList.builder();
    while (nInt > 1) {
      int prime = smallestPrimeDivisor[nInt];
      divisors.add(prime);
      nInt /= IntMath.pow(prime, smallestPrimeExp[nInt]);
    }
    return divisors.build();
  }

  public ImmutableList<Integer> primeExponents(long n) {
    checkArgument(n > 0 && n <= max, "Illegal n: %s", n);
    int nInt = Ints.checkedCast(n);
    ImmutableList.Builder<Integer> exponents = ImmutableList.builder();
    while (nInt > 1) {
      exponents.add(smallestPrimeExp[nInt]);
      nInt /= IntMath.pow(smallestPrimeDivisor[nInt], smallestPrimeExp[nInt]);
    }
    return exponents.build();
  }

  public int totient(long n) {
    checkArgument(n > 0 && n <= max, "Illegal n: %s", n);
    int nInt = Ints.checkedCast(n);
    int res = 1;
    while (nInt > 1) {
      int p = smallestPrimeDivisor[nInt];
      int pPow = IntMath.pow(p, smallestPrimeExp[nInt] - 1);
      res *= (p - 1) * pPow;
      nInt /= pPow * p;
    }
    return res;
  }

  public Factorization factorization(long n) {
    checkArgument(n > 0 && n <= max, "Illegal n: %s", n);
    int nInt = Ints.checkedCast(n);
    ImmutableSortedMap.Builder<Long, Integer> factors = ImmutableSortedMap.naturalOrder();
    while (nInt > 1) {
      factors.put((long) smallestPrimeDivisor[nInt], smallestPrimeExp[nInt]);
      nInt /= IntMath.pow(smallestPrimeDivisor[nInt], smallestPrimeExp[nInt]);
    }
    return Factorization.create(factors.build());
  }
}