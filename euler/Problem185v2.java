package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.Iterator;

import com.google.common.base.Optional;
import com.google.common.collect.AbstractIterator;

public class Problem185v2 {

  public static void main(String[] args) {
    System.out.println(new Problem185v2().solve());
  }

  public long solve() {
    int n = 16;
    Constraint[] constraints = {
        new Constraint(n, 2321386104303845L, 0),
        new Constraint(n, 3847439647293047L, 1),
        new Constraint(n, 3174248439465858L, 1),
        new Constraint(n, 8157356344118483L, 1),
        new Constraint(n, 6375711915077050L, 1),
        new Constraint(n, 6913859173121360L, 1),
        new Constraint(n, 4895722652190306L, 1),
        new Constraint(n, 5616185650518293L, 2),
        new Constraint(n, 4513559094146117L, 2),
        new Constraint(n, 2615250744386899L, 2),
        new Constraint(n, 6442889055042768L, 2),
        new Constraint(n, 2326509471271448L, 2),
        new Constraint(n, 5251583379644322L, 2),
        new Constraint(n, 2659862637316867L, 2),
        new Constraint(n, 5855462940810587L, 3),
        new Constraint(n, 9742855507068353L, 3),
        new Constraint(n, 4296849643607543L, 3),
        new Constraint(n, 7890971548908067L, 3),
        new Constraint(n, 8690095851526254L, 3),
        new Constraint(n, 1748270476758276L, 3),
        new Constraint(n, 3041631117224635L, 3),
        new Constraint(n, 1841236454324589L, 3)};
    int[] states = new int[n];
    Arrays.fill(states, (1 << 10) - 1);
    return solve(n, 0, constraints, states).get();
  }

  private Optional<Long> solve(int n, int constraint, Constraint[] constraints, int[] states) {
    if (constraint == constraints.length) {
      long res = 0L;
      for (int i = n - 1; i >= 0; i--) {
        if (Integer.bitCount(states[i]) != 1) {
          return Optional.absent();
        }
        res = res * 10 + Integer.numberOfTrailingZeros(states[i]);
      }
      return Optional.of(res);
    } else {
      Constraint c = constraints[constraint];
      outer: for (int[] comb : combinations(n, c.correct)) {
        boolean[] combArr = new boolean[n];
        for (int i : comb) {
          combArr[i] = true;
        }
        for (int i = 0; i < n; i++) {
          if (combArr[i] && (states[i] & (1 << c.num[i])) == 0) {
            continue outer;
          } else if (!combArr[i] && (states[i] & ~(1 << c.num[i])) == 0) {
            continue outer;
          }
        }
        int[] newStates = states.clone();
        for (int i = 0; i < n; i++) {
          if (combArr[i]) {
            newStates[i] = 1 << c.num[i];
          } else {
            newStates[i] = states[i] & ~(1 << c.num[i]);
          }
        }
        Optional<Long> solution = solve(n, constraint + 1, constraints, newStates);
        if (solution.isPresent()) {
          return solution;
        }
      }
      return Optional.absent();
    }
  }

  private static class Constraint {
    private final int[] num;
    private final int correct;

    public Constraint(int n, long num, int corect) {
      this.correct = corect;
      int idx = 0;
      this.num = new int[n];
      while (num > 0) {
        this.num[idx++] = (int) (num % 10);
        num /= 10;
      }
    }
  }

  private Iterable<int[]> combinations(final int n, final int k) {
    checkArgument(n >= k && k >= 0, "Invalid input: %d, %d", n, k);
    return new Iterable<int[]>() {
      @Override
      public Iterator<int[]> iterator() {
        return new AbstractIterator<int[]>() {
          private int[] comb;

          @Override
          protected int[] computeNext() {
            if (comb == null) {
              comb = new int[k];
              for (int i = 0; i < k; i++) {
                comb[i] = i;
              }
              return comb.clone();
            } else {
              int i = k - 1;
              while (i >= 0 && comb[i] == n - k + i) {
                i--;
              }
              if (i == -1) {
                return endOfData();
              }
              comb[i]++;
              i++;
              while (i < k) {
                comb[i] = comb[i - 1] + 1;
                i++;
              }
              return comb.clone();
            }
          }
        };
      }
    };
  }
}
