package euler;

import com.google.common.math.LongMath;

import java.util.Arrays;
import java.util.Collections;

public class Problem444 {
  public static void main(String[] args) {
    Runner.run(new Problem444()::solve);
  }

  public double solve() {
    long n = LongMath.pow(10, 14);
    int k = 20;
    long fact20 = LongMath.factorial(k);
    double sum = 0;
    for (long i = 1; i <= n; i++) {
      double prod = 1;
      for (int j = 1; j <= k; j++) {
        prod *= n - i + k - (j - 1);
      }
      sum += prod / (1D * fact20 * i);
      if ((i & 0xFFFFFF) == 0) {
        System.out.println(i + ": " + sum);
      }
    }
    return sum;
  }

  private BigFraction E(long n) {
    if (n == 1) {
      return BigFraction.create(1);
    }
    BigFraction En1 = E(n - 1);
    return (En1.add(1).divide(n))
        .add(En1.multiply(BigFraction.create(n - 1, n)));
  }

  private LongFraction X(long numbers) {
    int n = Long.bitCount(numbers);
    if (n == 1) {
      return LongFraction.create(Long.numberOfTrailingZeros(numbers));
    }
    LongFraction result = LongFraction.ZERO;
    int max = 63 - Long.numberOfLeadingZeros(numbers);
    for (int num = 1; num <= max; num++) {
      if ((numbers & (1L << num)) != 0) {
        long numbersWithoutNum = numbers ^ (1L << num);
        LongFraction xWithoutNum = X(numbersWithoutNum);
        result = result.add(Collections.min(Arrays.asList(xWithoutNum, LongFraction.create(num))));
      }
    }
    return result.divide(n);
  }
}
