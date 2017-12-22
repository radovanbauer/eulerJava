package euler;

import com.google.common.collect.ImmutableList;

public class Problem515 {

  public static void main(String[] args) {
    System.out.println(new Problem515().solve());
  }

  public long solve() {
    int a = 1_000_000_000;
    int b = 100_000;
    int k = 100_000;
    return ImmutableList.copyOf(PrimeSieve.create(a, a + b - 1)).stream()
        .mapToLong(p -> LongMod.create(k - 1, p).invert().n())
        .sum();
  }
}
