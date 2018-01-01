package euler;

import java.math.BigInteger;
import java.util.Arrays;

public class Problem196 {

  public static void main(String[] args) {
    System.out.println(new Problem196().solve(5678027) + new Problem196().solve(7208785));
  }

  public long solve(int n) {
    boolean[] row1 = row(n - 2);
    boolean[] row2 = row(n - 1);
    boolean[] row3 = row(n);
    boolean[] row4 = row(n + 1);
    boolean[] row5 = row(n + 2);
    long sum = 0L;
    long start = ((long) n) * (n - 1) / 2 + 1;
    for (int i = 0; i < n; i++) {
      if (row3[i]) {
        if (isTripletCenter(row1, row2, row3, i - 1)
              || isTripletCenter(row1, row2, row3, i)
              || isTripletCenter(row1, row2, row3, i + 1)
              || isTripletCenter(row2, row3, row4, i - 1)
              || isTripletCenter(row2, row3, row4, i)
              || isTripletCenter(row2, row3, row4, i + 1)
              || isTripletCenter(row3, row4, row5, i - 1)
              || isTripletCenter(row3, row4, row5, i)
              || isTripletCenter(row3, row4, row5, i + 1)) {
          sum += start + i;
        }
      }
    }
    return sum;
  }

  private boolean isTripletCenter(boolean[] prev, boolean[] cur, boolean[] next, int i) {
    return cnt(cur, i) == 1
        && cnt(prev, i - 1) + cnt(prev, i) + cnt(prev, i + 1)
            + cnt(cur, i - 1) + cnt(cur, i) + cnt(cur, i + 1)
            + cnt(next, i - 1) + cnt(next, i) + cnt(next, i + 1) >= 3;
  }

  private int cnt(boolean[] row, int pos) {
    if (pos >= 0 && pos < row.length && row[pos]) {
      return 1;
    } else {
      return 0;
    }
  }

  private boolean[] row(int n) {
    boolean[] res = new boolean[n];
    Arrays.fill(res, true);
    long start = ((long) n) * (n - 1) / 2 + 1;
    for (int d = 2; d < n; d++) {
      int k = (int) ((((start - 1) / d) + 1) * d - start);
      while (k < n) {
        res[k] = false;
        k += d;
      }
    }
    for (int i = 0; i < n; i++) {
      if (res[i]) {
        if (!BigInteger.valueOf(i + start).isProbablePrime(20)) {
          res[i] = false;
        }
      }
    }
    return res;
  }
}
