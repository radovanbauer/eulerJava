package euler;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Problem176 {

  public static void main(String[] args) {
    System.out.println(new Problem176().solve(47547));
  }

  public long solve(int n) {
    for (long k = 96818198400000L;; k++) {
      System.out.println(k);
      int cnt = 0;
      for (long d : divisorsOfSquare(k)) {
        if (d * d < k * k) {
          long e = k * k / d;
          if ((d + e) % 2 == 0) {
            cnt++;
          }
        }
      }
      if (cnt == n) {
        return k;
      }
    }
  }

  private List<Long> divisorsOfSquare(long n) {
    Set<Long> res = Sets.newHashSet();
    List<Long> div = divisors(n);
    for (long d1 : div) {
      for (long d2 : div) {
        res.add(d1 * d2);
      }
    }
    return ImmutableList.copyOf(res);
  }

  private List<Long> divisors(long n) {
    List<Long> res = Lists.newArrayList();
    for (long d = 1; d * d <= n; d++) {
      res.add(d);
      if (d * d != n) {
        res.add(n / d);
      }
    }
    return res;
  }
}
