package euler;

import com.google.common.collect.ImmutableList;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkArgument;

public class Problem284 {
  public static void main(String[] args) {
    Runner.run(new Problem284()::solve);
  }

  public String solve() {
    long sum = 0;
    for (int n = 1; n <= 10000; n++) {
      for (BigInteger rem1 : ImmutableList.of(BigInteger.ZERO, BigInteger.ONE)) {
        for (BigInteger rem2 : ImmutableList.of(BigInteger.ZERO, BigInteger.ONE)) {
          BigMod res = chineseRem(new BigInteger[]{rem1, rem2}, new BigInteger[]{BigInteger.valueOf(2).pow(n), BigInteger.valueOf(7).pow(n)});
          String resString = res.n().toString(14);
          if (resString.length() == n) {
            long digitSum = resString.chars().mapToLong(c -> Long.valueOf("" + (char) c, 14)).sum();
            sum += digitSum;
          }
        }
      }
    }
    return Long.toString(sum, 14);
  }

  private BigMod chineseRem(BigInteger[] rems, BigInteger[] mods) {
    checkArgument(rems.length == mods.length && rems.length > 0);
    BigInteger N = BigInteger.ONE;
    for (BigInteger mod : mods) {
      N = N.multiply(mod);
    }
    BigMod res = BigMod.zero(N);
    for (int i = 0; i < rems.length; i++) {
      res = res.add(BigMod.create(rems[i], N).multiply(N.divide(mods[i]))
          .multiply(BigMod.create(N.divide(mods[i]), mods[i]).invert().n()));
    }
    return res;
  }

}
