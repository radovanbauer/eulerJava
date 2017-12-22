package euler;

import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.Map;

import com.google.common.math.LongMath;

public class Problem463 {

  public static void main(String[] args) {
    System.out.println(new Problem463().solve());
  }

  private final long MOD = 1_000_000_000;

  public long solve() {
    return S(LongMath.pow(3, 37)).n();
  }

  private final Map<Long, LongMod> fCache = new HashMap<>();

  private LongMod f(long n) {
    LongMod res = fCache.get(n);
    if (res != null) {
      return res;
    }
    if (n == 1) {
      res = LongMod.create(1, MOD);
    } else if (n == 3) {
      res = LongMod.create(3, MOD);
    } else if (n % 2 == 0) {
      res = f(n / 2);
    } else if (n % 4 == 1) {
      long m = n / 4;
      res = f(2 * m + 1).multiply(2).subtract(f(m));
    } else {
      checkState(n % 4 == 3);
      long m = n / 4;
      res = f(2 * m + 1).multiply(3).subtract(f(m).multiply(2));
    }
    fCache.put(n, res);
    return res;
  }

  private final Map<Long, LongMod> SCache = new HashMap<>();

  private LongMod S(long n) {
    LongMod res = SCache.get(n);
    if (res != null) {
      return res;
    }
    if (n == 0) {
      res = LongMod.zero(MOD);
    } else if (n % 8 != 0) {
      res = f(n).add(S(n - 1));
    } else {
      long m = n / 8;
      res = S(4*m).multiply(2).subtract(S(2*m)).subtract(S(2*m - 1).multiply(3))
          .add(S(m - 1).multiply(3)).add(T(4*m).multiply(5)).subtract(T(2*m).multiply(5));
    }
    SCache.put(n, res);
    return res;
  }

  private final Map<Long, LongMod> TCache = new HashMap<>();

  private LongMod T(long n) {
    LongMod res = TCache.get(n);
    if (res != null) {
      return res;
    }
    if (n == 0) {
      res = LongMod.zero(MOD);
    } else if (n % 8 != 0 && n % 2 == 0) {
      res = T(n - 1);
    } else if (n % 8 != 0 && n % 2 == 1) {
      res = f(n).add(T(n - 1));
    } else {
      long m = n / 8;
      res = T(4*m).multiply(6).subtract(T(2*m).multiply(5))
          .subtract(S(2*m - 1).multiply(3)).add(S(m - 1).multiply(3));
    }
    TCache.put(n, res);
    return res;
  }
}
