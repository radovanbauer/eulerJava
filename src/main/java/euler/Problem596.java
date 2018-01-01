package euler;

import com.google.common.math.LongMath;

public class Problem596 {
  public static void main(String[] args) {
    Runner.run(new Problem596()::solve);
  }

  public long solve() {
    long n = LongMath.pow(10, 16);
    long mod = 1000000007;
    long inv2 = LongMod.create(2, mod).invert().n();

    LongMod sum = LongMod.zero(mod);

    for (long k = 1; k * k < n; k++) {
      if (k % 4 != 0) {
        sum = sum.add(n / k * k);
      }
    }

    for (long d = 1; d * d <= n; d++) {
      long mink = n / (d + 1) + 1;
      long maxk = n / d;
      long min4k = ((mink - 1) / 4 + 1) * 4;
      long max4k = maxk / 4 * 4;
      sum = sum.add(LongMod.create(mink + maxk, mod).multiply(maxk - mink + 1)
          .subtract(LongMod.create(min4k + max4k, mod).multiply((max4k - min4k) / 4 + 1))
          .multiply(inv2).multiply(d));
    }

    return sum.multiply(8).add(1).n();
  }
}
