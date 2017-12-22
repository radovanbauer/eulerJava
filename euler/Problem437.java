package euler;

import static com.google.common.math.LongMath.checkedMultiply;
import static com.google.common.math.LongMath.mod;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem437 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem437().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 100_000_000;
    FactorizationSieve sieve = new FactorizationSieve(max);
    long sum = 5;
    for (long n = 7; n < max; n++) {
      if ((n & 0xFFFFF) == 0) {
        System.out.println(n);
      }
      if (sieve.isPrime(n) && legendreSymbol(5, n) == 1) {
        long x = LongMod.create(modSqrt(5, n), n).add(1).divide(2).n();
        if (isPrimitiveRoot(x, n, sieve) || isPrimitiveRoot(n - x, n, sieve)) {
          sum += n;
        }
      }
    }
    return sum;
  }

  private boolean isPrimitiveRoot(long a, long p, FactorizationSieve sieve) {
    for (int div : sieve.primeDivisors(p - 1)) {
      if (div < p - 1) {
        if (LongMod.create(a, p).pow((p - 1) / div).n() == 1) {
          return false;
        }
      }
    }
    return true;
  }

  private long modSqrt(long a, long p) {
    if (legendreSymbol(a, p) != 1) {
      return 0;
    } else if (a == 0) {
      return 0;
    } else if (p == 2) {
      return p;
    } else if (p % 4 == 3) {
      return powMod(a, (p + 1) / 4, p);
    }

    long s = p - 1;
    long e = 0;
    while (s % 2 == 0) {
      s /= 2;
      e++;
    }

    long n = 2;
    while (legendreSymbol(n, p) != -1) {
      n++;
    }

    long x = powMod(a, (s + 1) / 2, p);
    long b = powMod(a, s, p);
    long g = powMod(n, s, p);
    long r = e;

    while (true) {
      long t = b;
      long m = 0;
      for (; m < r; m++) {
        if (t == 1) {
          break;
        }
        t = powMod(t, 2, p);
      }

      if (m == 0) {
        return x;
      }

      long gs = powMod(g, 1L << (r - m - 1), p);
      g = (gs * gs) % p;
      x = (x * gs) % p;
      b = (b * g) % p;
      r = m;
    }
  }

  private long legendreSymbol(long a, long p) {
    long ls = powMod(a, (p - 1) / 2, p);
    return ls == p - 1 ? -1 : ls;
  }

  private long powMod(long base, long exp, long mod) {
    if (exp == 0) {
      return 1L;
    }
    long x = powMod(base, exp / 2, mod);
    if (exp % 2 == 0) {
      return mod(checkedMultiply(x, x), mod);
    } else {
      return mod(checkedMultiply(base, mod(checkedMultiply(x, x), mod)), mod);
    }
  }
}
