package euler;

public class Problem214 {

  public static void main(String[] args) {
    System.out.println(new Problem214().solve(40000000, 25));
  }

  public long solve(int n, int length) {
    int[] phi = new int[n];
    for (int i = 0; i < n; i++) {
      phi[i] = i;
    }
    for (int i = 2; i < n; i++) {
      if (phi[i] == i) {
        phi[i] = i - 1;
        int j = i * 2;
        while (j < n) {
          phi[j] = phi[j] / i * (i - 1);
          j += i;
        }
      }
    }
    long sum = 0L;
    int[] chain = new int[n];
    chain[1] = 1;
    for (int i = 2; i < n; i++) {
      chain[i] = chain[phi[i]] + 1;
      if (phi[i] == i - 1 && chain[i] == length) {
        sum += i;
      }
    }
    return sum;
  }
}
