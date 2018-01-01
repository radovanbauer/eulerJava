package euler;

public class Problem179 {

  public static void main(String[] args) {
    System.out.println(new Problem179().solve2(10000000));
  }

  public int solve(int max) {
    int cnt = 0;
    int lastDivCnt = 1;
    for (int n = 2; n < max; n++) {
      int divCnt = 0;
      for (int d = 1; d * d <= n; d++) {
        if (n % d == 0) {
          divCnt++;
          if (d * d < n) {
            divCnt++;
          }
        }
      }
      if (lastDivCnt == divCnt) {
        cnt++;
      }
      lastDivCnt = divCnt;
    }
    return cnt;
  }

  public int solve2(int max) {
    int[] cnt = new int[max];
    for (int d = 1; d < max; d++) {
      int e = d;
      while (e < max) {
        cnt[e]++;
        e += d;
      }
    }
    int res = 0;
    for (int n = 2; n < max; n++) {
      if (cnt[n] == cnt[n - 1]) {
        res++;
      }
    }
    return res;
  }
}
