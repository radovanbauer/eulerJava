package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.math.LongMath.checkedMultiply;

import java.math.BigInteger;

import com.google.common.math.IntMath;
import com.google.common.math.LongMath;

public class Problem402 {

  public static void main(String[] args) {
    System.out.println(new Problem402().solve());
  }

  public long solve() {
    int[][][] s = new int[25][25][25];
    for (int a = 1; a <= 24; a++) {
      for (int b = 1; b <= 24; b++) {
        for (int c = 1; c <= 24; c++) {
          s[a][b][c] = s[a - 1][b][c];
          for (int d = 1; d <= b; d++) {
            for (int e = 1; e <= c; e++) {
              s[a][b][c] += m(a, d, e);
            }
          }
        }
      }
    }
    long mod2 = LongMath.pow(2, 9);
    int p2 = 1536;
    long[] sMod2p9 = new long[p2];
    BigInteger f0 = BigInteger.valueOf(0);
    BigInteger f1 = BigInteger.valueOf(1);
    for (int k = 2; k < 2 + p2; k++) {
      BigInteger f2 = f0.add(f1);
      f0 = f1;
      f1 = f2;
      LongMod x = LongMod.create(f2.subtract(BigInteger.ONE).divide(BigInteger.valueOf(24L)), mod2);
      int y = f2.subtract(BigInteger.ONE).mod(BigInteger.valueOf(24L)).add(BigInteger.ONE).intValueExact();
      sMod2p9[k - 2] = s(s, x, y).n();
    }
    long mod5 = LongMath.pow(5, 9);
    int p5 = 46_875_000;
    long[] sMod5p9 = new long[p5];
    LongMod f0m = LongMod.create(0, mod5 * 24);
    LongMod f1m = LongMod.create(1, mod5 * 24);
    for (int k = 2; k < 2 + p5; k++) {
      LongMod f2m = f0m.add(f1m);
      f0m = f1m;
      f1m = f2m;
      int mod24 = LongMath.mod(f2m.subtract(1).n(), 24);
      LongMod x = LongMod.create(f2m.n(), mod5).subtract(1).subtract(mod24).divide(24);
      int y = mod24 + 1;
      sMod5p9[k - 2] = s(s, x, y).n();
    }
    long n = 1_234_567_890_123L;
    long s2 = (n - 2) / p2 * sum(sMod2p9, 0, p2 - 1) + sum(sMod2p9, 0, LongMath.mod(n - 2, p2));
    long s5 = (n - 2) / p5 * sum(sMod5p9, 0, p5 - 1) + sum(sMod5p9, 0, LongMath.mod(n - 2, p5));
    return chineseRem(new long[] {s2, s5}, new long[] {mod2, mod5});
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

  private long sum(long[] arr, int from, int to) {
    long sum = 0;
    for (int i = from; i <= to; i++) {
      sum += arr[i];
    }
    return sum;
  }

  private LongMod s(int[][][] s, LongMod x, int y) {
    return x.multiply(x).multiply(x).multiply(s[24][24][24])
        .add(x.multiply(x).multiply(s[y][24][24] + s[24][y][24] + s[24][24][y]))
        .add(x.multiply(s[y][y][24] + s[y][24][y] + s[24][y][y]))
        .add(s[y][y][y]);
  }

  private int m(int a, int b, int c) {
    return gcd(24, 1 + a + b + c, 2 * (b + 1), 6 * (a + 2));
  }

  private int gcd(int... ls) {
    int gcd = Math.abs(ls[0]);
    for (int i = 1; i < ls.length; i++) {
      gcd = IntMath.gcd(gcd, Math.abs(ls[i]));
    }
    return gcd;
  }
}
