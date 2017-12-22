package euler;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Problem321 {

  public static void main(String[] args) {
    System.out.println(new Problem321().solve());
  }

  public long solve() {
    List<Long> solutions1 = genSolutions(1, 1, 21);
    List<Long> solutions2 = genSolutions(5, 2, 21);
    return ImmutableList.copyOf(Iterables.concat(solutions1, solutions2))
        .stream().filter(n -> n > 0).sorted().limit(40).mapToLong(n -> n).sum();
  }

  private List<Long> genSolutions(long a, long b, int n) {
    List<Long> solutions = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      solutions.add(b - 1);
      long c = a * 3 + 8 * b * 1;
      long d = a * 1 + b * 3;
      a = c;
      b = d;
    }
    return solutions;
  }
}
