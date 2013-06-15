package euler;

import java.util.List;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.math.IntMath;

public class Problem182 {

  public static void main(String[] args) {
//    System.out.println(new Problem182().solve(3, 5));
    System.out.println(new Problem182().solve(1009, 3643));
  }

  public long solve(int p, int q) {
    int n = p * q;
    int fi = (p - 1) * (q - 1);
    ImmutableSortedSet<Integer> fiDivs = divisors(fi);
    int[] cnt = new int[fi];
    for (int m = 0; m < n; m++) {
      if (m % 1000 == 0) {
        System.out.println(m);
      }
      int period = findPeriod(n, m, fiDivs);
      int e = 1 + period;
      while (e < fi) {
        cnt[e]++;
        e += period;
      }
    }
    int min = Integer.MAX_VALUE;
    for (int e = 2; e < fi; e++) {
      if (IntMath.gcd(e, fi) != 1) continue;
      System.out.println(String.format("%d: %d", e, cnt[e]));
      min = Math.min(min, cnt[e]);
    }
    System.out.println(min);
    long sum = 0L;
    for (int e = 2; e < fi; e++) {
      if (IntMath.gcd(e, fi) == 1 && cnt[e] == min) {
        sum += e;
      }
    }
    return sum;
  }

  private int findPeriod(int n, int m, ImmutableSortedSet<Integer> fiDivs) {
    if (IntMath.gcd(n, m) != 1) {
      int pow = 2;
      long x = (1L * m * m) % n;
      while (x != m) {
        x = (x * m) % n;
        pow++;
      }
      return pow - 1;
    } else {
      for (int fiDiv : fiDivs) {
        if (modPow(m, fiDiv, n) == 1) {
          return fiDiv;
        }
      }
      throw new AssertionError();
    }
  }

  private long modPow(long base, long exp, long mod) {
    long res = 1L;
    while (exp > 0L) {
      if ((exp & 1L) != 0L) {
        res = (res * base) % mod;
      }
      exp >>= 1;
      base = (base * base) % mod;
    }
    return res;
  }

  private ImmutableSortedSet<Integer> divisors(int n) {
    List<Integer> res = Lists.newArrayList();
    for (int d = 1; d * d <= n; d++) {
      res.add(d);
      if (d * d != n) {
        res.add(n / d);
      }
    }
    return ImmutableSortedSet.copyOf(res);
  }
}
