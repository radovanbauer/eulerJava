package euler;

public class Problem287 {

  public static void main(String[] args) {
    Runner.run(new Problem287()::solve);
  }

  // 2
  // .***
  // ****
  // .***
  // ..*.
  //
  // 3
  // ..*****.
  // .*******
  // .*******
  // ********
  // .*******
  // .*******
  // ..*****.
  // ....*...

  public long solve() {
    int n = 24;
    return encode(0, 0, 1L << n, 1L << n, n);
  }

  private long encode(long x1, long y1, long x2, long y2, int n) {
    long xm = (x1 + x2) / 2;
    long ym = (y1 + y2) / 2;
    long[][] points = new long[][] {{x1, y1}, {x1, y2 - 1}, {x2 - 1, y1}, {x2 - 1, y2 - 1}, {xm, ym}};
    if (points[0][0] == points[3][0] && points[0][1] == points[3][1]) {
      return 2;
    }
    int blacks = 0;
    for (long[] point : points) {
      blacks += isBlack(point[0], point[1], n) ? 1 : 0;
    }
    if (blacks == 0 || blacks == 5) {
      return 2;
    } else {
      return 1
          + encode(x1, ym, xm, y2, n)
          + encode(xm, ym, x2, y2, n)
          + encode(x1, y1, xm, ym, n)
          + encode(xm, y1, x2, ym, n);
    }
  }

  private boolean isBlack(long x, long y, int n) {
    long npow = 1L << (n - 1);
    return (x - npow) * (x - npow) + (y - npow) * (y - npow) <= npow * npow;
  }
}
