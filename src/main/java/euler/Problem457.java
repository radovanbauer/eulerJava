package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.math.LongMath.checkedMultiply;
import static com.google.common.math.LongMath.mod;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import com.google.common.math.LongMath;

public class Problem457 {

  public static void main(String[] args) {
    System.out.println(new Problem457().solve());
  }

  public BigInteger solve() {
    int max = 10_000_000;
    Primes primes = new Primes(max);
    BigInteger sum = BigInteger.ZERO;
    for (int i = 2; i <= max; i++) {
      if (primes.isPrime(i)) {
        sum = sum.add(r(i));
      }
    }
    return sum;
  }

  private BigInteger r(long p) {
    long s1 = modSqrt(13, p);
    if (s1 == 0) {
      return BigInteger.ZERO;
    }
    long s2 = p - s1;
    long t1 = s1 + p*LongMath.mod(-(s1*s1 - 13)/p*modInv(2*s1, p), p);
    long t2 = s2 + p*LongMath.mod(-(s2*s2 - 13)/p*modInv(2*s2, p), p);
    BigInteger x1 = BigInteger.valueOf(t1 + 3)
        .multiply(BigInteger.valueOf(modInv(2, p*p))).mod(BigInteger.valueOf(p*p));
    BigInteger x2 = BigInteger.valueOf(t2 + 3)
        .multiply(BigInteger.valueOf(modInv(2, p*p))).mod(BigInteger.valueOf(p*p));
    return Collections.min(Arrays.asList(x1, x2));
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

  private long modInv(long a, long mod) {
    long r1 = mod;
    long r2 = a;
    long s1 = 0;
    long s2 = 1;
    while (true) {
      long q = r1 / r2;
      long r = r1 - q * r2;
      if (r == 0) {
        return LongMath.mod(s2, mod);
      }
      long s = s1 - q * s2;
      r1 = r2; r2 = r;
      s1 = s2; s2 = s;
    }
  }

  private static class Primes {
    private final int max;
    private final boolean[] nonPrimes;

    public Primes(int max) {
      this.max = max;
      nonPrimes = new boolean[max + 1];
      for (int i = 2; i <= max; i++) {
        if (!nonPrimes[i]) {
          for (long j = 1L * i * i; j <= max; j += i) {
            if (!nonPrimes[(int) j]) {
              nonPrimes[(int) j] = true;
            }
          }
        }
      }
    }

    public boolean isPrime(int n) {
      checkArgument(n <= max);
      return n > 1 && !nonPrimes[n];
    }
  }
}
