package euler;

import com.google.auto.value.AutoValue;
import com.google.common.base.Strings;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.math.LongMath.checkedAdd;
import static com.google.common.math.LongMath.checkedMultiply;
import static java.math.RoundingMode.UNNECESSARY;

public class Problem384 {
  public static void main(String[] args) {
    Runner.run(new Problem384()::solve);
  }

  public long solve() {
    int max = 1000;
    Pos[] pos = new Pos[max + 1];
    int[] currentK = new int[max + 1];
    for (int i = 0; i <= max; i++) {
      if (LongMath.isPowerOfTwo(i) && LongMath.log2(i, UNNECESSARY) % 2 == 0) {
        System.out.println();
      }
      int s = Ints.checkedCast(s(i));
      currentK[s]++;
      pos[i] = Pos.create(s, currentK[s]);
      System.out.println(Strings.padStart(String.valueOf(i), 3, '0') + " " + Strings.padStart(Long.toBinaryString(i), 8, '0') + ": " + s + " " + pos[i] + " -> " + pos[i / 4]);
      checkState(g(s, currentK[s], new HashMap<>())[currentK[s]] == i);
    }
    int f1 = 1;
    int f2 = 1;
    long res = 0;
    for (int i = 2; i <= 45; i++) {
      int prevf2 = f2;
      f2 = f1 + f2;
      f1 = prevf2;
      res += g(f2, f1, new HashMap<>())[f1];
      System.out.println(i);
    }
    return res;
  }

  @AutoValue
  static abstract class Pos {
    abstract long number();
    abstract long k();

    static Pos create(long number, long k) {
      return new AutoValue_Problem384_Pos(number, k);
    }

    @Override
    public String toString() {
      return "[" + number() + ":" + k() + "]";
    }
  }

  private int a(long n) {
    int hi = Long.numberOfLeadingZeros(n);
    int count = 0;
    for (int i = 0; i < 63 - hi; i++) {
      if ((n & (3L << i)) == (3L << i)) {
        count++;
      }
    }
    return count;
  }

  private int b(long n) {
    return a(n) % 2 == 0 ? 1 : -1;
  }

  private long s(long n) {
    return n == -1 ? 0 : s(n - 1) + b(n);
  }

  private long[] g(int n, int maxk, Map<Integer, long[]> cache) {
    checkArgument(n >= 1);
    checkArgument(maxk <= n);
    if (cache.containsKey(n)) {
      return cache.get(n);
    }
    long[] res = new long[maxk + 1];
    if (n == 1) {
      res[1] = 0;
    } else if (n % 2 == 0) {
      long[] g = g(n / 2, Math.min(n / 2, maxk), cache);
      for (int k = 1; k <= maxk; k++) {
        if (k % 2 == 0) {
          res[k] = checkedAdd(checkedMultiply(4, g[k / 2]), 3);
        } else {
          res[k] = checkedAdd(checkedMultiply(4, g[(k + 1) / 2]), 1);
        }
      }
    } else {
      int n1 = (n - 1) / 2;
      int n2 = (n + 1) / 2;
      int nextk1 = 1;
      int nextk2 = 1;
      int i = 0;
      long[] g1 = g(n1, Math.min(n1, maxk + 10), cache);
      long[] g2 = g(n2, Math.min(n2, maxk + 10), cache);
      while (i < maxk) {
        int nn;
        long g;
        if (nextk1 <= n1 && (nextk2 > n2 || g1[nextk1] < g2[nextk2])) {
          nn = n1;
          g = g1[nextk1++];
        } else {
          checkState(nextk2 <= n2, "n=%s,n1=%s,n2=%s,nextk1=%s,nextk2=%s", n, n1, n2, nextk1, nextk2);
          nn = n2;
          g = g2[nextk2++];
        }
        int b = b(g);
        if (i < maxk && 2 * nn - b == n) {
          res[++i] = checkedMultiply(4, g);
        }
        if (i < maxk && 2 * nn + (g % 2 == 0 ? 1 : -1) * b == n) {
          res[++i] = checkedAdd(checkedMultiply(4, g), 2);
        }
      }
    }
    cache.put(n, res);
    return res;
  }
}
