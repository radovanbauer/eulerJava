package euler;

public class Problem523 {

  public static void main(String[] args) {
    System.out.println(new Problem523().solve());
  }

  public String solve() {
    int n = 30;
    double sum = 0D;
    for (int i = 1; i <= n; i++) {
      sum += 1D * ((1L << (i - 1)) - 1) / i;
    }
    return String.format("%.2f", sum);
  }
}
