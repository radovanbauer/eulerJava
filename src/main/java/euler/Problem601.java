package euler;

import com.google.common.math.LongMath;

import java.util.stream.IntStream;

import static com.google.common.math.LongMath.checkedPow;
import static euler.Runner.run;

public class Problem601 {
  public static void main(String[] args) {
    run(new Problem601()::solve);
  }

  public long solve() {
    return IntStream.rangeClosed(1, 31).mapToLong(i -> P(i, checkedPow(4, i))).sum();
  }

  private long P(int s, long N) {
    long lcm1 = 1;
    for (int i = 2; i <= s; i++) {
      lcm1 = lcm(lcm1, i);
    }
    long lcm2 = lcm(lcm1, s + 1);
    return (N - 2) / lcm1 - (N - 2) / lcm2;
  }

  private long lcm(long a, long b) {
    return a / LongMath.gcd(a, b) * b;
  }
}
