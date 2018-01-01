package euler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class Problem537 {

  public static void main(String[] args) {
    System.out.println(new Problem537().solve());
  }

  private static final long MOD = 1_004_535_809;

  public long solve() {
    int max = 20_000;
    FactorizationSieve sieve = new FactorizationSieve(max * 100);
    Multiset<Integer> pi = HashMultiset.create();
    int primeCount = 0;
    for (int n = 1;; n++) {
      if (sieve.isPrime(n)) {
        primeCount++;
      }
      if (primeCount > max) {
        break;
      }
      pi.add(primeCount);
    }
    List<Integer> keys = new ArrayList<>();
    int m = max;
    while (m > 0) {
      keys.add(m);
      if (m % 2 == 0) {
        m /= 2;
      } else {
        m--;
      }
    }
    keys.add(0);
    Collections.sort(keys);
    long[][] count = new long[keys.size()][max + 1];
    count[0][0] = 1;
    for (int k = 1; k < keys.size(); k++) {
      for (int n = 0; n <= max; n++) {
        if (keys.get(k) % 2 == 0) {
          for (int i = 0; i <= n; i++) {
            count[k][n] = (count[k][n] + count[k - 1][n - i] * count[k - 1][i]) % MOD;
          }
        } else {
          for (int i = 0; i <= n; i++) {
            count[k][n] = (count[k][n] + count[k - 1][n - i] * pi.count(i)) % MOD;
          }
        }
      }
    }
    return count[keys.size() - 1][max];
  }
}
