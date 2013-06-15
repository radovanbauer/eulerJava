package euler;


public class Problem174 {

  public static void main(String[] args) {
    System.out.println(new Problem174().solve(1000000));
  }

  public int solve(int n) {
    int sum = 0;
    for (int i = 4; i <= n; i += 4) {
      int cnt = 0;
      for (int d = 2; d * d < i; d += 2) {
        if (i % d == 0) {
          if ((i / d) % 2 == 0) {
            cnt++;
          }
        }
      }
      if (cnt >= 1 && cnt <= 10) {
        sum ++;
      }
    }
    return sum;
  }
}
