package euler;

import java.util.HashMap;
import java.util.Map;

import com.google.auto.value.AutoValue;

public class Problem534 {

  public static void main(String[] args) {
    System.out.println(new Problem534().solve());
  }

  public long solve() {
    int n = 14;
    long sum = 0;
    for (int w = 0; w < n; w++) {
      sum += solve(n, w, new int[n]);
      cache.clear();
    }
    return sum;
  }

  @AutoValue
  abstract static class Key {
    abstract int w();
    @SuppressWarnings("mutable")
    abstract int[] captured();

    static Key create(int w, int[] captured) {
      return new AutoValue_Problem534_Key(w, captured);
    }
  }

  private Map<Key, Long> cache = new HashMap<>();

  private long solve(int n, int w, int[] captured) {
    if (captured.length == 0) {
      return 1;
    }
    Key key = Key.create(w, captured);
    Long count = cache.get(key);
    if (count != null) {
      return count;
    }
    count = 0L;
    for (int i = 0; i < n; i++) {
      if ((captured[0] & (1 << i)) == 0) {
        int[] newCaptured = new int[captured.length - 1];
        for (int j = 0; j < newCaptured.length; j++) {
          newCaptured[j] = captured[j + 1];
        }
        for (int j = 0; j < newCaptured.length && j < n - w - 1; j++) {
          int diag = j + 1;
          newCaptured[j] |= (1 << i);
          if (i - diag >= 0) {
            newCaptured[j] |= (1 << (i - diag));
          }
          if (i + diag < n) {
            newCaptured[j] |= (1 << (i + diag));
          }
        }
        count += solve(n, w, newCaptured);
      }
    }
    if (w >= 6) {
      cache.put(key, count);
    }
    return count;
  }
}
