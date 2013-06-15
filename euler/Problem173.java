package euler;

import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

public class Problem173 {

  public static void main(String[] args) {
    System.out.println(new Problem173().solve(1000000));
  }

  public int solve(int n) {
    Set<Laminae> all = Sets.newHashSet();
    for (int i = 4; i <= n; i += 4) {
      for (int d = 2; d * d < i; d += 2) {
        if (i % d == 0) {
          int e = i / d;
          if (e % 2 == 0) {
            int a = (d + e) / 2;
            int b = (e - d) / 2;
            all.add(new Laminae(a, b));
          }
        }
      }
    }
    return all.size();
  }

  private static class Laminae {
    private final int a, b;

    public Laminae(int a, int b) {
      this.a = a;
      this.b = b;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(a, b);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Laminae) {
        Laminae that = (Laminae) obj;
        return this.a == that.a
            && this.b == that.b;
      }
      return false;
    }
  }
}
