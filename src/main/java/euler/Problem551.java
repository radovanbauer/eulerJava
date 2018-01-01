package euler;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class Problem551 {

  public static void main(String[] args) {
    System.out.println(new Problem551().solve());
  }

  public long solve() {
    long max = 1000;
    Set<Long> trunk = new LinkedHashSet<>(getBranch(1L, max, ImmutableSet.<Long>of()));
    Set<Long> remaining = new LinkedHashSet<>();
    for (long n = 1; n <= max; n++) {
      if (n % 3 != 0 && !trunk.contains(n)) {
        remaining.add(n);
      }
    }
    System.out.println(trunk.size() + " " + trunk);
    while (!remaining.isEmpty()) {
      List<Long> branch = getBranch(remaining.iterator().next(), max, trunk);
      for (long x : branch) {
        remaining.remove(x);
      }
      System.out.println(branch);
    }
    return 0;
  }

  private List<Long> getBranch(long start, long max, Set<Long> trunk) {
    long x = start;
    List<Long> result = new ArrayList<>();
    while (x <= max && !trunk.contains(x)) {
      result.add(x);
      x = next(x);
    }
    if (x <= max) {
      result.add(x);
    }
    return result;
  }

  private long next(long n) {
    long res = n;
    while (n > 0) {
      res += n % 10;
      n /= 10;
    }
    return res;
  }
}
