package euler;

public class Problem430 {

  public static void main(String[] args) {
    System.out.println(new Problem430().solve(10000000000L, 4000));
  }

  public String solve(long n, int k) {
    double sum = 0D;
    for (long i = 0L; i < n / 2; i++) {
      double q = 1D * (n - i - 1) / n * (n - i - 1) / n + 1D * i / n * i / n;
      double p = 1D - q;
      double exp = Math.pow(p - q, k);
      sum += exp;
      if ((n / 2 - i - 1) * exp < 1e-10) break;
    }
    return String.format("%.2f", n / 2 + sum);
  }
}
