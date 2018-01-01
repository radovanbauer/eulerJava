package euler;

public class Problem429 {

  public static void main(String[] args) {
    System.out.println(new Problem429().solve());
  }

  public long solve() {
    long n = 100_000_000;
    PrimeSieve sieve = PrimeSieve.create(n);
    int mod = 1_000_000_009;
    LongMod sum = LongMod.create(1, mod);
    for (long p = 2; p <= n; p++) {
      if (sieve.isPrime(p)) {
        long pPow = p;
        int exp = 0;
        while (pPow <= n) {
          exp += n / pPow;
          pPow *= p;
        }
        sum = sum.add(sum.multiply(LongMod.create(p, mod).pow(2 * exp)));
      }
    }
    return sum.n();
  }
}
