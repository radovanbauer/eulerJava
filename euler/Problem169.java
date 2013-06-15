package euler;

import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Problem169 {

  public static void main(String[] args) {
    System.out.println(new Problem169().solve(BigInteger.TEN.pow(25)));
  }

  public long solve(BigInteger n) {
    BigInteger pow = BigInteger.ONE;
    while (pow.compareTo(n.multiply(BigInteger.valueOf(4L))) <= 0) {
      pow2.add(pow);
      pow = pow.multiply(BigInteger.valueOf(2L));
    }
    Collections.reverse(pow2);
    long cnt = 0L;
    for (int firstPow = 0; firstPow < pow2.size(); firstPow++) {
      cnt += solveInternal(n, firstPow);
    }
    return cnt;
  }

  private List<BigInteger> pow2 = Lists.newArrayList();
  private Map<CacheKey, Long> cache = Maps.newHashMap();

  private long solveInternal(BigInteger n, int firstPow) {
    CacheKey cacheKey = new CacheKey(n, firstPow);
    if (cache.containsKey(cacheKey)) {
      return cache.get(cacheKey);
    }
    Long res = 0L;
    if (firstPow == pow2.size()) {
      if (n.equals(ZERO)) {
        res = 1L;
      } else {
        res = 0L;
      }
    } else if (pow2.get(firstPow).compareTo(n) <= 0
          && pow2.get(firstPow - 2).subtract(BigInteger.valueOf(2L)).compareTo(n) >= 0) {
      for (int nextPow = firstPow + 1; nextPow <= pow2.size(); nextPow++) {
        res += solveInternal(n.subtract(pow2.get(firstPow)), nextPow);
        BigInteger x = n.subtract(pow2.get(firstPow - 1));
        if (x.compareTo(ZERO) >= 0) {
          res += solveInternal(x, nextPow);
        }
      }
    }
    cache.put(cacheKey, res);
    return res;
  }

  private static class CacheKey {
    private final BigInteger n;
    private final int firstPow;

    public CacheKey(BigInteger n, int firstPow) {
      this.n = n;
      this.firstPow = firstPow;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(n, firstPow);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof CacheKey) {
        CacheKey that = (CacheKey) obj;
        return this.n.compareTo(that.n) == 0
            && this.firstPow == that.firstPow;
      }
      return false;
    }
  }
}
