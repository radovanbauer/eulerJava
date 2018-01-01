package euler;

import com.google.auto.value.AutoValue;
import com.google.common.math.BigIntegerMath;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class Problem582 {

  public static void main(String[] args) {
    Runner.run(new Problem582()::solve);
  }

  // c^2 = a^2 + b^2 + a*b = (a + b)^2 - a*b
  // b - a = k <= 100
  // c^2 = (b - a)^2 + 3*a*b
  // c^2 - 3*a*(a + k) = k^2
  // c^2 - 3*((a + k/2)^2 - k^2/4) = k^2
  // c^2 - 3*(a+k/2)^2 = k^2/4
  // (2*c)^2 - 3*(2*a + k)^2 = k^2

  private static final BigInteger TWO = BigInteger.valueOf(2);

  public long solve() {
    BigInteger max = BigInteger.valueOf(10).pow(100);

    long count = 0;

    for (int k = 1; k <= 100; k++) {
      Set<Solution> solutions = new HashSet<>();
      BigInteger kMod2 = BigInteger.valueOf(k % 2);
      for (BigInteger a = ONE; a.compareTo(BigInteger.valueOf(2_000)) <= 0; a = a.add(ONE)) {
        BigInteger y = a.multiply(TWO).add(BigInteger.valueOf(k));
        BigInteger x2 = y.multiply(y).multiply(BigInteger.valueOf(3)).add(BigInteger.valueOf(k * k));
        BigInteger x = BigIntegerMath.sqrt(x2, RoundingMode.FLOOR);
        if (x.multiply(x).compareTo(x2) == 0) {
          BigInteger c = x.divide(TWO);

          if (!solutions.contains(Solution.create(c, a))) {
            System.out.printf("new solution %d: %d %d r=%d c=%d\n", k, c, a, x, y);
            List<Solution> possibleSolutions = generateSolutions(Solution.create(x, y), max.multiply(TWO));
            for (Solution s : possibleSolutions) {
              if (s.x().mod(TWO).compareTo(ZERO) == 0
                  && s.y().mod(TWO).compareTo(kMod2) == 0) {
                BigInteger newC = s.x().divide(TWO);
                BigInteger newA = s.y().subtract(BigInteger.valueOf(k)).divide(TWO);
                checkState(newA.compareTo(newC) < 0 && newC.compareTo(max) <= 0);
                solutions.add(Solution.create(newC, newA));
                count++;
              }
            }
          }
        }
      }
    }

    return count;
  }

  private List<Solution> generateSolutions(Solution solution, BigInteger maxX) {
    List<Solution> result = new ArrayList<>();
    Solution s = solution;
    while (s.x().compareTo(maxX) <= 0) {
      result.add(s);
      s = Solution.create(
          s.x().multiply(BigInteger.valueOf(2)).add(s.y().multiply(BigInteger.valueOf(3))),
          s.x().add(s.y().multiply(BigInteger.valueOf(2))));
    }
    return result;
  }

  @AutoValue
  abstract static class Solution {
    abstract BigInteger x();
    abstract BigInteger y();

    static Solution create(BigInteger x, BigInteger y) {
      return new AutoValue_Problem582_Solution(x, y);
    }
  }
}
