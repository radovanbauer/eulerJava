package euler;

public class Problem539 {

  public static void main(String[] args) {
    System.out.println(new Problem539().solve());
  }

  public long solve() {
    long mod = 987654321;
    return s(1_000_000_000_000_000_000L, mod);
  }

  private long p(long n) {
    if (n == 1) {
      return 1;
    } else if (n == 2) {
      return 2;
    } else if (n == 3) {
      return 2;
    } else if (n % 4 == 0 || n % 4 == 1) {
      return 4 * p(n / 4) - 2;
    } else {
      return 4 * p(n / 4);
    }
  }

  private long s(long n, long mod) {
    if (n == 0) {
      return 0;
    } else if (n % 4 != 3) {
      return (s(n - 1, mod) + p(n)) % mod;
    } else {
      return (16 * s(n / 4, mod) - 4 * (n / 4) + 5) % mod;
    }
  }
}
