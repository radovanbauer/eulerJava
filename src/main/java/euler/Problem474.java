package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableList.toImmutableList;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.common.base.Stopwatch;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Problem474 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem474().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
//    int n = 50;
//    int mod = 1000;
//    int r = 123;
    int n = 1_000_000;
    int mod = 100_000;
    int x = 65_432;
    long countMod = LongMath.pow(10, 16) + 61;
    final long[] count = new long[mod];
    count[1] = 1;
    ImmutableList<Long> primes = new FactorizationSieve(n).getAllPrimes();
    for (long prime : primes) {
      int p = Ints.checkedCast(prime);
      int exp = factorialExponent(n, p);
      System.out.println(p + ": " + exp);
      long[] newCount = ImmutableList.copyOf(Iterables.partition(IntStream.range(0, mod).boxed().collect(toImmutableList()), 1000)).stream()
          .parallel()
          .map(ms -> getCounts(count, mod, countMod, p, exp, ms))
          .reduce((a, b) -> add(a, b, countMod))
          .get();
      System.arraycopy(newCount, 0, count, 0, mod);
    }
    return count[x];
  }

  private long[] getCounts(long[] count, int mod, long countMod, long p, int exp, List<Integer> ms) {
    long[] newCount = new long[mod];
    for (int m : ms) {
      if (count[m] != 0) {
        int pPow = 1;
        for (int e = 0; e <= exp; e++) {
          int idx = LongMath.mod((long) m * pPow, mod);
          newCount[idx] += count[m];
          while (newCount[idx] >= countMod) {
            newCount[idx] -= countMod;
          }
          pPow = LongMath.mod(p * pPow, mod);
        }
      }
    }
    return newCount;
  }

  private long[] add(long[] left, long[] right, long mod) {
    checkArgument(left.length == right.length);
    long[] res = new long[left.length];
    for (int i = 0; i < res.length; i++) {
      res[i] = LongMath.mod(LongMath.checkedAdd(left[i], right[i]), mod);
    }
    return res;
  }

  private Iterable<Integer> primePowIterable(int p, int mod) {
    return new Iterable<Integer>() {
      @Override
      public Iterator<Integer> iterator() {
        return new AbstractIterator<Integer>() {
          private int next = 1;

          @Override
          protected Integer computeNext() {
            int res = next;
            next = LongMath.mod(1L * next * p, mod);
            return res;
          }
        };
      }
    };
  }

  private int factorialExponent(int n, int prime) {
    int result = 0;
    for (long primePow = prime; primePow <= n; primePow *= prime) {
      result += n / primePow;
    }
    return result;
  }
}
