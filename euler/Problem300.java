package euler;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.util.stream.LongStream;

public class Problem300 {
  public static void main(String[] args) {
    Runner.run(new Problem300()::solve);
  }

  public String solve() {
    int n = 15;
    long proteinCount = 1L << n;
    long sum = LongStream.range(0, proteinCount).parallel().map(i -> {
      String protein = intToProtein(n, i);
      String reversed = new StringBuilder(protein).reverse().toString();
      if (protein.compareTo(reversed) > 0) {
        return 0;
      } else if (protein.compareTo(reversed) == 0) {
        return optimum(protein);
      } else {
        return 2 * optimum(protein);
      }
    }).sum();
    return BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(proteinCount)).toPlainString();
  }

  private String intToProtein(int n, long i) {
    return Strings.padStart(Long.toString(i, 2), n, '0').replace('0', 'H').replace('1', 'P');
  }

  private long optimum(String protein) {
    Preconditions.checkArgument(protein.length() >= 2);
    int l = protein.length();
    char[][] map = new char[2 * l + 1][2 * l + 1];
    map[l][l] = protein.charAt(0);
    return optimum(protein, map, 0, 1, l + 1, l);
  }

  private static final int[][] dirs = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

  private long optimum(String protein, char[][] map, int contacts, int idx, int x, int y) {
    if (map[x][y] != 0) {
      return 0;
    }
    map[x][y] = protein.charAt(idx);
    int newContacts = contacts;
    if (map[x][y] == 'H') {
      for (int[] dir : dirs) {
        newContacts += map[x + dir[0]][y + dir[1]] == 'H' ? 1 : 0;
      }
    }
    long res = 0;
    if (idx == protein.length() - 1) {
      res = newContacts;
    } else {
      for (int[] newDir : dirs) {
        res = Math.max(res, optimum(protein, map, newContacts, idx + 1, x + newDir[0], y + newDir[1]));
      }
    }
    map[x][y] = 0;
    return res;
  }
}
