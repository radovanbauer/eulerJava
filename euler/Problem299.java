package euler;

import static com.google.common.math.LongMath.checkedMultiply;

public class Problem299 {

  public static void main(String[] args) {
    Runner.run(new Problem299()::solve);
  }

  public long solve() {
    long count = 0;
    long max = 100_000_000;

    FactorizationSieve sieve = new FactorizationSieve(max);
    Factorization twoFact = sieve.factorization(2);

    // 2*(b - a)*(d - a) == a^2
    for (long a = 2; a < max; a += 2) {
      Factorization aFact = sieve.factorization(a);
      Factorization aSqHalfFact = aFact.multiply(aFact).divide(twoFact);
      long aSqHalf = checkedMultiply(a, a) / 2;
      for (long div : aSqHalfFact.unsortedDivisors()) {
        long b = div + a;
        long d = aSqHalf / div + a;
        if (b + d < max) {
          count++;
//          System.out.printf("a=%d b=%d d=%d\n", a, b, d);
        }
      }
    }

    // 2*p*(a - p) == (b - a)^2
    for (long baDiff = 2; baDiff < max; baDiff += 2) {
      Factorization baDiffFact = sieve.factorization(baDiff);
      Factorization baDiffSqHalfFact = baDiffFact.multiply(baDiffFact).divide(twoFact);
      long baDiffSqHalf = checkedMultiply(baDiff, baDiff) / 2;
      for (long div : baDiffSqHalfFact.unsortedDivisors()) {
        long p = div;
        long a = baDiffSqHalf / p + p;
        long b = a + baDiff;
        if (2 * b < max && 2 * p < a) {
          count++;
//          System.out.printf("a=%d b=d=%d p=%d\n", a, b, p);
        }
      }
    }

    return count;
  }
}
