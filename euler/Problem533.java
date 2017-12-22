package euler;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Ordering;
import com.google.common.math.LongMath;

public class Problem533 {

  public static void main(String[] args) {
    System.out.println(new Problem533().solve());
  }

  public BigInteger solve() {
    long n = 20_000_000;
    FactorizationSieve sieve = new FactorizationSieve(n + 1);
    BigInteger max = BigInteger.ZERO;
    for (int k = 2; k < n; k++) {
      Factorization factorization = sieve.factorization(k);
      Map<Long, Integer> m = new HashMap<>();
      for (Factorization.PrimeFactor factor : factorization.factors()) {
        if (factor.prime() == 2) {
          m.put(2L, factor.exp() + 2);
        } else {
          if (k % (factor.prime() - 1) == 0) {
            m.put((long) factor.prime(), factor.exp() + 1);
          }
        }
      }
      for (long div : sieve.divisors(k)) {
        if (sieve.isPrime(div + 1) && !m.containsKey(div + 1)) {
          m.put(div + 1, 1);
        }
      }
      if (lambda(m) == k) {
        max = Ordering.natural().max(max, eval(m));
      }
    }
    return max.add(BigInteger.ONE).mod(BigInteger.valueOf(1_000_000_000L));
  }

  private BigInteger eval(Map<Long, Integer> f) {
    BigInteger prod = BigInteger.ONE;
    for (Map.Entry<Long, Integer> entry : f.entrySet()) {
      prod = prod.multiply(BigInteger.valueOf(entry.getKey()).pow(entry.getValue()));
    }
    return prod;
  }

  private long lambda(Map<Long, Integer> f) {
    long res = 1L;
    for (Map.Entry<Long, Integer> entry : f.entrySet()) {
      Long prime = entry.getKey();
      Integer exp = entry.getValue();
      if (prime == 2) {
        if (exp <= 2) {
          res = lcm(res, 1L << (exp - 1));
        } else {
          res = lcm(res, 1L << (exp - 2));
        }
      } else {
        res = lcm(res, (prime - 1) * LongMath.pow(prime, exp - 1));
      }
    }
    return res;
  }

  private long lcm(long a, long b) {
    return a / LongMath.gcd(a, b) * b;
  }
}
