package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.math.LongMath.checkedMultiply;
import static com.google.common.math.LongMath.mod;

import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.common.base.Preconditions;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;

public class Problem451 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem451().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 20_000_000;
    Primes primes = new Primes(max);
    return IntStream.rangeClosed(3, max).parallel()
        .mapToLong(n -> l(primes, n))
        .sum();
  }

  private long l(Primes primes, int n) {
    ImmutableList<Long> roots = modUnitSqrt(primes, n);
    return roots.stream().filter(l -> l < n - 1).max(Comparator.naturalOrder()).get();
  }

  private ImmutableList<Long> modUnitSqrt(Primes primes, int n) {
    checkArgument(n >= 2);
    int p = primes.smallestPrimeDivisor(n);
    int k = 0;
    int m = n;
    int pToK = 1;
    while (m % p == 0) {
      m /= p;
      k++;
      pToK *= p;
    }
    ImmutableList<Long> result;
    if (m == 1) {
      result = modUnitSqrt(p, k);
    } else {
      ImmutableList<Long> roots1 = modUnitSqrt(primes, m);
      ImmutableList<Long> roots2 = modUnitSqrt(p, k);
      ImmutableList.Builder<Long> resultBuilder = ImmutableList.builder();
      for (long r1 : roots1) {
        for (long r2 : roots2) {
          resultBuilder.add(chineseRem(new long[] {r1, r2}, new long[] {m, pToK}));
        }
      }
      result = resultBuilder.build();
    }
    return result;
  }

  // r^2 == 1 (mod p^k)
  private ImmutableList<Long> modUnitSqrt(long p, int k) {
    Preconditions.checkArgument(k >= 1);
    if (p == 2) {
      if (k == 1) {
        return ImmutableList.of(1L);
      } else if (k == 2) {
        return ImmutableList.of(1L, 3L);
      } else {
        return ImmutableList.of(1L, (1L << (k - 1)) - 1, (1L << (k - 1)) + 1, (1L << k) - 1);
      }
    } else {
      long r1 = 1;
      long r2 = p - 1;
      long pPow = 1;
      for (int i = 2; i <= k; i++) {
        pPow = checkedMultiply(pPow, p);
        long t1 = mod(-checkedMultiply((checkedMultiply(r1, r1) - 1) / pPow, modInv(2 * r1, p)), p);
        long t2 = mod(-checkedMultiply((checkedMultiply(r2, r2) - 1) / pPow, modInv(2 * r2, p)), p);
        long s1 = r1 + t1 * pPow;
        long s2 = r2 + t2 * pPow;
        r1 = s1; r2 = s2;
      }
      return ImmutableList.of(r1, r2);
    }
  }

  private long chineseRem(long[] rems, long[] mods) {
    checkArgument(rems.length == mods.length && rems.length > 0);
    long N = 1L;
    for (long mod : mods) {
      N = checkedMultiply(N, mod);
    }
    long res = 0;
    for (int i = 0; i < rems.length; i++) {
      res += checkedMultiply(checkedMultiply(rems[i], (N / mods[i])), modInv(N / mods[i], mods[i]));
    }
    return mod(res, N);
  }

  private long modInv(long a, long mod) {
    long r1 = mod;
    long r2 = a;
    long s1 = 0;
    long s2 = 1;
    while (true) {
      long q = r1 / r2;
      long r = r1 - q * r2;
      if (r == 0) {
        return mod(s2, mod);
      }
      long s = s1 - q * s2;
      r1 = r2; r2 = r;
      s1 = s2; s2 = s;
    }
  }

  private static class Primes {
    private final int max;
    private final boolean[] nonPrimes;
    private final int[] smallestPrimeDivisor;

    public Primes(int max) {
      this.max = max;
      nonPrimes = new boolean[max + 1];
      smallestPrimeDivisor = new int[max + 1];
      for (int i = 2; i <= max; i++) {
        if (!nonPrimes[i]) {
          smallestPrimeDivisor[i] = i;
          long j = 1L * i * i;
          while (j <= max) {
            if (!nonPrimes[(int) j]) {
              nonPrimes[(int) j] = true;
              smallestPrimeDivisor[(int) j] = i;
            }
            j += i;
          }
        }
      }
    }

    public int smallestPrimeDivisor(int n) {
      checkArgument(n >= 2 && n <= max);
      return smallestPrimeDivisor[n];
    }
  }
}
