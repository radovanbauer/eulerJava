package euler;

import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Problem155 {

  public static void main(String[] args) {
    System.out.println(new Problem155().solve(18));
  }

  public long solve(int n) {
    List<ImmutableSet<LongFrac>> caps = Lists.newArrayList();
    caps.add(ImmutableSet.<LongFrac>of());
    caps.add(ImmutableSet.<LongFrac>of(LongFrac.ONE));
    for (int k = 2; k <= n; k++) {
      Set<LongFrac> newCaps = Sets.newHashSet(caps.get(k - 1));
      for (int a = 1; a <= k - a; a++) {
        Set<LongFrac> left = caps.get(a);
        Set<LongFrac> right = caps.get(k - a);
        for (LongFrac cap1 : left) {
          for (LongFrac cap2 : right) {
            newCaps.add(cap1.add(cap2));
            newCaps.add(LongFrac.ONE.divide(
                LongFrac.ONE.divide(cap1).add(LongFrac.ONE.divide(cap2))));
          }
        }
      }
      caps.add(k, ImmutableSet.copyOf(newCaps));
      System.out.printf("%d: %d\n", k, caps.get(k).size());
    }
    return caps.get(n).size();
  }

  private static class LongFrac {
    public static LongFrac ONE = new LongFrac(1L, 1L);

    private final long n, d;

    public LongFrac(long n, long d) {
      long g = gcd(n, d);
      this.n = n / g;
      this.d = d / g;
    }

    private static long gcd(long a, long b) {
      return b == 0 ? a : gcd(b, a % b);
    }

    public LongFrac add(LongFrac that) {
      return new LongFrac(this.n * that.d + that.n * this.d, this.d * that.d);
    }

    public LongFrac divide(LongFrac that) {
      return new LongFrac(this.n * that.d, this.d * that.n);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(n, d);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof LongFrac) {
        LongFrac that = (LongFrac) obj;
        return this.n == that.n && this.d == that.d;
      }
      return false;
    }

    @Override
    public String toString() {
      return String.format("%d/%d", n, d);
    }
  }
}
