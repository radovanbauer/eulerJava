package euler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Problem445 {
  public static void main(String[] args) {
    Runner.run(new Problem445()::solve);
  }

  // a*(a*r+b)+b == a*r+b
  // a*a*r + a*b == a*r
  // r*(a*a-a) = -a*b
  // r*(a'*a-a') = -a'*b (mod n/gcd(n,a))
  // r*(a-1) = -b
  //
  // g = gcd(n,a)
  // a=1 (mod n/g)
  // a=0 (mod g)
  // b=0 (mod n/g)
  // gcd(g, n/g) = 1
  //
  // R(n) = sum(d, d|n && d<n && gcd(d, n/d)==1)
  // R(n = p1^a1 * p2^a2 * ... * pk^ak) = (1+p1^a1)*(1+p2^a2)*...*(1+pk^ak) - n
  //
  // sum()

  public long solve() {
    long n = 10_000_000;
    long mod = 1_000_000_007;
    FactorizationSieve sieve = new FactorizationSieve(n);

    LongMod r = LongMod.create(1, mod);
    Map<Long, Long> f = new HashMap<>();

    LongMod sum = LongMod.zero(mod);

    for (long k = 1; k < n; k++) {
      // c(n,k) = c(n,k-1) * (n-k+1) / k
      Factorization nk1f = sieve.factorization(n - k + 1);
      Factorization kf = sieve.factorization(k);
      Set<Long> primes = new HashSet<>();
      primes.addAll(nk1f.getPrimes());
      primes.addAll(kf.getPrimes());

      Map<Long, Long> oldF = new HashMap<>();
      for (long prime : primes) {
        long oldExp = f.getOrDefault(prime, 0L);
        oldF.put(prime, oldExp);
        long newExp = oldExp + nk1f.getExponent(prime) - kf.getExponent(prime);
        if (newExp == 0) {
          f.remove(prime);
        } else {
          f.put(prime, newExp);
        }
      }

      for (long prime : primes) {
        long oldExp = oldF.getOrDefault(prime, 0L);
        long newExp = f.getOrDefault(prime, 0L);
        if (oldExp > 0) {
          r = r.divide(LongMod.create(prime, mod).pow(oldExp).add(1));
        }
        if (newExp > 0) {
          r = r.multiply(LongMod.create(prime, mod).pow(newExp).add(1));
        }
      }

      sum = sum.add(r);
    }

//    for (int n = 2; n <= 1000; n++) {
//      int count = 0;
//      for (int a = 1; a < n; a++) {
//        for (int b = 0; b < n; b++) {
//          if (test(n, a, b)) {
////            System.out.println(n + " " + a + " " + b);
//            count++;
//          }
//        }
//      }
//      int count2 = 0;
//      for (long d : sieve.divisors(n)) {
//        if (d < n && LongMath.gcd(d, n / d) == 1) {
//          count2 += d;
//        }
//      }
//      int count3 = 1;
//      Factorization factorization = sieve.factorization(n);
//      for (Long p : factorization.getPrimes()) {
//        count3 *= 1 + LongMath.pow(p, factorization.getExponent(p));
//      }
//      count3 -= n;
//
//      System.out.println(n + ": " + count + " vs " + count2 + " vs " + count3);
//      Preconditions.checkState(count == count2);
//      Preconditions.checkState(count2 == count3);
//    }

    return sum.subtract(LongMod.create(2, mod).pow(n).subtract(2)).n();
  }

  private boolean test(int n, int a, int b) {
    for (int x = 0; x < n; x++) {
      LongMod axb = LongMod.create(a, n).multiply(x).add(b);
      if (!axb.multiply(a).add(b).equals(axb)) {
        return false;
      }
    }
    return true;
  }
}
