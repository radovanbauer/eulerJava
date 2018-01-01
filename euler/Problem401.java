package euler;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem401 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem401().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    BigInteger n = BigInteger.valueOf(1_000_000_000_000_000L);
    BigInteger sum = ZERO;
    BigInteger i = ONE;
    while (i.compareTo(n) <= 0) {
      BigInteger d = n.divide(i);
      BigInteger j = n.divide(d);
      sum = sum.add(d.multiply(sumOfSquares(j).subtract(sumOfSquares(i.subtract(ONE)))));
      i = j.add(ONE);
    }
    return sum.mod(BigInteger.valueOf(1_000_000_000L)).longValueExact();
  }

  private BigInteger sumOfSquares(BigInteger n) {
    return n.multiply(n.add(ONE)).multiply(n.multiply(BigInteger.valueOf(2)).add(ONE))
        .divide(BigInteger.valueOf(6));
  }
}
