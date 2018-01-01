package euler;

public class Problem575 {

  public static void main(String[] args) {
    Runner.run(new Problem575()::solve);
  }

  // 1:
  // a * 4 + b * 4 * (n - 2) + c * (n - 2) * (n - 2) = 1
  // a = a / 3 + b / 2
  // b = 3 * b / 4 + c / 5
  //
  // 2:
  // a * 4 + b * 4 * (n - 2) + c * (n - 2) * (n - 2) = 1
  // a = a / 2 + b / 3
  // b = b / 2 + b / 3 + c / 8

  public String solve() {
    int n = 1000;

    long sum1 = 0;
    long sum2 = 0;
    for (int i = 1; i <= n; i++) {
      int x = (i * i - 1) / n;
      int y = (i * i - 1) % n;
      boolean xEdge = x == 0 || x == n - 1;
      boolean yEdge = y == 0 || y == n - 1;
      if (xEdge && yEdge) {
        sum1 += 3;
        sum2 += 2;
      } else if (xEdge || yEdge) {
        sum1 += 4;
        sum2 += 3;
      } else {
        sum1 += 5;
        sum2 += 4;
      }
    }
    return String.format("%.12f", 0.5 * sum1 / (n * (5 * n - 4)) + 0.5 * sum2 / (4 * n * (n - 1)));
  }
}
