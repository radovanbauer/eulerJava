package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

public class Problem548 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem548().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long max = 10_000_000_000_000_000L;
    long[] primes = Longs.toArray(new FactorizationSieve(1000).getAllPrimes());
    return solve(max, new int[0], primes, 1);
  }

  private long solve(long max, int[] exp, long[] primes, long minProduct) {
    checkArgument(exp.length <= primes.length);
    long sum = 0;
    BigInteger g = gozintaChains(exp);
    if (g.compareTo(BigInteger.valueOf(max)) <= 0
        && Arrays.equals(normalize(factorizationExponents(g.longValueExact())), normalize(exp))) {
      sum += g.longValueExact();
      System.out.println(g);
    }
    int[] newExp = new int[exp.length + 1];
    System.arraycopy(exp, 0, newExp, 0, exp.length);
    long prime = primes[exp.length];
    long newMinProduct = minProduct;
    for (int e = 1; (exp.length == 0 || exp[exp.length - 1] >= e) && max / prime >= newMinProduct; e++) {
      newMinProduct *= prime;
      newExp[newExp.length - 1] = e;
      sum += solve(max, newExp, primes, newMinProduct);
    }
    return sum;
  }

  private int[] factorizationExponents(long n) {
    List<Integer> res = new ArrayList<>();
    for (long d = 2; d * d <= n; d++) {
      int exp = 0;
      while (n % d == 0) {
        n /= d;
        exp++;
      }
      if (exp > 0) {
        res.add(exp);
      }
    }
    if (n > 1) {
      res.add(1);
    }
    return Ints.toArray(res);
  }

  private final Map<Exponents, BigInteger> cache = new HashMap<>();

  private BigInteger gozintaChains(int[] exp) {
    exp = normalize(exp);
    if (exp.length == 0) {
      return BigInteger.ONE;
    }
    Exponents key = Exponents.create(exp);
    if (cache.containsKey(key)) {
      return cache.get(key);
    }
    BigInteger result = BigInteger.ZERO;
    int[] smallerExp = new int[exp.length];
    while (!Arrays.equals(smallerExp, exp)) {
      result = result.add(gozintaChains(smallerExp));
      advance(smallerExp, exp);
    }
    cache.put(key, result);
    return result;
  }

  private int[] normalize(int[] exp) {
    int zeroCount = 0;
    for (int i : exp) {
      if (i == 0) {
        zeroCount++;
      }
    }
    int[] res = new int[exp.length - zeroCount];
    int idx = 0;
    for (int i : exp) {
      if (i != 0) {
        res[idx++] = i;
      }
    }
    Arrays.sort(res);
    return res;
  }

  private void advance(int[] arr, int[] max) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] < max[i]) {
        arr[i]++;
        return;
      } else {
        arr[i] = 0;
      }
    }
    throw new IllegalArgumentException();
  }

  @AutoValue
  static abstract class Exponents {
    @SuppressWarnings("mutable")
    abstract int[] exp();
    static Exponents create(int[] exp) {
      return new AutoValue_Problem548_Exponents(exp.clone());
    }
  }
}
