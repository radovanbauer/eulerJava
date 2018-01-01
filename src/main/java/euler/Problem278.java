package euler;

import static com.google.common.math.LongMath.divide;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.math.LongMath.checkedMultiply;
import static java.math.RoundingMode.FLOOR;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;

public class Problem278 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem278().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    PrimeSieve primeSieve = PrimeSieve.create(5000 - 1);
    return ImmutableList.copyOf(primeSieve).stream().parallel().mapToLong(p -> {
      long sum = 0L;
      for (long q : primeSieve) {
        for (long r : primeSieve) {
          if (p < q && q < r) {
            sum += f(p, q, r);
          }
        }
      }
      return sum;
    }).sum();
  }

  private long f(long p, long q, long r) {
    long a1 = LongMod.create(p * q, r).invert().n();
    long d1 = (1 - a1 * p * q) / r;
    long b1 = LongMod.create(q, p).invert().multiply(d1).n();
    long c1 = (d1 - b1 * q) / p;
    long n1 = chineseRem(
        new long[] {
            LongMod.create(-a1, r).invert().n(),
            LongMod.create(-b1, p).invert().n(),
            LongMod.create(-c1, q).invert().n()},
        new long[] { r, p, q });
    long n2 = p * q * r + n1;
    if (divide(a1*n2, r, FLOOR) + divide(b1*n2, p, FLOOR) + divide(c1*n2, q, FLOOR) < 0) {
      return n2;
    } else {
      return n1;
    }
  }

  private long chineseRem(long[] rems, long[] mods) {
    checkArgument(rems.length == mods.length && rems.length > 0);
    long N = 1L;
    for (long mod : mods) {
      N = checkedMultiply(N, mod);
    }
    LongMod res = LongMod.zero(N);
    for (int i = 0; i < rems.length; i++) {
      res = res.add(LongMod.create(rems[i], N).multiply(N / mods[i])
          .multiply(LongMod.create(N / mods[i], mods[i]).invert().n()));
    }
    return res.n();
  }
}
