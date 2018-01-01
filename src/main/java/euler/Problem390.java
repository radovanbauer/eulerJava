package euler;

import com.google.common.math.BigIntegerMath;
import com.google.common.math.LongMath;

import java.math.BigInteger;
import java.math.RoundingMode;

import static com.google.common.math.LongMath.checkedAdd;
import static com.google.common.math.LongMath.checkedMultiply;
import static java.math.BigInteger.ONE;

public class Problem390 {
  public static void main(String[] args) {
    Runner.run(new Problem390()::solve);
  }

  public long solve() {
    BigInteger max = BigInteger.valueOf(10).pow(10);
    BigInteger maxProd = max.pow(2).multiply(BigInteger.valueOf(4));
    System.out.println(maxProd);
    long areaSum = 0;
    long maxb = LongMath.sqrt((BigIntegerMath.sqrt(maxProd, RoundingMode.FLOOR).longValueExact() - 1) / 4, RoundingMode.FLOOR);
    for (long b = 1; b <= maxb; b++) {
      long maxc =
          BigIntegerMath.sqrt(
              maxProd.divide(BigInteger.valueOf(1 + checkedMultiply(2 * b, 2 * b)))
                  .subtract(ONE)
                  .divide(BigInteger.valueOf(4)),
              RoundingMode.FLOOR).longValueExact();
      long maxclong =
          BigIntegerMath.sqrt(
            BigInteger.valueOf(Long.MAX_VALUE).divide(BigInteger.valueOf(1 + checkedMultiply(2 * b, 2 * b)))
                .subtract(ONE)
                .divide(BigInteger.valueOf(4)),
          RoundingMode.FLOOR).longValueExact();
      for (long c = b; c <= maxc; c++) {
        if (c <= maxclong) {
          long prod = checkedMultiply(checkedAdd(1, checkedMultiply(2 * b, 2 * b)), checkedAdd(1, checkedMultiply(2 * c, 2 * c)));
          long prodMinusOne = prod - 1;
          long sqrt = LongMath.sqrt(prodMinusOne, RoundingMode.FLOOR);
          if (sqrt * sqrt == prodMinusOne) {
            areaSum += sqrt / 2;
            System.out.printf("b=%d c=%d\n", 2 * b, 2 * c);
          }
        } else {
          BigInteger prod =
              BigInteger.valueOf(2 * b).pow(2).add(ONE).multiply(BigInteger.valueOf(2 * c).pow(2).add(ONE));
          BigInteger prodMinusOne = prod.subtract(ONE);
          BigInteger sqrt = BigIntegerMath.sqrt(prodMinusOne, RoundingMode.FLOOR);
          if (sqrt.pow(2).compareTo(prodMinusOne) == 0) {
            areaSum += sqrt.longValueExact() / 2;
            System.out.printf("b=%d c=%d\n", 2 * b, 2 * c);
          }
        }
      }
    }
    return areaSum;
  }
}
