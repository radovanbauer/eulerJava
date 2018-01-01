package euler;

public class Problem371 {

  public static void main(String[] args) {
    System.out.println(new Problem371().solve());
  }

  public String solve() {
    double[][] p = new double[2][2];
    p[0][0] = 1;
    double exp = 0;
    for (int n = 1;; n++) {
      double prevExp = exp;
      double[][] np = new double[n + 2][2];
      for (int reg = 0; reg <= n; reg++) {
        for (int fiveh = 0; fiveh <= 1; fiveh++) {
          // 0
          np[reg][fiveh] += p[reg][fiveh] * 1 / 1000;
          // 500
          if (fiveh > 0) {
            np[reg][fiveh] += p[reg][fiveh - 1] * 1 / 1000;
          }
          // others
          if (reg > 0) {
            np[reg][fiveh] += p[reg][fiveh] * reg / 1000
                + p[reg - 1][fiveh] * (998 - 2 * (reg - 1)) / 1000;
          }
          exp += (n + 1) * np[reg][fiveh] * (reg + fiveh) / 1000;
        }
      }
      if (Math.abs(exp - prevExp) < 1e-15) {
        break;
      }
      p = np;
    }
    return String.format("%.8f", exp);
  }
}
