package euler;

import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Problem350 {
  public static void main(String[] args) {
    Runner.run(new Problem350()::solve);
  }

  public long solve() {
    long G = LongMath.pow(10, 6);
    long L = LongMath.pow(10, 12);
    long N = LongMath.pow(10, 18);
    long mod = LongMath.pow(101, 4);
    int maxProduct = Ints.checkedCast(L / G);
    FactorizationSieve sieve = new FactorizationSieve(maxProduct);

    LongMod[] lcmCount = new LongMod[maxProduct + 1];
    LongMod[] lcmCountSum = new LongMod[maxProduct + 1];
    lcmCount[0] = LongMod.zero(mod);
    lcmCountSum[0] = LongMod.zero(mod);
    for (int lcm = 1; lcm <= maxProduct; lcm++) {
      lcmCount[lcm] = LongMod.create(sieve.divisorCount(lcm), mod).pow(N);
      for (int d : sieve.divisors(lcm)) {
        if (d < lcm) {
          lcmCount[lcm] = lcmCount[lcm].subtract(lcmCount[d].multiply(sieve.divisorCount(lcm / d)));
        }
      }
      lcmCountSum[lcm] = lcmCountSum[lcm - 1].add(lcmCount[lcm]);
    }

    LongMod result = LongMod.zero(mod);
    for (long gcd = G; gcd * gcd <= L; gcd++) {
      result = result.add(lcmCountSum[Ints.checkedCast(L / gcd)]);
    }
    for (long ratio = 1; ratio * ratio < L; ratio++) {
      // L / gcd >= ratio => L / ratio >= gcd
      // L / gcd < ratio + 1 => L / (ratio + 1) < gcd
      long gcdCount = L / ratio - L / (ratio + 1);
      result = result.add(lcmCountSum[Ints.checkedCast(ratio)].multiply(gcdCount));
    }
    return result.n();
  }
}
