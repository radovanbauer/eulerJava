package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.auto.value.AutoValue;
import com.google.common.math.LongMath;

public class Problem396 {

  public static void main(String[] args) {
    System.out.println(new Problem396().solve());
  }

  public long solve() {
    LongMod sum = LongMod.zero(1_000_000_000);
    for (long g = 1; g < 16; g++) {
      sum = sum.add(solve(g));
    }
    return sum.n();
  }

  private long solve(long g) {
    long k = 2;
    while (g > -1) {
      List<Long> number = toBase(g, k);
      if (number.size() == 3 && number.get(0).equals(number.get(1)) && number.get(1).equals(number.get(2))) {
        return LongMod.create(calc(number.get(0) + 1, number.get(0) + 1, 9), 1_000_000_000)
            .multiply(LongMath.pow(2, 9))
            .multiply(LongMod.create(LongMath.pow(2, 9), LongMath.pow(5, 9)).invert().n())
            .subtract(3).n();
      }
      g = fromBase(number, k + 1) - 1;
      k++;
    }
    return (k - 3) % 1_000_000_000;
  }

  @AutoValue
  static abstract class Key {
    abstract long k();
    abstract long l();
    abstract int mod5();

    static Key create(long k, long l, int mod5) {
      return new AutoValue_Problem396_Key(k, l, mod5);
    }
  }

  private final Map<Key, Long> cache = new HashMap<>();

  private long calc(long k, long l, int mod5) {
    checkArgument(k >= 2);
    if (mod5 == 0) {
      return 0;
    }
    long mod = LongMath.pow(5, mod5);
    if (l == 0) {
      return k % mod;
    } else if (l == 1) {
      return LongMod.create(2, mod).pow(k).multiply(k).n();
    } else {
      Key key = Key.create(k, l, mod5);
      if (cache.containsKey(key)) {
        return cache.get(key);
      }
      long x = calc(k, l - 1, mod5);
      long x5 = calc(k, l - 1, mod5 - 1);
      long exp = mod5 > 1
          ? LongMod.create(x5, LongMath.pow(5, mod5 - 1) * 4)
              .multiply(4).multiply(LongMod.create(4, LongMath.pow(5, mod5 - 1)).invert().n()).n()
          : 0;
      long result = LongMod.create(2, mod).pow(exp).multiply(x).n();
      cache.put(key, result);
      return result;
    }
  }

  private List<Long> toBase(long n, long radix) {
    List<Long> result = new ArrayList<>();
    while (n > 0) {
      result.add(n % radix);
      n /= radix;
    }
    return result;
  }

  public long fromBase(List<Long> number, long radix) {
    checkArgument(radix > 1);
    long result = 0;
    for (int i = number.size() - 1; i >= 0; i--) {
      result = LongMath.checkedMultiply(result, radix);
      result = LongMath.checkedAdd(result, number.get(i));
    }
    return result;
  }
}
