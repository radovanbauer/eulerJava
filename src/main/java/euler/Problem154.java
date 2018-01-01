package euler;

public class Problem154 {

  public static void main(String[] args) {
    System.out.println(new Problem154().solve(200000));
  }

  public int solve(int n) {
    int[] powers5 = calcPowers(n, 5);
    int[] powers2 = calcPowers(n, 2);
    int count = 0;
    for (int a = 0; a <= n; a++) {
      for (int b = 0; a + b <= n; b++) {
        if (powers5[n] - powers5[a] - powers5[b] - powers5[n - a - b] >= 12 &&
            powers2[n] - powers2[a] - powers2[b] - powers2[n - a - b] >= 12) {
          count++;
        }
      }
    }
    return count;
  }

  private int[] calcPowers(int n, int d) {
    int[] powers = new int[n + 1];
    for (int k = 1; k <= n; k++) {
      powers[k] = powers[k - 1];
      int l = k;
      while (l % d == 0) {
        powers[k]++;
        l /= d;
      }
    }
    return powers;
  }
}
