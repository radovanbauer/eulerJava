package euler;

import java.util.List;

import com.google.common.collect.Lists;

public class Problem187 {

  public static void main(String[] args) {
    System.out.println(new Problem187().solve(100000000));
  }

  public long solve(int n) {
    int cnt = 0;
    List<Integer> primes = primes(n);
    int primeCnt = primes.size();
    for (int i = 0; i < primeCnt; i++) {
      for (int j = i; j < primeCnt; j++) {
        long prod = ((long) primes.get(i)) * primes.get(j);
        if (prod >= n) {
          break;
        }
        cnt++;
      }
    }
    return cnt;
  }

  private List<Integer> primes(int n) {
    boolean[] sieve = new boolean[n];
    for (int d = 2; d < n; d++) {
      if (!sieve[d]) {
        for (long k = ((long) d) * d; k < n; k += d) {
          sieve[(int) k] = true;
        }
      }
    }
    List<Integer> primes = Lists.newArrayList();
    for (int i = 2; i < n; i++) {
      if (!sieve[i]) {
        primes.add(i);
      }
    }
    return primes;
  }
}
