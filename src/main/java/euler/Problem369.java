package euler;

import com.google.common.math.LongMath;

import java.math.RoundingMode;

import static com.google.common.base.Preconditions.checkState;

public class Problem369 {
  public static void main(String[] args) {
    Runner.run(new Problem369()::solve);
  }

  public long solve() {
    long count = 0;
    for (int n = 4; n <= 13; n++) {
      count += count(n, 0, 0, 0, new int[13]);
    }
    return count;
  }

  private long count(int n, int color, int cards, int nextNumber, int[] chosen) {
    if (color == 4) {
      checkState(cards == n);
      if (hasBadugi(chosen)) {
        return totalCount(chosen);
      } else {
        return 0;
      }
    }
    long result = 0;
    // same color
    if (n - cards - 1 >= 3 - color) {
      for (int number = nextNumber; number < 13; number++) {
        if (number == 0 || chosen[number - 1] != chosen[number]) {
          int prevChosen = chosen[number];
          chosen[number] |= 1 << color;
          result += count(n, color, cards + 1, number + 1, chosen);
          chosen[number] = prevChosen;
        }
      }
    }
    // new color
    if (nextNumber != 0 && (color < 3 || cards == n)) {
      result += count(n, color + 1, cards, 0, chosen);
    }
    return result;
  }

  private boolean hasBadugi(int[] chosen) {
    return hasBadugi(chosen, 0, new boolean[13]);
  }

  private boolean hasBadugi(int[] chosen, int color, boolean[] found) {
    if (color == 4) {
      return true;
    }
    for (int number = 0; number < 13; number++) {
      if (!found[number] && (chosen[number] & (1 << color)) != 0) {
        found[number] = true;
        if (hasBadugi(chosen, color + 1, found)) {
          return true;
        }
        found[number] = false;
      }
    }
    return false;
  }

  private long totalCount(int[] chosen) {
    long count = 1;
    int group = 1;
    for (int number = 0; number < 13 && chosen[number] != 0; number++) {
      if (number != 0 && chosen[number] == chosen[number - 1]) {
        group++;
      } else {
        group = 1;
      }
      count = LongMath.checkedMultiply(count, 13 - number);
      count = LongMath.divide(count, group, RoundingMode.UNNECESSARY);
    }
    return count;
  }
}
