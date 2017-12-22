package euler;

public class Problem577 {

  public void solve() {
    long[] H = new long[12346];
    long sum = 0L;
    for (int n = 3; n <= 12345; n++) {
      H[n] = 3 * H[n - 1] - 3 * H[n - 2] + H[n - 3] + ((n % 3 == 0) ? n / 3 : 0);
      sum += H[n];
    }
    System.out.println(sum);
  }

  public static void main(String[] args) {
    new Problem577().solve();
  }
}
