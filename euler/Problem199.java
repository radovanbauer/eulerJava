package euler;

public class Problem199 {

  public static void main(String[] args) {
    System.out.println(new Problem199().solve(10));
  }

  public String solve(int n) {
    double r1 = 1D;
    double r2 = 2*Math.sqrt(3) - 3;
    double empty = 1D - 3*r2*r2 - 3*outer(r1, r2, r2, n) - inner(r2, r2, r2, n);
    return String.format("%.8f", empty);
  }

  private double inner(double r1, double r2, double r3, int n) {
    if (n == 0) return 0D;
    double r = r1*r2*r3*(r1*r2 + r1*r3 + r2*r3 - 2*Math.sqrt(r1*r2*r3*(r1 + r2 + r3)))
        / (r1*r1*r2*r2 + r1*r1*r3*r3 + r2*r2*r3*r3 - 2*r1*r2*r3*(r1 + r2 + r3));
    return r*r + inner(r1, r2, r, n - 1) + inner(r1, r, r3, n - 1) + inner(r, r2, r3, n - 1);
  }

  private double outer(double r1, double r2, double r3, int n) {
    if (n == 0) return 0D;
    double r = r1*r2*r3*(r1*r2 + r1*r3 - r2*r3 - 2*Math.sqrt(r1*r2*r3*(r1 - r2 - r3)))
        / (r1*r1*r2*r2 + r1*r1*r3*r3 + r2*r2*r3*r3 - 2*r1*r2*r3*(r1 - r2 - r3));
    return r*r + outer(r1, r2, r, n - 1) + outer(r1, r, r3, n - 1) + inner(r, r2, r3, n - 1);
  }
}
