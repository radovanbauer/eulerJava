package euler;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem487 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem487().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long start = 2 * LongMath.pow(10, 9);
    long end = start + 2000;
    return LongStream.rangeClosed(start, end).parallel()
        .map(mod -> BigInteger.valueOf(mod).isProbablePrime(10)
            ? S(10000, 1_000_000_000_000L, mod).n() : 0)
        .sum();
  }

  private LongMod S(int k, long n, long mod) {
    return powerSum2(k, n, mod).multiply(n + 1).subtract(powerSum2(k + 1, n, mod));
  }

  private LongMod powerSum2(int k, long n, long mod) {
    long[] binomial = new long[1];
    binomial[0] = 0L;
    long[] bernoulli = new long[k + 1];
    bernoulli[0] = 1L;
    for (int i = 1; i <= k; i++) {
      binomial = nextBinomial(binomial, mod);
      bernoulli[i] = 0L;
      for (int j = 0; j < i; j++) {
        bernoulli[i] -=
            LongMod.create(binomial[j], mod).multiply(bernoulli[j]).divide(i - j + 1).n();
      }
    }
    binomial = nextBinomial(binomial, mod);
    LongMod res = LongMod.zero(mod);
    for (int i = 0; i <= k; i++) {
      res = res.add(LongMod.create(i % 2 == 0 ? 1 : -1, mod)
          .multiply(binomial[i])
          .multiply(bernoulli[i])
          .multiply(LongMod.create(n, mod).pow(k + 1 - i)));
    }
    return res.divide(k + 1);
  }

  private long[] nextBinomial(long[] binomial, long mod) {
    long[] newBinomial = new long[binomial.length + 1];
    newBinomial[0] = 1L;
    newBinomial[binomial.length] = 1L;
    for (int j = 1; j < binomial.length; j++) {
      newBinomial[j] = LongMath.checkedAdd(binomial[j - 1], binomial[j]) % mod;
    }
    return newBinomial;
  }
}
