package euler;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;


public class Problem223 {

  public static void main(String[] args) {
    System.out.println(new Problem223().solve(25000000));
  }

  public long solve(int n) {
    long cnt = 1L;
    for (long b = 2; 2 * b <= n; b++) {
      if (b % 10000 == 0) System.out.println(b);
      long x = (b - 1) * (b + 1);
      Factor[] xFactors = multiply(factorize(b - 1), factorize(b + 1));
      for (long d1 : divisors(xFactors)) {
        long d2 = x / d1;
        if (d1 < d2 && (d1 + d2) % 2 == 0) {
          long c = (d1 + d2) / 2;
          long a = c - d1;
          if (a <= b && b <= c && a + b + c <= n) {
            Preconditions.checkState(a >= 0 && a * a + b * b == c * c + 1);
            cnt++;
          }
        }
      }
    }
    return cnt;
  }

  private long[] divisors(Factor[] factors) {
    List<Long> result = Lists.newArrayList();
    divisors(factors, 0, 1, result);
    return Longs.toArray(result);
  }

  private void divisors(Factor[] factors, int k, long prod, List<Long> result) {
    if (k == factors.length) {
      result.add(prod);
    } else {
      long x = 1;
      for (int coef = 0; coef <= factors[k].coef; coef++) {
        divisors(factors, k + 1, prod * x, result);
        x *= factors[k].prime;
      }
    }
  }

  private Factor[] factorize(long n) {
    Preconditions.checkArgument(n >= 1);
    List<Factor> factors = Lists.newArrayList();
    for (long d = 2; d * d <= n; d++) {
      if (n % d == 0) {
        int coef = 0;
        while (n % d == 0) {
          coef++;
          n /= d;
        }
        factors.add(new Factor(d, coef));
      }
    }
    if (n > 1) {
      factors.add(new Factor(n, 1));
    }
    return Iterables.toArray(factors, Factor.class);
  }

  private Factor[] multiply(Factor[] factors1, Factor[] factors2) {
    List<Factor> result = Lists.newArrayList();
    int i1 = 0, i2 = 0;
    while (i1 < factors1.length || i2 < factors2.length) {
      if (i1 == factors1.length) {
        result.add(factors2[i2++]);
      } else if (i2 == factors2.length) {
        result.add(factors1[i1++]);
      } else if (factors1[i1].prime == factors2[i2].prime) {
        result.add(new Factor(factors1[i1].prime, factors1[i1].coef + factors2[i2].coef));
        i1++;
        i2++;
      } else if (factors1[i1].prime < factors2[i2].prime) {
        result.add(factors1[i1++]);
      } else {
        result.add(factors2[i2++]);
      }
    }
    return Iterables.toArray(result, Factor.class);
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
}
