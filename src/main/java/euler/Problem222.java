package euler;


public class Problem222 {

  public static void main(String[] args) {
    System.out.println(new Problem222().calcLength(
        50,
        new double[] { 50, 48, 46, 44, 42, 40, 38, 36, 34, 32, 30, 31, 33, 35, 37, 39, 41, 43, 45, 47, 49 }));
  }

  private int calcLength(double x, double[] radius) {
    double length = 0D;
    for (int i = 0; i < radius.length - 1; i++) {
      double r1 = radius[i];
      double r2 = radius[i + 1];
      length += 2D * Math.sqrt(x * (r1 + r2 - x));
    }
    length += radius[0] + radius[radius.length - 1];
    return (int) Math.round(length * 1000);
  }
}
