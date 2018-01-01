package euler;

import static com.google.common.math.LongMath.checkedAdd;
import static com.google.common.math.LongMath.checkedMultiply;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.common.math.LongMath;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableSortedSet;

public class Problem421 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem421().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int maxp = 100_000_000;
    long maxn = 100_000_000_000L;
    FactorizationSieve sieve = new FactorizationSieve(maxp);
    return IntStream.rangeClosed(2, maxp).parallel().mapToLong(p -> {
      long sum = 0;
      if (sieve.isPrime(p)) {
        ImmutableSortedSet<Long> roots = modSqrt15(-1, p);
        for (long root : roots) {
          if (root <= maxn) {
            sum = checkedAdd(sum, checkedMultiply(p, 1 + (maxn - root) / p));
          }
        }
      }
      return sum;
    }).sum();
  }

  private ImmutableSortedSet<Long> modSqrt15(long δ, long q) {
    ImmutableSortedSet<Long> cubicRoots = modSqrt(δ, 3, q);
    ImmutableSortedSet.Builder<Long> result = ImmutableSortedSet.naturalOrder();
    for (long root : cubicRoots) {
      result.addAll(modSqrt(root, 5, q));
    }
    return result.build();
  }

  // Computes r-th roots of δ mod q. Both r and q must be primes.
  private ImmutableSortedSet<Long> modSqrt(long δ, long r, long q) {
    if (LongMod.create(δ, q).n() == 0) {
      return ImmutableSortedSet.of(0L);
    } else if (LongMath.gcd(r, q - 1) == 1) {
      LongMod u = LongMod.create(-1, r).divide(q - 1);
      long v = q * u.n() / r;
      LongMod res = LongMod.create(δ, q).pow(v);
      return ImmutableSortedSet.of(res.n());
    } else if ((q - 1) % r == 0) {
      if (LongMod.create(δ, q).pow((q - 1) / r).n() != 1) {
        return ImmutableSortedSet.of();
      }

      // Step 1: Choose ρ uniformly at random from F∗q
      // Step 2: if ρ^((q−1)/r) = 1, go to Step 1.
      LongMod ρ = LongMod.create(1, q);
      LongMod rootOf1 = LongMod.create(1, q);
      while (rootOf1.n() == 1) {
        ρ = ρ.add(1);
        rootOf1 = ρ.pow((q - 1) / r);
      }

      // Step 3: Compute t, s such that q − 1 = r^t*s, where (r, s) = 1.
      int t = 0;
      long s = q - 1;
      while (s % r == 0) {
        t++;
        s /= r;
      }

      // Compute the least nonnegative integer α such that s|rα − 1.
      long α = s == 1 ? 0 : LongMod.create(r, s).invert().n();

      // Compute a ← ρ^((r^(t−1))s), b ← δ^(rα−1), c ← ρ^s, h ← 1
      LongMod a = ρ.pow((q-1) / r);
      LongMod b = LongMod.create(δ, q).pow(r * α - 1);
      LongMod c = ρ.pow(s);
      LongMod h = LongMod.create(1, q);

      // Step 4: for i = 1 to t − 1
      //   compute d = b^(r^(t−1−i))
      //   if d = 1, j ← 0,
      //   else j ← − log_a(d) (compute the discrete logarithm)
      //   b ← b (c^r)^j, h ← h c^j
      //   c ← c^r
      // end for
      for (int i = 1; i <= t - 1; i++) {
        LongMod d = b.pow(LongMath.pow(r, t - 1 - i));
        long j = d.n() == 1 ? 0 : dLog(a.n(), d.n(), q); // TODO
        b = b.multiply(c.pow(r).pow(j));
        h = h.multiply(c.pow(j));
        c = c.pow(r);
      }
      // Step 5: return δ^α * h
      LongMod root = LongMod.create(δ, q).pow(α).multiply(h);

      ImmutableSortedSet.Builder<Long> result = ImmutableSortedSet.naturalOrder();
      for (int i = 0; i < r; i++) {
        result.add(root.n());
        root = root.multiply(rootOf1);
      }
      return result.build();
    } else {
      throw new UnsupportedOperationException();
    }
  }

  private long dLog(long a, long d, long q) {
    LongMod x = LongMod.create(1, q);
    int exp = 0;
    LongMod dm = LongMod.create(d, q);
    while (!x.equals(dm)) {
      x = x.multiply(a);
      exp++;
    }
    return exp;
  }
}
