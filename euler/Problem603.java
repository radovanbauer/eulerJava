package euler;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;

public class Problem603 {
  public static void main(String[] args) {
    Runner.run(new Problem603()::solve);
  }

  public long solve() {
    long mod = 1_000_000_007;
    long n = 1_000_000;
    long k = LongMath.pow(10, 12);

    ImmutableList<Long> primes = ImmutableList.copyOf(PrimeSieve.create(n * 20));
    Preconditions.checkState(primes.size() >= n);
    StringBuilder primeString = new StringBuilder();
    for (int i = 0; i < n; i++) {
      primeString.append(String.valueOf(primes.get(i)));
    }

    LongModMatrix2 count = LongModMatrix2.create(new long[][]{{0, 0, 1, 1}}, mod);
    LongModMatrix2 transition = LongModMatrix2.identity(4, mod);
    for (int i = 0; i < primeString.length(); i++) {
      int d = primeString.charAt(i) - '0';
      transition = transition.multiply(LongModMatrix2.create(new long[][] {
          {1, 0, 0, 0},
          {10, 10, 0, 0},
          {d, d, 1, 0},
          {0, 0, 1, 1},
      }, mod));
    }

    LongModMatrix2 result = count.multiply(transition.pow(k));

    return result.element(0, 0);
  }
}
