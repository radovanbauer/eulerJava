package euler;

import java.util.List;

import com.google.common.math.IntMath;
import com.google.common.math.LongMath;

public class Problem550 {

  public static void main(String[] args) {
    System.out.println(new Problem550().solve());
  }

  public long solve() {
    int n = IntMath.pow(10, 7);
    long k = LongMath.pow(10, 12);
    long mod = 987654321;
    int maxGrundy = 64;
    int[] grundy = new int[n + 1];
    FactorizationSieve sieve = new FactorizationSieve(n);
    for (int i = 2; i <= n; i++) {
      boolean[] moves = new boolean[maxGrundy];
      List<Integer> divs = sieve.unsortedDivisors(i);
      for (int d1 : divs) {
        if (d1 > 1 && d1 < i) {
          for (int d2 : divs) {
            if (d2 > 1 && d2 < i) {
              moves[grundy[d1] ^ grundy[d2]] = true;
            }
          }
        }
      }
      grundy[i] = 0;
      while (moves[grundy[i]]) {
        grundy[i]++;
      }
    }
    long[] gCount = new long[maxGrundy];
    for (int i = 2; i <= n; i++) {
      gCount[grundy[i]]++;
    }
    long[][] transitions = new long[maxGrundy][maxGrundy];
    for (int from = 0; from < maxGrundy; from++) {
      for (int to = 0; to < maxGrundy; to++) {
        transitions[to][from] += gCount[to ^ from];
      }
    }
    LongModMatrix2 result = LongModMatrix2.create(transitions, mod).pow(k - 1)
        .multiply(LongModMatrix2.create(new long[][] {gCount}, mod).transpose());
    LongMod sum = LongMod.zero(mod);
    for (int winningG = 1; winningG < maxGrundy; winningG++) {
      sum = sum.add(result.element(winningG, 0));
    }
    return sum.n();
  }
}
