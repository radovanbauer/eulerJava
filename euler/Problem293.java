package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.primitives.Ints;

public class Problem293 {

  public static void main(String[] args) {
    System.out.println(new Problem293().solve());
  }

  public long solve() {
    int n = 1_000_000_000;
    Primes primes = new Primes(n);
    Set<Integer> pseudoFortunateNumbers = new HashSet<>();
    for (int i = 0; i < n; i++) {
      if (i % 1000000 == 0) {
        System.out.println(i);
      }
      if (isAdmissible(i, primes)) {
        pseudoFortunateNumbers.add(pseudoFortunate(i, primes));
      }
    }
    return pseudoFortunateNumbers.stream().mapToInt(i -> i).sum();
  }

  private boolean isAdmissible(int n, Primes primes) {
    if (n <= 0 || n % 2 != 0) {
      return false;
    }
    int[] divisors = primes.primeDivisors(n);
    if (divisors.length == 1) {
      return divisors[0] == 2;
    }
    for (int i = 0; i < divisors.length - 1; i++) {
      int p = divisors[i] + 1;
      while (p < divisors[i + 1]) {
        if (primes.isPrime(p)) {
          return false;
        }
        p++;
      }
    }
    return true;
  }

  private int pseudoFortunate(int n, Primes primes) {
    for (int m = 2;; m++) {
      if (primes.isPrime(n + m)) {
        return m;
      }
    }
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

    public boolean isPrime(int n) {
      checkArgument(n <= max);
      return n > 1 && !nonPrimes[n];
    }

    public int[] primeDivisors(int n) {
      List<Integer> divisors = new ArrayList<>();
      while (n > 1) {
        int prime = smallestPrimeDivisor[n];
        divisors.add(prime);
        while (n % prime == 0) {
          n /= prime;
        }
      }
      return Ints.toArray(divisors);
    }
  }
}
