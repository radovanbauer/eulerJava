package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import com.google.common.math.BigIntegerMath;

public class Problem266 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem266().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    Primes primes = new Primes(190);
    List<BigInteger> allPrimes = IntStream.range(0, 190)
        .filter(i -> primes.isPrime(i))
        .mapToObj(i -> BigInteger.valueOf(i))
        .collect(Collectors.toList());
    List<BigInteger> primes1 = allPrimes.subList(0, allPrimes.size() / 2);
    List<BigInteger> primes2 = allPrimes.subList(allPrimes.size() / 2, allPrimes.size());
    List<BigInteger> primes1Products = Sets.powerSet(ImmutableSet.copyOf(primes1)).parallelStream()
        .map(this::bigIntegerProduct)
        .sorted()
        .collect(Collectors.toList());
    List<BigInteger> primes2Products = Sets.powerSet(ImmutableSet.copyOf(primes2)).parallelStream()
        .map(this::bigIntegerProduct)
        .sorted()
        .collect(Collectors.toList());
    BigInteger max = BigIntegerMath.sqrt(bigIntegerProduct(allPrimes), RoundingMode.FLOOR);
    BigInteger res = BigInteger.ZERO;
    int p2 = primes2Products.size() - 1;
    for (int p1 = 0; p1 < primes1Products.size(); p1++) {
      while (primes1Products.get(p1).multiply(primes2Products.get(p2)).compareTo(max) > 0 && p2 > 0) {
        p2--;
      }
      res = Ordering.natural().max(res, primes1Products.get(p1).multiply(primes2Products.get(p2)));
    }
    return res.mod(BigInteger.valueOf(10_000_000_000_000_000L)).longValueExact();
  }

  private BigInteger bigIntegerProduct(Collection<BigInteger> bigIntegers) {
    return bigIntegers.stream().reduce(BigInteger.ONE, (a, b) -> a.multiply(b));
  }

  private static class Primes {
    private final int max;
    private final boolean[] nonPrimes;

    public Primes(int max) {
      this.max = max;
      nonPrimes = new boolean[max + 1];
      for (int i = 2; i <= max; i++) {
        if (!nonPrimes[i]) {
          long j = 1L * i * i;
          while (j <= max) {
            nonPrimes[(int) j] = true;
            j += i;
          }
        }
      }
    }

    public boolean isPrime(int n) {
      checkArgument(n <= max);
      return n > 1 && !nonPrimes[n];
    }
  }
}
