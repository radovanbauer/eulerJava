package euler;

import com.google.common.math.LongMath;

public class Problem205v2 {

  public static void main(String[] args) {
    System.out.println(new Problem205v2().solve());
  }

  public String solve() {
    int[] pcount = count(4, 9);
    int[] ccount = count(6, 6);
    long win = 0L;
    for (int psum = 0; psum < pcount.length; psum++) {
      for (int csum = 0; csum < psum; csum++) {
        win += 1L * pcount[psum] * ccount[csum];
      }
    }
    long all = LongMath.pow(4, 9) * LongMath.pow(6, 6);
    return String.format("%.7f", 1D * win / all);
  }

  private int[] count(int sides, int dices) {
    int max = sides * dices;
    int[][] counts = new int[dices + 1][max + 1];
    counts[0][0] = 1;
    for (int dice = 1; dice <= dices; dice++) {
      for (int side = 1; side <= sides; side++) {
        for (int sum = 0; sum + side <= max; sum++) {
          counts[dice][sum + side] += counts[dice - 1][sum];
        }
      }
    }
    return counts[dices];
  }
}
