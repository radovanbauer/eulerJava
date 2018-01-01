package euler;

import com.google.common.base.Preconditions;

public class Problem327 {

  public static void main(String[] args) {
    Runner.run(new Problem327()::solve);
  }

  // M(3, 1) = 2
  // M(3, 2) = 3
  // M(3, 3) = 6
  // M(3, 4) = 15
  // M(3, 5) = 42
  // M(3, 6) = 123
  // M(3, 7) = 366
  // M(3, 8) = 1095
  // M(3, n) = (3^(n - 1) + 3) / 2
  //
  // M(4, 1) = 2
  // M(4, 2) = 3
  // M(4, 3) = 4
  // M(4, 4) = 7
  // M(4, 5) = 12
  // M(4, 6) = 23
  // M(4, 7) = 44
  // M(4, 8) = 87
  // M(4, 9) = 172
  // M(4, 10) = 343
  // M(4, n) = floor( (2^(n + 1) + 10)/6 )
  //
  // M(5, 1) = 2
  // M(5, 2) = 3
  // M(5, 3) = 4
  // M(5, 4) = 5
  // M(5, 5) = 8
  // M(5, 6) = 13
  // M(5, 7) = 20
  // M(5, 8) = 33
  // M(5, 9) = 54
  // M(5, 10) = 89
  // M(5, 11) = 148
  // M(5, 12) = 245
  // M(5, 13) = 408

  // r - floor((r + c - 1)/c)*2 + 1 = c
  // r even:
  // floor((c*r/2 - r - c + 1)/c) = (c - 1)/2
  // r >= (c*(c - 1) + 2*c - 2)/(c - 2)
  //
  // r odd:
  // 2*(r + 1)/2 - 1 - floor((r + c - 1)/c)*2 + 1 = c
  // 2*floor((r + 1)/2 - (r + c - 1)/c) = c
  // (r + 1)/2 - (r + c - 1)/c >= c/2

  public long solve() {
    long sum = 0;
    for (int i = 40; i >= 3; i--) {
      long res = m(i, 30);
      System.out.println(i + ": " + res);
      sum += res;
    }
    return sum;
  }

  private long m(int c, int r) {
    long count = 0;
    for (int i = 0; i <= r; i++) {
      count = findx(count, c);
    }
    return count;
  }

  private long findx(long y, int c) {
    if (y == 0) {
      return 1;
    }
    if (y == 1) {
      return 2;
    }
    long res = (c * y - 3) / (c - 2);
    if (((y + res) & 1) == 0) {
      res++;
    }
    Preconditions.checkState(res - ((res + c - 1)/c)*2 + 1 >= y);
    return res;
  }
}
