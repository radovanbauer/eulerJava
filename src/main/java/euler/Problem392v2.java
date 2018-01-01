package euler;

public class Problem392v2 {

  public static void main(String[] args) {
    System.out.printf("%.10f\n", new Problem392v2().solve());
  }

  public double solve() {
    int n = 400 / 2;
    double high = 1;
    double low = 0;
    while (high - low > 1e-13) {
      double mid = (high + low) / 2;
      double[] x = findSolution(mid, n);
      if (x[n + 1] > 1 || Double.isNaN(x[n + 1])) {
        high = mid;
      } else {
        low = mid;
      }
    }
    double[] x = findSolution(low, n);
    return f(x) * 4;
  }

  private double f(double[] x) {
    double sum = 0;
    for (int i = 1; i < x.length; i++) {
      sum += (x[i] - x[i - 1]) * Math.sqrt(1 - x[i - 1] * x[i - 1]);
    }
    return sum;
  }

  private double[] findSolution(double x1, int n) {
    double[] x = new double[n + 2];
    x[0] = 0;
    x[1] = x1;
    for (int i = 1; i < n + 1; i++) {
      x[i + 1] = (-1 + 2 * x[i] * x[i] + Math.sqrt((1 - x[i] * x[i]) * (1 - x[i - 1] * x[i - 1]))) / x[i];
      if (x[i + 1] <= x[i]) {
        return null;
      }
    }
    return x;
  }
}
