package euler;

import com.google.auto.value.AutoValue;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

import java.util.HashMap;
import java.util.Map;

public class Problem294 {

  public static void main(String[] args) {
    Runner.run(new Problem294()::solve);
  }

  public long solve() {
    return count(LongMath.pow(11, 12), 23, 0).n();
  }

  private static final long MOD = 1_000_000_000;

  private final Map<Input, LongMod> cache = new HashMap<>();

  private LongMod count(long n, int d, int mod) {
    if (n == 0) {
      return d == 0 && mod == 0 ? LongMod.create(1, MOD) : LongMod.zero(MOD);
    }
    Input input = Input.create(n, d, mod);
    if (cache.containsKey(input)) {
      return cache.get(input);
    }
    LongMod sum = LongMod.zero(MOD);
    if (n % 2 == 1) {
      for (int digit = 0; digit <= 9 && digit <= d; digit++) {
        sum = sum.add(count(n - 1, d - digit, (mod - digit + 23) * 7 % 23));
      }
    } else {
      long halfN = n / 2;
      int pow = Ints.checkedCast(LongMod.create(7, 23).pow(halfN).n());
      for (int d1 = 0; d1 <= d; d1++) {
        for (int mod1 = 0; mod1 < 23; mod1++) {
          sum = sum.add(count(halfN, d1, mod1).multiply(count(halfN, d - d1, (mod - mod1) * pow % 23)));
        }
      }
    }
    cache.put(input, sum);
    return sum;
  }

  @AutoValue
  static abstract class Input {
    abstract long n();
    abstract int d();
    abstract int mod();

    static Input create(long n, int d, int mod) {
      return new AutoValue_Problem294_Input(n, d, mod);
    }
  }
}
