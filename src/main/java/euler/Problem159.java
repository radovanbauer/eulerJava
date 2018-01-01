package euler;

public class Problem159 {

  public static void main(String[] args) {
    System.out.println(new Problem159().solve2());
  }

  public long solve1() {
    long sum = 0L;
    long[] mdrs = new long[1000000];
    for (int n = 2; n < 1000000; n++) {
      mdrs[n] = digitalRoot(n);
      for (int d = 2; d * d <= n; d++) {
        if (n % d == 0) {
          mdrs[n] = Math.max(mdrs[n], mdrs[d] + mdrs[n / d]);
        }
      }
      sum += mdrs[n];
    }
    return sum;
  }

  public long solve2() {
    long sum = 0L;
    long[] mdrs = new long[1000000];
    for (int n = 2; n < 1000000; n++) {
      mdrs[n] = Math.max(mdrs[n], digitalRoot(n));
      sum += mdrs[n];
      for (int k = 1; k <= n && n * k < 1000000; k++) {
        mdrs[n * k] = Math.max(mdrs[n * k], mdrs[n] + mdrs[k]);
      }
    }
    return sum;
  }

  private int digitalRoot(long n) {
    return n < 10 ? (int) n : digitalRoot(sumOfDigits(n));
  }

  private int sumOfDigits(long n) {
    int sum = 0;
    while (n > 0) {
      sum += n % 10;
      n /= 10;
    }
    return sum;
  }
}
