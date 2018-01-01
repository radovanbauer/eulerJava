package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.math.LongMath;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

public class Problem528 {

  public static void main(String[] args) {
    System.out.println(new Problem528().solve());
  }

  public long solve() {
    LongMod res = LongMod.zero(MOD);
    for (int k = 10; k <= 15; k++) {
      res = res.add(S(LongMath.pow(10, k), k, k));
    }
    return res.n();
  }

  private static final long MOD =  1_000_000_007;

  private LongMod S(long n, int k, int b) {
    LongMod res = LongMod.zero(MOD);
    for (long mask = 0; mask < (1L << k); mask++) {
      long m = n;
      List<Integer> pow = new ArrayList<>();
      long bPow = 1;
      for (int i = 0; i < k; i++) {
        bPow *= b;
        if ((mask & (1L << i)) != 0) {
          m -= bPow;
        } else {
          pow.add(i);
        }
      }
      if (m >= 0) {
        res = res.add(s(m, Ints.toArray(pow), b));
      }
    }
    return res;
  }

  @AutoValue
  static abstract class SKey {
    abstract long n();
    abstract ImmutableList<Integer> pow();
    abstract int b();
    static SKey create(long n, int[] pow, int b) {
      return new AutoValue_Problem528_SKey(n, ImmutableList.copyOf(Ints.asList(pow)), b);
    }
  }

  private final Map<SKey, LongMod> sCache = new HashMap<>();

  private LongMod s(long n, int[] pow, int b) {
    checkArgument(n >= 0 && b > 0);
    SKey key = SKey.create(n, pow, b);
    LongMod res = sCache.get(key);
    if (res != null) {
      return res;
    }
    if (pow.length == 0) {
      res = LongMod.create(1, MOD);
    } else {
      res = LongMod.zero(MOD);
      for (int sum = 0; sum <= (b - 1) * pow.length && sum <= n; sum++) {
        int[] newPow = dec(pow);
        res = res.add(s((n - sum) / b, newPow, b).multiply(count(pow.length, b, sum)));
      }
    }
    sCache.put(key, res);
    return res;
  }

  private int[] dec(int[] pow) {
    int newSize = pow[0] == 0 ? pow.length - 1 : pow.length;
    int[] newPow = new int[newSize];
    int idx = 0;
    for (int p : pow) {
      if (p >= 1) {
        newPow[idx++] = p - 1;
      }
    }
    return newPow;
  }

  @AutoValue
  static abstract class CountKey {
    abstract int k();
    abstract int b();
    abstract int sum();
    static CountKey create(int k, int b, int sum) {
      return new AutoValue_Problem528_CountKey(k, b, sum);
    }
  }

  private final Map<CountKey, LongMod> countCache = new HashMap<>();

  private LongMod count(int k, int b, int sum) {
    CountKey key = CountKey.create(k, b, sum);
    LongMod res = countCache.get(key);
    if (res != null) {
      return res;
    }
    if (k == 0) {
      if (sum == 0) {
        res = LongMod.create(1, MOD);
      } else {
        res = LongMod.zero(MOD);
      }
    } else {
      res = LongMod.zero(MOD);
      for (int first = 0; first < b; first++) {
        res = res.add(count(k - 1, b, sum - first));
      }
    }
    countCache.put(key, res);
    return res;
  }
}
