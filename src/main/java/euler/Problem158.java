package euler;


public class Problem158 {

  public static void main(String[] args) {
    System.out.println(new Problem158().solve());
  }

  public long solve() {
    long max = 0L;
    long[][] comb = new long[27][27];
    for (int n = 0; n <= 26; n++) {
      comb[n][0] = comb[n][n] = 1;
      for (int k = 1; k < n; k++) {
        comb[n][k] = comb[n - 1][k - 1] + comb[n - 1][k];
      }
    }
    for (int n = 2; n <= 26; n++) {
      long count = 0L;
      for (int l1 = 0; 2 + l1 <= n; l1++) {
        for (int l2 = 0; 2 + l1 + l2 <= n; l2++) {
          for (int l3 = 0; 2 + l1 + l2 + l3 <= n; l3++) {
            int l4 = n - 2 - l1 - l2 - l3;
            for (int a = l4; a < 26; a++) {
              for (int b = a + l2 + l3 + 1; b < 26 - l1; b++) {
                count += comb[26 - b - 1][l1] * comb[a][l4]
                    * comb[b - a - 1][l2 + l3] * comb[l2 + l3][l2];
              }
            }
          }
        }
      }
      max = Math.max(max, count);
    }
    return max;
  }
}
