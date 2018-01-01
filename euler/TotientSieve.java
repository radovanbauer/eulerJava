package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.RoundingMode;

import com.google.common.math.LongMath;

import com.google.common.primitives.Ints;

public class TotientSieve {
  private final long min;
  private final long max;
  private final long[] totient;

  public TotientSieve(long max) {
    this(0, max);
  }

  public TotientSieve(long min, long max) {
    this.min = min;
    this.max = max;
    int size = Ints.checkedCast(max - min + 1);
    totient = new long[size];
    long[] product = new long[size];
    for (long n = min; n <= max; n++) {
      int idx = Ints.checkedCast(n - min);
      totient[idx] = 1;
      product[idx] = 1;
    }
    int maxSqrt = Ints.checkedCast(LongMath.sqrt(max, RoundingMode.FLOOR));
    boolean[] nonPrimes = new boolean[maxSqrt + 1];
    for (int prime = 2; prime <= maxSqrt; prime++) {
      if (!nonPrimes[prime]) {
        for (int j = prime; j <= maxSqrt; j += prime) {
          nonPrimes[j] = true;
        }
        long primePower = 1;
        while (primePower <= max / prime) {
          primePower *= prime;
          for (long n = ((min - 1) / primePower + 1) * primePower; n <= max; n += primePower) {
            int idx = Ints.checkedCast(n - min);
            totient[idx] *= primePower == prime ? prime - 1 : prime;
            product[idx] *= prime;
          }
        }
      }
    }
    for (long n = min; n <= max; n++) {
      int idx = Ints.checkedCast(n - min);
      if (product[idx] < n) {
        totient[idx] *= (n / product[idx]) - 1;
      }
    }
  }

  public long min() {
    return min;
  }

  public long max() {
    return max;
  }

  public long totient(long n) {
    checkArgument(n >= min && n <= max, "Illegal n: %s", n);
    return totient[Ints.checkedCast(n - min)];
  }
}