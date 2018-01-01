package euler;

import com.google.common.collect.Streams;
import com.google.common.math.LongMath;

public class Problem274 {
  public static void main(String[] args) {
    Runner.run(new Problem274()::solve);
  }

  public long solve() {
    return 1 + Streams.stream(PrimeSieve.create(7, LongMath.pow(10, 7)))
        .mapToLong(p -> LongMod.create(10, p).invert().n())
        .sum();
  }
}
