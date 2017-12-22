package euler;

import java.util.HashMap;
import java.util.Map;

import com.google.auto.value.AutoValue;

public class Problem290 {

  public static void main(String[] args) {
    System.out.println(new Problem290().solve());
  }

  public long solve() {
    return solve(18, 0, 0);
  }

  @AutoValue
  static abstract class Key {
    abstract int digits();
    abstract long carry();
    abstract long digitSumDiff();

    static Key create(int digits, long carry, long digitSumDiff) {
      return new AutoValue_Problem290_Key(digits, carry, digitSumDiff);
    }
  }

  private final Map<Key, Long> cache = new HashMap<>();

  private long solve(int digits, long carry, long digitSumDiff) {
    Key key = Key.create(digits, carry, digitSumDiff);
    if (cache.containsKey(key)) {
      return cache.get(key);
    }
    long cnt = 0;
    if (digits == 0) {
      if (sumOfDigits(carry) == -digitSumDiff) {
        cnt = 1;
      } else {
        cnt = 0;
      }
    } else if (digits > 0) {
      for (int d = 0; d <= 9; d++) {
        cnt += solve(digits - 1,
            (carry + d * 137) / 10,
            digitSumDiff + (carry + d * 137) % 10 - d % 10);
      }
    }
    cache.put(key, cnt);
    return cnt;
  }

  private int sumOfDigits(long n) {
    int sum = 0;
    while (n > 0) {
      sum += n % 10;
      n /= 10;
    }
    return sum;
  }
}
