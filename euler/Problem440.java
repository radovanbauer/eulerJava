package euler;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.IntMath;

public class Problem440 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem440().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int n = 2000;
    long mod = 987_898_789;
    LongMod[] TS = new LongMod[n + 1];
    Arrays.fill(TS, LongMod.zero(mod));
    for (int c = 1; c <= n; c++) {
      LongModMatrix2 mat = LongModMatrix2.create(new long[][] {{10, 1}, {1, 0}}, mod);
      for (int e = 1; e <= n; e++) {
        mat = mat.pow(c);
        TS[e] = TS[e].add(mat.multiply(LongModMatrix2.create(new long[][] {{1}, {0}}, mod)).element(0, 0));
      }
    }
    LongMod res = LongMod.zero(mod);
    for (int a = 1; a <= n; a++) {
      for (int b = 1; b <= n; b++) {
        int g = IntMath.gcd(a, b);
        if (a / g % 2 == 0 || b / g % 2 == 0) {
          res = res.add(n / 2); // 1 for all even c
          res = res.add((n + 1) / 2 * 10); // 10 for all odd c
        } else {
          res = res.add(TS[g]); // sum of T(c^g) for all c
        }
      }
    }
    return res.n();
  }
}
