package euler;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Problem224 {

  public static void main(String[] args) {
    System.out.println(new Problem224().solve(75000000));
  }

  public long solve(int n) {
    Factor[][] factors = new Factor[n / 3][0];
    int[] primes = primes(n / 3 + 1);
    for (int prime : primes) {
      System.out.println(prime);
      int sqrt1 = Ints.checkedCast(modSqrt(prime - 1, prime));
      if (sqrt1 != -1) {
        int sqrt2 = prime - sqrt1;
        Preconditions.checkState((1L * sqrt1 * sqrt1) % prime == prime - 1);
        Preconditions.checkState((1L * sqrt2 * sqrt2) % prime == prime - 1);
        for (int c : (sqrt1 == sqrt2) ? new int[] {sqrt1} : new int[] {sqrt1, sqrt2}) {
          int d = c;
          while (d < factors.length) {
            int coef = 0;
            long x = 1L * d * d + 1;
            while (x % prime == 0) {
              coef++;
              x /= prime;
            }
            factors[d] = Arrays.copyOf(factors[d], factors[d].length + 1);
            factors[d][factors[d].length - 1] = new Factor(prime, coef);
            d += prime;
          }
        }
      }
    }
    for (int i = 0; i < factors.length; i++) {
      long x = 1L * i * i + 1;
      long z = eval(factors[i]);
      if (z < x) {
        factors[i] = Arrays.copyOf(factors[i], factors[i].length + 1);
        factors[i][factors[i].length - 1] = new Factor(x / z, 1);
      }
    }
    return 0L;
  }

  private int[] primes(int n) {
    List<Integer> primes = Lists.newArrayList();
    boolean[] sieve = new boolean[n];
    for (int p = 2; p < sieve.length; p++) {
      if (!sieve[p]) {
        primes.add(p);
        if (1L * p * p < sieve.length) {
          int d = p * p;
          while (d < sieve.length) {
            sieve[d] = true;
            d += p;
          }
        }
      }
    }
    return Ints.toArray(primes);
  }

  private long eval(Factor[] factors) {
    long res = 1L;
    for (Factor factor : factors) {
      res *= LongMath.pow(factor.prime, factor.coef);
    }
    return res;
  }

  private static class Factor {
    private final long prime;
    private final int coef;

    public Factor(long prime, int coef) {
      this.prime = prime;
      this.coef = coef;
    }

    @Override
    public String toString() {
      return prime + "^" + coef;
    }
  }

  private long modSqrt(long a, long p) {
    if (legendreSymbol(a, p) != 1) {
      return -1;
    } else if (a == 0) {
      return 0;
    } else if (p == 2) {
      return a;
    } else if (p % 4 == 3) {
      return modPow(a, (p + 1) / 4, p);
    } else {
      long s = p - 1;
      int e = 0;
      while (s % 2 == 0) {
        s /= 2;
        e += 1;
      }
  
      long n = 2;
      while (legendreSymbol(n, p) != -1) {
        n += 1;
      }
  
      long x = modPow(a, (s + 1) / 2, p);
      long b = modPow(a, s, p);
      long g = modPow(n, s, p);
      int r = e;
  
      while (true) {
        long t = b;
        int m = 0;
        for (; m < r; m++) {
          if (t == 1) {
            break;
          }
          t = modPow(t, 2, p);
        }

        if (m == 0) {
          return x;
        }

        long gs = modPow(g, LongMath.pow(2, r - m - 1), p);
        g = (gs * gs) % p;
        x = (x * gs) % p;
        b = (b * g) % p;
        r = m;
      }
    }
  }


  private long legendreSymbol(long a, long p) {
    long ls = modPow(a, (p - 1) / 2, p);
    return (p > 2 && ls == p - 1) ? -1 : ls;
  }

  private long modPow(long a, long exp, long m) {
    if (exp == 0) {
      return 1;
    } else {
      long x = modPow(a, exp / 2, m);
      if (exp % 2 == 0) {
        return (x * x) % m;
      } else {
        return (((x * x) % m) * a) % m;
      }
    }
  }
}
