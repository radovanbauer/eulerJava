package euler;

import java.util.Arrays;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

public class Problem172 {

  public static void main(String[] args) {
    System.out.println(new Problem172().solve());
  }

  public long solve() {
    return solve(18, new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 })
        - solve(17, new int[] { 2, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
  }

  private final Map<CacheKey, Long> cache = Maps.newHashMap();

  private long solve(int digits, int[] digitCnt) {
    CacheKey cacheKey = new CacheKey(digits, digitCnt);
    if (cache.containsKey(cacheKey)) {
      return cache.get(cacheKey);
    }
    long result = 0;
    if (digits == 0) {
      result = 1L;
    } else {
      for (int d = 0; d <= 9; d++) {
        if (digitCnt[d] > 0) {
          int[] newDigitCnt = digitCnt.clone();
          newDigitCnt[d]--;
          result += solve(digits - 1, newDigitCnt);
        }
      }
    }
    cache.put(cacheKey, result);
    return result;
  }

  private static class CacheKey {
    private final int digits;
    private final int[] digitCnt;

    public CacheKey(int digits, int[] digitCnt) {
      this.digits = digits;
      this.digitCnt = digitCnt;
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(new Object[] { digits, digitCnt });
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof CacheKey) {
        CacheKey that = (CacheKey) obj;
        return this.digits == that.digits
            && Arrays.equals(this.digitCnt, that.digitCnt);
      }
      return false;
    }
  }
}
