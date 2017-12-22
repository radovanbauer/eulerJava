package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

public class PrimeSieve implements Iterable<Long> {

  private static final int SEGMENT_SIZE = 1_000_000;

  private final long min;
  private final long max;
  private final long[] primes;

  private PrimeSieve(long min, long max) {
    checkArgument(min >= 0);
    checkArgument(min <= max);
    this.min = min;
    this.max = max;
    int sqrt = Ints.checkedCast(LongMath.sqrt(max, RoundingMode.FLOOR));
    boolean[] isPrime = new boolean[sqrt + 1];
    Arrays.fill(isPrime, true);
    for (int i = 2; 1L * i * i <= sqrt; i++) {
      if (isPrime[i]) {
        for (int k = i * i; k <= sqrt; k += i) {
          isPrime[k] = false;
        }
      }
    }
    LongArrayList smallPrimes = new LongArrayList();
    for (int i = 2; i <= sqrt; i++) {
      if (isPrime[i]) {
        smallPrimes.addLong(i);
      }
    }
    LongArrayList allPrimes = new LongArrayList();
    List<Long> segmentLows = new LongArrayList();
    for (long low = Math.max(min, 2); low <= max; low += SEGMENT_SIZE) {
      segmentLows.add(low);
    }
    segmentLows.stream().parallel()
        .map(low -> singleSegment(max, smallPrimes, low))
        .forEachOrdered(allPrimes::addAll);
    this.primes = allPrimes.toLongArray();
  }

  private static LongArrayList singleSegment(long max, LongArrayList smallPrimes, long low) {
    long high = Math.min(low + SEGMENT_SIZE, max + 1);
    boolean[] sieve = new boolean[Ints.checkedCast(high - low)];
    Arrays.fill(sieve, true);
    for (long p : smallPrimes) {
      for (long i = Math.max(((low - 1) / p + 1), 2) * p - low; i < sieve.length; i += p) {
        sieve[Ints.checkedCast(i)] = false;
      }
    }
    LongArrayList localPrimes = new LongArrayList();
    for (int i = 0; i < sieve.length; i++) {
      if (sieve[i] && low + i <= max) {
        localPrimes.addLong(low + i);
      }
    }
    return localPrimes;
  }

  public static PrimeSieve create(long max) {
    return new PrimeSieve(1, max);
  }

  public static PrimeSieve create(long min, long max) {
    return new PrimeSieve(min, max);
  }

  @Override
  public Iterator<Long> iterator() {
    return Longs.asList(primes).iterator();
  }

  public long get(int i) {
    return primes[i];
  }

  public int primeCount() {
    return primes.length;
  }

  public boolean isPrime(long n) {
    checkArgument(n >= min && n <= max);
    return Arrays.binarySearch(primes, n) >= 0;
  }

  @Override
  public String toString() {
    return Arrays.toString(primes);
  }
}
