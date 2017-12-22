package euler;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.math.BigInteger;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkState;
import static java.math.RoundingMode.FLOOR;

public class Problem446 {
  public static void main(String[] args) {
    Runner.run(new Problem446()::solve);
  }

  // n^4+4 = (n^2 + 2n + 2)*(n^2 - 2n - 2)

  public long solve() {
    int n = 10_000_000;
    long[] primes = Longs.toArray(ImmutableList.copyOf(PrimeSieve.create(LongMath.sqrt((long) n * n + 2 * n + 2, FLOOR))));
    Multiset<Long>[] f = new Multiset[n + 1];

    long[] productPlus = new long[n + 1];
    long[] productMinus = new long[n + 1];
    for (int i = 1; i <= n; i++) {
      productPlus[i] = (long) i * i + 2*i + 2;
      productMinus[i] = (long) i * i - 2*i + 2;
      f[i] = HashMultiset.create();
    }

    for (long p : primes) {
      // (n^2+2n+2) == 0 (mod p)
      // (n+1)^2 == -1 (mod p)
      // (n^2-2n+2) == 0 (mod p)
      // (n-1)^2 == -1 (mod p)
      Optional<LongMod> sqrt = LongMod.create(-1, p).sqrt();
      if (sqrt.isPresent()) {
        System.out.println(p + ": " + sqrt.get());
        long s = sqrt.get().n();

        sieve(n, f, productPlus, p, Ints.checkedCast(s - 1));
        if (s != p - s) {
          sieve(n, f, productPlus, p, Ints.checkedCast(p - s - 1));
        }

        sieve(n, f, productMinus, p, Ints.checkedCast(s + 1));
        if (s != p - s) {
          sieve(n, f, productMinus, p, Ints.checkedCast(p - s + 1));
        }
      }
    }

    for (int i = 1; i <= n; i++) {
      if (productPlus[i] > 1) {
//        checkState(BigInteger.valueOf(productPlus[i]).isProbablePrime(10));
        f[i].add(productPlus[i], 1);
      }
      if (productMinus[i] > 1) {
//        checkState(BigInteger.valueOf(productMinus[i]).isProbablePrime(10));
        f[i].add(productMinus[i], 1);
      }
//      checkState(product(f[i]).equals(BigInteger.valueOf(i).pow(4).add(BigInteger.valueOf(4))));
    }

    int mod = 1_000_000_007;
    LongMod sum = LongMod.create(0, mod);
    for (int i = 1; i <= n; i++) {
      LongMod product = LongMod.create(1, mod);
      for (Multiset.Entry<Long> entry : f[i].entrySet()) {
        product = product.multiply(LongMod.create(entry.getElement(), mod).pow(entry.getCount()).add(1));
      }
      sum = sum.add(product).subtract(LongMod.create(i, mod).pow(4).add(4));
    }

    return sum.n();
  }

  private void sieve(int n, Multiset<Long>[] fact, long[] product, long p, int start) {
    for (int i = start; i <= n; i += p) {
      if (i == 0) {
        continue;
      }
      int exp = 1;
      checkState(product[i] % p == 0);
      product[i] /= p;
      while (product[i] % p == 0) {
        product[i] /= p;
        exp++;
      }
      fact[i].add(p, exp);
    }
  }

  private BigInteger product(Multiset<Long> fact) {
    BigInteger res = BigInteger.ONE;
    for (Multiset.Entry<Long> entry : fact.entrySet()) {
      res = res.multiply(BigInteger.valueOf(entry.getElement()).pow(entry.getCount()));
    }
    return res;
  }
}
