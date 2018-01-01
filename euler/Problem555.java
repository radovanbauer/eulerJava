package euler;


public class Problem555 {

  public static void main(String[] args) {
    System.out.println(new Problem555().solve());
  }

  public long solve() {
    long m = 1_000_000;
    FactorizationSieve sieve = new FactorizationSieve(m);
    long sum = 0L;
    for (long s = 1; s <= m; s++) {
      for (long div : sieve.divisors(s)) {
        long k = s + div;
        if (k <= m) {
          sum += (2 * m - 3 * s + k + 1) * (k - s) / 2;
        }
      }
    }
    return sum;
  }
}
