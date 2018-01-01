package euler;

import java.util.HashMap;
import java.util.Map;

import com.google.auto.value.AutoValue;

public class Problem383 {

  public static void main(String[] args) {
    System.out.println(new Problem383().solve());
  }

  public long solve() {
    return count(1_000_000_000_000_000_000L, 0);
  }

  @AutoValue
  static abstract class Key {
    abstract long n();
    abstract int sum();
    static Key create(long n, int sum) {
      return new AutoValue_Problem383_Key(n, sum);
    }
  }

  private final Map<Key, Long> cache = new HashMap<>();

  private long count(long n, int sum) {
    if (n == 0) {
      return 0;
    } else if (n == 1) {
      return sum < 0 ? 1L : 0L;
    } else {
      Key key = Key.create(n, sum);
      Long result = cache.get(key);
      if (result != null) {
        return result;
      }
      result = 0L;
      long p5 = 1L;
      while (p5 * 5 < n) {
        p5 *= 5;
      }
      result += count(p5, sum);
      if (n < 2 * p5) {
        result += count(n % p5, sum);
      } else {
        result += count(p5, sum);
        if (n <= (5 * p5 - 1) / 2) {
          result += count(n % p5, sum);
        } else {
          result += count((p5 - 1) / 2, sum);
          if (n < 3 * p5) {
            result += count(n % p5, sum + 1) - count((p5 - 1) / 2, sum + 1);
          } else {
            result += count(p5, sum + 1) - count((p5 - 1) / 2, sum + 1);
            if (n < 4 * p5) {
              result += count(n % p5, sum + 1);
            } else {
              result += count(p5, sum + 1);
              if (n < 5 * p5) {
                result += count(n % p5, sum + 1);
              } else {
                result += count(p5 - 1, sum + 1);
                result += count(p5, sum - 1) - count(p5 - 1, sum - 1);
              }
            }
          }
        }
      }
      cache.put(key, result);
      return result;
    }
  }
}
