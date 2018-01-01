package euler;

public class Problem317 {

  public static void main(String[] args) {
    System.out.println(new Problem317().solve());
  }

  public double solve() {
    double v = 20;
    double g = 9.81;
    double h = 100;
    return Math.PI * Math.pow(2 * g * h * v + v * v * v, 2) / (4 * g * g * g);
  }
}
