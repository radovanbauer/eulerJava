package euler;


public class Problem191 {

  public static void main(String[] args) {
    System.out.println(new Problem191().solve(30));
  }

  public long solve(int n) {
    long[] oa = new long[Math.max(3, n + 1)];
    oa[0] = 1;
    oa[1] = 2;
    oa[2] = 4;
    for (int i = 3; i <= n; i++) {
      oa[i] = oa[i - 1] + oa[i - 2] + oa[i - 3];
    }
    long res = oa[n];
    for (int l = 0; l < n; l++) {
      res += oa[l] * oa[n - l - 1];
    }
    return res;
  }
}
