package euler;


public class Problem190 {

  public static void main(String[] args) {
    System.out.println(new Problem190().solve(15));
  }

  public long solve(int n) {
    long sum = 0L;
    for (int m = 2; m <= n; m++) {
      double prod = 1D;
      for (int j = 1; j <= m; j++) {
        prod *= Math.pow(2D * m * j / (m * (m + 1)), j);
      }
      sum += Math.floor(prod);
    }
    return sum;
  }
}
