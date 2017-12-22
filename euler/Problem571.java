package euler;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.math.LongMath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

public class Problem571 {

  public void solve() {
    int n = 12;
    int found = 0;
    long sum = 0L;
    List<Integer> digits = IntStream.rangeClosed(0, n - 1).boxed().collect(toImmutableList());
    Collection<List<Integer>> permutations = Collections2.orderedPermutations(digits);
    for (List<Integer> permutation : permutations) {
      if (permutation.get(0) == 0) {
        continue;
      }
      long number = fromBase(n, permutation);
      if (isNSuperPandigital(number, n - 1)) {
        System.out.println(permutation);
        for (int b = 2; b < n; b++) {
          System.out.println(b + ": " + Long.toString(number, b));
        }
        found++;
        sum += number;
        if (found == 10) {
          System.out.println(sum);
          return;
        }
      }
    }
  }

  private long fromBase(int base, List<Integer> number) {
    long res = 0;
    for (int i = 0; i < number.size(); i++) {
      res = LongMath.checkedMultiply(res, base);
      res += number.get(i);
    }
    return res;
  }

  private List<Integer> toBase(long number, int base) {
    List<Integer> result = new ArrayList<>();
    while (number > 0) {
      result.add(LongMath.mod(number, base));
      number /= base;
    }
    return ImmutableList.copyOf(Lists.reverse(result));
  }

  private boolean isNSuperPandigital(long number, int n) {
    for (int b = n; b >= 2; b--) {
      if (!isPandigital(number, b)) {
        return false;
      }
    }
    return true;
  }

  private boolean isPandigital(long number, int b) {
    return toBase(number, b).stream().collect(toImmutableSet()).size() == b;
  }

  public static void main(String[] args) {
    new Problem571().solve();
  }
}
