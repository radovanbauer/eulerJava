package euler;

import com.google.common.math.LongMath;

public class Problem531 {

  public static void main(String[] args) {
    System.out.println(new Problem531().solve());
  }

  public long solve() {
    int min = 1_000_000;
    int max = 1_005_000;
    FactorizationSieve sieve = new FactorizationSieve(max);
    long sum = 0;
    for (int n = min; n < max; n++) {
      for (int m = n + 1; m < max; m++) {
        sum += g(sieve.totient(n), n, sieve.totient(m), m);
      }
    }
    return sum;
  }

  private long g(long a, long n, long b, long m) {
    long g = LongMath.gcd(n, m);
    if ((a - b) % g != 0) {
      return 0L;
    }
    long lcm = n / g * m;
    return BigMod.create(a, lcm).add(
        BigMod.create(n, lcm)
            .multiply(LongMod.create(n / g, m / g).invert().n())
            .multiply((b - a) / g)).n().longValueExact();
  }
}
