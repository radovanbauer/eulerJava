package euler;

public class Problem547 {

  public static void main(String[] args) {
    System.out.println(new Problem547().solve());
  }

  public String solve() {
    System.out.println(expectedDistance(0, 1, 0, 1, 0, 1, 0, 1, 200));
    return null;
  }

  private double expectedDistance(
      double x1min, double x1max, double y1min, double y1max,
      double x2min, double x2max, double y2min, double y2max,
      long div) {
    double sum = 0;
    for (int x1i = 0; x1i <= div; x1i++) {
      double x1 = x1min * x1i / div + x1max * (1 - 1D * x1i / div);
      for (int y1i = 0; y1i <= div; y1i++) {
        double y1 = y1min * y1i / div + y1max * (1 - 1D * y1i / div);
        for (int x2i = 0; x2i <= div; x2i++) {
          double x2 = x2min * x2i / div + x2max * (1 - 1D * x2i / div);
          for (int y2i = 0; y2i <= div; y2i++) {
            double y2 = y2min * y2i / div + y2max * (1 - 1D * y2i / div);
            double dist = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
            sum += dist;
//            System.out.println(x1 + " " + y1 + " " + x2 + " " + y2 + ": " + dist);
          }
        }
      }
    }
    return sum / ((div + 1) * (div + 1) * (div + 1) * (div + 1));
  }
}
