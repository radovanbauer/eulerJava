package euler;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Problem203 {

  public static void main(String[] args) {
    System.out.println(new Problem203().solve(50));
  }

  public long solve(int maxn) {
    int[] primes = primes(100);
    PrimeNum[] facts = new PrimeNum[maxn + 1];
    facts[0] = PrimeNum.valueOf(1, primes);
    for (int i = 1; i <= maxn; i++) {
      facts[i] = facts[i - 1].multiply(PrimeNum.valueOf(i, primes));
    }
    Set<Long> distinct = Sets.newHashSet();
    for (int n = 0; n <= maxn; n++) {
      for (int k = 0; k <= n; k++) {
        PrimeNum comb = facts[n].divide(facts[n - k]).divide(facts[k]);
        if (comb.isSquareFree()) {
          distinct.add(comb.longValue());
        }
      }
    }
    long sum = 0L;
    for (long i : distinct) {
      sum += i;
    }
    return sum;
  }

  private static class PrimeNum {
    private final int[] primes;
    private final int[] coefs;

    private PrimeNum(int[] primes, int[] coefs) {
      this.primes = primes;
      if (coefs.length == 0 || coefs[coefs.length - 1] > 0) {
        this.coefs = coefs;
      } else {
        int last = coefs.length - 2;
        while (last >= 0 && coefs[last] == 0) {
          last--;
        }
        this.coefs = Arrays.copyOf(coefs, last + 1);
      }
    }

    private PrimeNum(int[] primes, Collection<Integer> coefs) {
      this(primes, Ints.toArray(coefs));
    }

    public static PrimeNum valueOf(int n, int[] primes) {
      int d = 2;
      List<Integer> coefs = Lists.newArrayList();
      for (int idx = 0; n > 1; idx++) {
        int coef = 0;
        while (n % primes[idx] == 0) {
          coef++;
          n /= primes[idx];
        }
        coefs.add(coef);
      }
      return new PrimeNum(primes, coefs);
    }
    
    public PrimeNum multiply(PrimeNum that) {
      int[] coefs1, coefs2;
      if (this.coefs.length <= that.coefs.length) {
        coefs1 = this.coefs;
        coefs2 = that.coefs;
      } else {
        coefs1 = that.coefs;
        coefs2 = this.coefs;
      }
      int[] resCoefs = coefs2.clone();
      for (int i = 0; i < coefs1.length; i++) {
        resCoefs[i] += coefs1[i];
      }
      return new PrimeNum(primes, resCoefs);
    }

    public PrimeNum divide(PrimeNum that) {
      int[] resCoefs = this.coefs.clone();
      for (int i = 0; i < that.coefs.length; i++) {
        resCoefs[i] -= that.coefs[i];
      }
      return new PrimeNum(primes, resCoefs);
    }

    public boolean isSquareFree() {
      for (int i = 0; i < coefs.length; i++) {
        if (coefs[i] > 1) {
          return false;
        }
      }
      return true;
    }

    public long longValue() {
      long res = 1L;
      for (int i = 0; i < coefs.length; i++) {
        if (coefs[i] > 0) {
          res *= primes[i] * coefs[i];
        }
      }
      return res;
    }

    public BigInteger bigIntegerValue() {
      BigInteger res = BigInteger.ONE;
      for (int i = 0; i < coefs.length; i++) {
        if (coefs[i] > 0) {
          res = res.multiply(BigInteger.valueOf(primes[i]).pow(coefs[i]));
        }
      }
      return res;
    }

    @Override
    public String toString() {
      return bigIntegerValue().toString();
    }
  }

  private int[] primes(int n) {
    boolean[] sieve = new boolean[n];
    List<Integer> res = Lists.newArrayList();
    for (int d = 2; d < n; d++) {
      if (!sieve[d]) {
        res.add(d);
        long k = 1L * d * d;
        while (k < n) {
          sieve[(int) k] = true;
          k += d;
        }
      }
    }
    return Ints.toArray(res);
  }
}
