package euler;

import com.google.common.math.LongMath;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.LongStream;

public class Problem342 {
  public static void main(String[] args) {
    Runner.run(new Problem342()::solve);
  }

  public long solve() {
    long n = LongMath.pow(10, 10) - 1;

    Set<BigInteger> cubes = new HashSet<>();
    BigInteger maxCube = BigInteger.valueOf(n).pow(2);
    {
      BigInteger i = BigInteger.ONE;
      BigInteger i3 = i.pow(3);
      while (i3.compareTo(maxCube) <= 0) {
        cubes.add(i3);
        i = i.add(BigInteger.ONE);
        i3 = i.pow(3);
      }
    }

    long segment = 500_000_000;
    long sum = 0;
    for (long start = 2, end = Math.min(start + segment - 1, n); start <= n; start += segment, end += segment) {
      TotientSieve sieve = new TotientSieve(start, end);
      sum += LongStream.rangeClosed(start, end).parallel().filter(i -> {
        long totient = sieve.totient(i);
        BigInteger totient2 = BigInteger.valueOf(i).multiply(BigInteger.valueOf(totient));
        if (cubes.contains(totient2)) {
          System.out.println(i + ": " + totient2);
          return true;
        } else {
          return false;
        }
      }).sum();
    }
    return sum;
  }
}
