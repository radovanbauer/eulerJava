package euler;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Problem556 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem556().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long n = LongMath.pow(10, 14);
    int nSqrt = Ints.checkedCast(LongMath.sqrt(n, RoundingMode.FLOOR));
    List<GI> giPrimes = giPrimes(nSqrt);
    return giCount(n) - count(n, giPrimes, 0, GI.ONE, 0);
  }

  private long count(
      long maxNorm2,
      List<GI> giPrimes,
      int nextGiPrime,
      GI product,
      int productCount) {
    long sum = 0L;
    for (int i = nextGiPrime; i < giPrimes.size(); i++) {
      GI prime = giPrimes.get(i);
      GI primeSq = prime.multiply(prime);
      if (product.norm2() > maxNorm2 / primeSq.norm2()) {
        break;
      }
      GI newProduct = product.multiply(primeSq);
      int newProductCount = productCount + 1;
      long maxFactorNorm2 = maxNorm2 / newProduct.norm2();
      sum += (newProductCount % 2 == 0 ? -1 : 1) * giCount(maxFactorNorm2);
      sum += count(maxNorm2, giPrimes, i + 1, newProduct, newProductCount);
    }
    return sum;
  }

  private List<GI> giPrimes(int maxNorm2) {
    int maxDim = IntMath.sqrt(maxNorm2, RoundingMode.FLOOR);
    List<GI> nonZeroProperInts = new ArrayList<>();
    for (int a = 1; a <= maxDim; a++) {
      for (int b = a == 1 ? 1 : 0; a * a + b * b <= maxNorm2; b++) {
        nonZeroProperInts.add(GI.create(a, b));
      }
    }
    Collections.sort(nonZeroProperInts);
    boolean[][] nonPrimes = new boolean[maxDim + 1][maxDim + 1];
    ImmutableList.Builder<GI> result = ImmutableList.builder();
    for (GI prime : nonZeroProperInts) {
      if (!nonPrimes[(int) prime.a()][(int) prime.b()]) {
        result.add(prime);
        long primeNorm2 = prime.norm2();
        for (GI i : nonZeroProperInts) {
          if (primeNorm2 * i.norm2() > maxNorm2) {
            break;
          }
          GI product = prime.multiply(i);
          for (GI multiple : new GI[] {
              product,
              product.multiply(GI.I),
              product.multiply(GI.MINUS_ONE),
              product.multiply(GI.MINUS_I)}) {
            if (multiple.isProper()) {
              nonPrimes[(int) multiple.a()][(int) multiple.b()] = true;
            }
          }
        }
      }
    }
    return result.build();
  }

  private final Map<Long, Long> giCountCache = new HashMap<Long, Long>();

  private long giCount(long maxNorm2) {
    Long res = giCountCache.get(maxNorm2);
    if (res != null) {
      return res;
    }
    long maxNorm2Sqrt = LongMath.sqrt(maxNorm2, RoundingMode.FLOOR);
    res = maxNorm2Sqrt;
    for (long a = 1; a <= maxNorm2Sqrt; a++) {
      res += LongMath.sqrt(maxNorm2 - a * a, RoundingMode.FLOOR);
    }
    giCountCache.put(maxNorm2, res);
    return res;
  }

  @AutoValue
  static abstract class GI implements Comparable<GI> {
    static final GI ZERO = create(0, 0);
    static final GI ONE = create(1, 0);
    static final GI I = create(0, 1);
    static final GI MINUS_ONE = create(-1, 0);
    static final GI MINUS_I = create(0, -1);

    abstract long a();
    abstract long b();

    static GI create(long a, long b) {
      return new AutoValue_Problem556_GI(a, b);
    }

    long norm2() {
      return LongMath.checkedAdd(
          LongMath.checkedMultiply(a(), a()),
          LongMath.checkedMultiply(b(), b()));
    }

    GI multiply(GI that) {
      return create(
          LongMath.checkedSubtract(
              LongMath.checkedMultiply(this.a(), that.a()),
              LongMath.checkedMultiply(this.b(), that.b())),
          LongMath.checkedAdd(
              LongMath.checkedMultiply(this.a(), that.b()),
              LongMath.checkedMultiply(this.b(), that.a())));
    }

    @Override
    public int compareTo(GI that) {
      return ComparisonChain.start()
          .compare(this.norm2(), that.norm2())
          .compare(this.a(), that.a())
          .compare(this.b(), that.b())
          .result();
    }

    boolean isProper() {
      return a() >= 1 && b() >= 0;
    }

    boolean isZero() {
      return this.equals(ZERO);
    }
  }
}
