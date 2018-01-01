package euler;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MinMaxPriorityQueue;
import com.google.common.math.LongMath;

public class Problem538 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem538().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int n = 3_000_000;
    MinMaxPriorityQueue<Long> sides = MinMaxPriorityQueue
        .orderedBy(Comparator.<Long>reverseOrder())
        .maximumSize(5)
        .create();
    long sum = 0;
    for (int i = 1; i <= n; i++) {
      sides.add(LongMath.pow(2, Integer.bitCount(3 * i))
          + LongMath.pow(3, Integer.bitCount(2 * i))
          + Integer.bitCount(i + 1));
      if (i >= 4) {
        sum += bf(ImmutableList.copyOf(sides));
      }
    }
    return sum;
  }

  private long bf(List<Long> sides) {
    BigInteger max = BigInteger.ZERO;
    long maxP = 0;
    for (int ai = 0; ai < sides.size(); ai++) {
      long a = sides.get(ai);
      for (int bi = ai + 1; bi < sides.size(); bi++) {
        long b = sides.get(bi);
        for (int ci = bi + 1; ci < sides.size(); ci++) {
          long c = sides.get(ci);
          for (int di = ci + 1; di < sides.size(); di++) {
            long d = sides.get(di);
            long p = a + b + c + d;
            BigInteger x = BigInteger.valueOf(p - 2 * a)
                .multiply(BigInteger.valueOf(p - 2 * b))
                .multiply(BigInteger.valueOf(p - 2 * c))
                .multiply(BigInteger.valueOf(p - 2 * d));
            if (x.compareTo(max) > 0 || (x.compareTo(max) == 0 && p > maxP)) {
              max = x;
              maxP = p;
            }
          }
        }
      }
    }
    return maxP;
  }
}
