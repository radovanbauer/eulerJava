package euler;

import java.math.BigInteger;
import java.util.TreeSet;

import com.google.common.collect.Sets;

public class Problem221 {

  public static void main(String[] args) {
    System.out.println(new Problem221().solve(150000));
  }

  public long solve(int n) {
    TreeSet<Long> numbers = Sets.newTreeSet();
    for (long p = 1;; p++) {
      if (numbers.size() == n) {
        if (numbers.last() < p * p * p) {
          return numbers.last();
        }
      }
      long x = p * p + 1;
      for (long d = 1; d * d <= x; d++) {
        if (x % d == 0) {
          BigInteger number =
              BigInteger.valueOf(p * (p + d)).multiply(BigInteger.valueOf(p + x / d));
          if (number.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0) {
            if (numbers.size() == n) {
              if (numbers.last() > number.longValue()) {
                numbers.remove(numbers.last());
                numbers.add(number.longValue());
              }
            } else {
              numbers.add(number.longValue());
            }
          }
        }
      }
    }
  }
}
