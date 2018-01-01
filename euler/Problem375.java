package euler;

public class Problem375 {

  public static void main(String[] args) {
    System.out.println(new Problem375().solve());
  }

  public long solve() {
    int s = 290797;
    int n = 2_000_000_000;
    long sum = 0;
    long sumMin = 0;
    int[] min = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      s = (int) (1L * s * s % 50515093);
      min[i] = s;
      sumMin += min[i];
      for (int j = i - 1; j >= 1 && min[j] > s; j--) {
        sumMin += s - min[j];
        min[j] = s;
      }
      sum += sumMin;
    }
    return sum;
  }
}
