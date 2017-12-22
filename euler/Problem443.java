package euler;

import java.util.ArrayList;
import java.util.List;

public class Problem443 {

  public static void main(String[] args) {
    System.out.println(new Problem443().solve());
  }

  public long solve() {
    long m = 1_000_000_000_000_000L;
    long n = 9;
    while (true) {
      List<Long> primes = primeDivisors(2 * n - 1);
      long min = 2 * n;
      for (long prime : primes) {
        min = Math.min(min, (n / prime + 1) * prime);
      }
      if (min > m) {
        break;
      }
      n = min;
    }
    return 3 * n + (m - n);
  }

  private List<Long> primeDivisors(long n) {
    List<Long> primes = new ArrayList<>();
    long d = 2;
    while (n > 1 && d * d <= n) {
      if (n % d == 0) {
        primes.add(d);
        while (n % d == 0) {
          n /= d;
        }
      }
      d++;
    }
    if (n > 1) {
      primes.add(n);
    }
    return primes;
  }
}
