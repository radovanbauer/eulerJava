package euler;

public class Problem313 {

  public static void main(String[] args) {
    System.out.println(new Problem313().solve());
  }

  public long solve() {
    int max = 1_000_000;
    FactorizationSieve sieve = new FactorizationSieve(max);
    long count = 0;
    for (int p = 3; p < max; p++) {
      if (sieve.isPrime(p)) {
        long p2 = 1L * p * p;

        long maxm = (p2 + 7) / 8;
        while ((p2 + 13 - 2 * maxm) % 6 != 0) {
          maxm--;
        }
        long minm = p == 3 ? 2 : 4;
        if (minm <= maxm) {
          count += 2 * ((maxm - minm) / 3 + 1);
        }
      }
    }
    return count;
  }
}
