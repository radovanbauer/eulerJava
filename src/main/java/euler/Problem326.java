package euler;

import com.google.common.math.IntMath;
import com.google.common.math.LongMath;

import java.math.BigInteger;

import static com.google.common.math.LongMath.checkedMultiply;

public class Problem326 {
  public static void main(String[] args) {
    Runner.run(new Problem326()::solve);
  }

  public long solve() {
    long N = LongMath.pow(10, 12);
    int M = IntMath.pow(10, 6);
    long a = 1;
    BigInteger sumka = BigInteger.ONE;
    int suma = 1;
    int period = M * 6;
    int[] sumaStart = new int[period + 1];
    sumaStart[1] = 1;
    for (int n = 2; n <= period; n++) {
      a = sumka.mod(BigInteger.valueOf(n)).longValueExact();
      sumka = sumka.add(BigInteger.valueOf(n).multiply(BigInteger.valueOf(a)));
      suma = LongMath.mod(suma + a, M);
      sumaStart[n] = suma;
    }

    long[] counts = new long[M];
    counts[0] = 1;
    for (int n = 1; n <= period; n++) {
      counts[sumaStart[n]] += N / period + (n <= N % period ? 1 : 0);
    }

    long res = 0;
    for (long count : counts) {
      res += checkedMultiply(count, count - 1) / 2;
    }
    return res;
  }
}
