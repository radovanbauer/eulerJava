package euler;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem485 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem485().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int u = 100_000_000;
    int k = 100_000;
    Primes primes = new Primes(u);
    ArrayDeque<Integer> divisorNumbers = new ArrayDeque<Integer>();
    long sum = 0;
    for (int n = 1; n <= u; n++) {
      int added = primes.numberOfDivisors(n);
      while (!divisorNumbers.isEmpty() && divisorNumbers.peekLast() < added) {
        divisorNumbers.removeLast();
      }
      divisorNumbers.add(added);
      if (n > k) {
        int removed = primes.numberOfDivisors(n - k);
        if (divisorNumbers.peekFirst() == removed) {
          divisorNumbers.removeFirst();
        }
      }
      if (n >= k) {
        sum += divisorNumbers.peekFirst();
      }
    }
    return sum;
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
          long j = 1L * i * i;
          while (j <= max) {
            if (!nonPrimes[(int) j]) {
              nonPrimes[(int) j] = true;
              smallestPrimeDivisor[(int) j] = i;
            }
            j += i;
          }
        }
      }
    }

    public int numberOfDivisors(int n) {
      int res = 1;
      while (n > 1) {
        int prime = smallestPrimeDivisor[n];
        int factor = 0;
        while (n % prime == 0) {
          n /= prime;
          factor++;
        }
        res *= (factor + 1);
      }
      return res;
    }
  }
}
