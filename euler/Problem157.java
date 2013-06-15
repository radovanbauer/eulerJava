package euler;


public class Problem157 {

  public static void main(String[] args) {
    System.out.println(new Problem157().solve());
  }

  public long solve() {
    long cnt = 0L;
    long[] pow2 = calcPow(2, 18);
    long[] pow5 = calcPow(5, 18);
    for (int n = 1; n <= 9; n++) {
      for (int i = 0; i <= 2 * n; i++) {
        for (int j = 0; j <= 2 * n; j++) {
          for (int k = 0; k <= 2 * n; k++) {
            for (int l = 0; l <= 2 * n; l++) {
              long aa = pow2[i] * pow5[j];
              long bb = pow2[k] * pow5[l];
              if (aa > bb) continue;
              long xx = aa + bb;
              int x = 0;
              int y = 0;
              while (xx % 2 == 0) {
                xx /= 2;
                x++;
              }
              while (xx % 5 == 0) {
                xx /= 5;
                y++;
              }
              if (i + k > n + x) continue;
              if (j + l > n + y) continue;
              cnt += numDiv(xx);
            }
          }
        }
      }
    }
    return cnt;
  }

  private long[] calcPow(int b, int max) {
    long[] res = new long[max + 1];
    res[0] = 1;
    for (int i = 1; i <= max; i++) {
      res[i] = res[i - 1] * b;
    }
    return res;
  }

  private int numDiv(long n) {
    int cnt = 0;
    for (long d = 1; d * d <= n; d++) {
      if (n % d == 0) {
        cnt += (d * d == n) ? 1 : 2;
      }
    }
    return cnt;
  }
}
