package euler;

import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class Problem188 {

  public static void main(String[] args) {
    System.out.println(new Problem188().solve(1777, 1855, 100000000));
  }

  public int solve(long a, long b, int mod) {
    if (b == 0L) {
      return 1;
    } else {
      List<Integer> pow = Lists.newArrayList(1);
      int m = (int) (a % mod);
      while (m != 1L) {
        pow.add(m);
        m = (int) ((m * a) % mod);
      }
      if (pow.size() == 1) {
        return Iterables.getOnlyElement(pow);
      } else {
        return pow.get(solve(a, b - 1, pow.size()));
      }
    }
  }
}
