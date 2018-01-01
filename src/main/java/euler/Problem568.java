package euler;

public class Problem568 {
  public static void main(String[] args) {
    Runner.run(new Problem568()::solve);
  }

  public long solve() {
    double res = 0;
    int n = 123456789;
    for (int i = 1; i <= n; i++) {
      res += 1D/i;
    }
    for (int i = 1; i <= n; i++) {
      res /= 2;
      while (res < 1e6) {
        res *= 10;
      }
    }
    System.out.println(res);
    return Math.round(res);
  }
}
