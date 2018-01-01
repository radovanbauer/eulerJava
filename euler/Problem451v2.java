package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.IntStream;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.Striped;

public class Problem451v2 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem451v2().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 20_000_000;
    long[] l = new long[max + 1];
    Arrays.fill(l, 1L);
    Primes primes = new Primes(max);
    Striped<Lock> locks = Striped.lock(Runtime.getRuntime().availableProcessors() * 16);
    IntStream.rangeClosed(3, max - 2).parallel().forEach(n -> {
      List<Integer> divs1 = primes.divisors(n - 1);
      List<Integer> divs2 = primes.divisors(n + 1);
      for (long d1 : divs1) {
        for (long d2 : divs2) {
          long d = d1 * d2;
          if (d - 1 > n && d <= max) {
            Lock lock = locks.get(d);
            lock.lock();
            l[(int) d] = Math.max(l[(int) d], n);
            lock.unlock();
          }
        }
      }
    });
    return Arrays.stream(l).sum();
  }

  private static class Primes {
    private final int max;
    private final boolean[] nonPrimes;
    private final int[] smallestPrimeDivisor;

    public Primes(int max) {
      this.max = max;
      nonPrimes = new boolean[max + 1];
      smallestPrimeDivisor = new int[max + 1];
      for (int i = 2; i <= max; i++) {
        if (!nonPrimes[i]) {
          smallestPrimeDivisor[i] = i;
          for (long j = 1L * i * i; j <= max; j += i) {
            if (!nonPrimes[(int) j]) {
              nonPrimes[(int) j] = true;
              smallestPrimeDivisor[(int) j] = i;
            }
          }
        }
      }
    }

    public List<Integer> divisors(int n) {
      checkArgument(n <= max);
      List<Integer> result = new ArrayList<>();
      result.add(1);
      divisors(n, 1, result);
      return result;
    }

    private void divisors(int n, int k, List<Integer> result) {
      if (n == 1) {
        return;
      }
      int prime = smallestPrimeDivisor[n];
      int power = 0;
      while (n % prime == 0) {
        n /= prime;
        power++;
      }
      for (int i = 0; i <= power; i++) {
        if (i != 0) {
          result.add(k);
        }
        divisors(n, k, result);
        k *= prime;
      }
    }
  }
}
