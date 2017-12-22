package euler;

import static com.google.common.base.Preconditions.checkState;

import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;

public class Problem536 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem536().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long max = 1_000_000_000_000L;
    long maxSqrt = LongMath.sqrt(max, RoundingMode.FLOOR);
    long smallPrimeMax = LongMath.sqrt(maxSqrt, RoundingMode.FLOOR) * 2;
    FactorizationSieve sieve = new FactorizationSieve(max / smallPrimeMax);
    ImmutableList<Long> smallPrimes = sieve.getPrimes(smallPrimeMax);
    long smallPrimeSum = solve(smallPrimes, max, new ArrayDeque<Long>(), 0, 1, 1);
    long bigPrimeSum = 0;
    for (long p : sieve.getPrimes(maxSqrt)) {
      if (p > smallPrimeMax) {
        next: for (long rem = p - 4; p * rem <= max; rem += p - 1) {
          checkState((p * rem + 3) % (p - 1) == 0);
          Factorization remFact = sieve.factorization(rem);
          for (Factorization.PrimeFactor factor : remFact.factors()) {
            if (factor.exp() > 1 || factor.prime() >= p || (p * rem + 3) % (factor.prime() - 1) != 0) {
              continue next;
            }
          }
          bigPrimeSum += p * rem;
        }
      }
    }
    return 1 + smallPrimeSum + bigPrimeSum;
  }

  private long solve(List<Long> primes, long max, ArrayDeque<Long> chosen, int next, long product, long lambda) {
    long sum = 0;
    for (int i = next; i < primes.size(); i++) {
      long prime = primes.get(i);
      if (product > max / prime) {
        break;
      }
      long newProduct = product * prime;
      long newLambda = lcm(lambda, prime - 1);
      if ((newProduct + 3) % newLambda == 0) {
        sum += newProduct;
      }
      chosen.addLast(prime);
      sum += solve(primes, max, chosen, i + 1, newProduct, newLambda);
      chosen.removeLast();
    }
    return sum;
  }

  private long lcm(long a, long b) {
    return a / LongMath.gcd(a, b) * b;
  }
}
