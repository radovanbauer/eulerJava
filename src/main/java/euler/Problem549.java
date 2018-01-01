package euler;

import java.util.stream.LongStream;

import com.google.common.collect.ImmutableSet;

public class Problem549 {

  public static void main(String[] args) {
    System.out.println(new Problem549().solve());
  }

  public long solve() {
    long n = 100_000_000;
    FactorizationSieve sieve = new FactorizationSieve(n);
    return LongStream.rangeClosed(2, n).parallel().map(i -> s(i, sieve)).sum();
  }

  private long s(long n, FactorizationSieve sieve) {
    long m = 1;
    Factorization factorization = sieve.factorization(n);
    ImmutableSet<Long> primes = factorization.getPrimes();
    for (long prime : primes) {
      int exp = factorization.getExponent(prime);
      int sum = 0;
      int mul = 0;
      while (sum < exp) {
        mul++;
        sum++;
        int x = mul;
        while (x % prime == 0) {
          sum++;
          x /= prime;
        }
      }
      m = Math.max(m, mul * prime);
    }
    return m;
  }
}
