package euler;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class Problem346 {

  public static void main(String[] args) {
    System.out.println(new Problem346().solve());
  }

  public long solve() {
    long max = 1_000_000_000_000L;
    Set<Long> strong = new HashSet<>();
    strong.add(1L);
    outer: for (int b = 2;; b++) {
      for (int k = 3;; k++) {
        BigInteger number =
            BigInteger.valueOf(b).pow(k).subtract(BigInteger.ONE).divide(BigInteger.valueOf(b - 1));
        if (number.compareTo(BigInteger.valueOf(max)) >= 0) {
          if (k == 3) {
            break outer;
          } else {
            continue outer;
          }
        }
        strong.add(number.longValueExact());
      }
    }
    return strong.stream().mapToLong(i -> i).sum();
  }
}
