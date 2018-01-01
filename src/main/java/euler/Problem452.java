package euler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

public class Problem452 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem452().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  private static final long MOD = 1_234_567_891;

  public long solve() {
    int max = 1_000_000_000;
    FactorizationSieve sieve = new FactorizationSieve(max);
    LongMod[] binom = new LongMod[30];
    for (int i = 0; i < binom.length; i++) {
      binom[i] = binomialMod(max, i, MOD);
    }
    return IntStream.rangeClosed(2, max).parallel()
        .mapToObj(n -> count(binom, primeExponents(sieve, n)))
        .reduce(LongMod.zero(MOD), (a, b) -> a.add(b)).add(1).n();
  }

  private final Map<ImmutableList<Integer>, LongMod> cache = new ConcurrentHashMap<>();

  private LongMod count(LongMod[] binom, ImmutableList<Integer> exponents) {
    exponents = normalize(exponents);
    LongMod result = cache.get(exponents);
    if (result != null) {
      return result;
    }
    int expSum = 0;
    for (int exp : exponents) {
      expSum += exp;
    }
    result = LongMod.zero(MOD);
    for (int i = 1; i <= expSum; i++) {
      result = result.add(count(exponents, i).multiply(binom[i]));
    }
    cache.put(exponents, result);
    return result;
  }

  private LongMod binomialMod(long n, long k, long mod) {
    LongMod result = LongMod.create(1, mod);
    for (int i = 0; i < k; i++) {
      result = result.multiply(n - i).divide(i + 1);
    }
    return result;
  }

  @AutoValue
  static abstract class Key2 {
    abstract ImmutableList<Integer> exponents();
    abstract int n();

    static Key2 create(ImmutableList<Integer> exponents, int n) {
      return new AutoValue_Problem452_Key2(exponents, n);
    }
  }

  private final Map<Key2, LongMod> cache2 = new ConcurrentHashMap<>();

  private LongMod count(List<Integer> exponents, int n) {
    if (n == 1) {
      for (int exp : exponents) {
        if (exp > 0) {
          return LongMod.create(1, MOD);
        }
      }
      return LongMod.zero(MOD);
    } else {
      ImmutableList<Integer> normalizedExponents = normalize(exponents);
      Key2 key = Key2.create(normalizedExponents, n);
      LongMod result = cache2.get(key);
      if (result != null) {
        return result;
      }
      result = LongMod.create(0, MOD);
      List<Integer> newExponents = new ArrayList<>(normalizedExponents);
      while (true) {
        int k = 0;
        while (k < newExponents.size() && newExponents.get(k) == 0) {
          newExponents.set(k, normalizedExponents.get(k));
          k++;
        }
        if (k == newExponents.size()) {
          break;
        }
        newExponents.set(k, newExponents.get(k) - 1);
        result = result.add(count(newExponents, n - 1));
      }
      cache2.put(key, result);
      return result;
    }
  }

  private ImmutableList<Integer> normalize(List<Integer> exponents) {
    ArrayList<Integer> result = new ArrayList<>();
    for (int exp : exponents) {
      if (exp != 0) {
        result.add(exp);
      }
    }
    return Ordering.natural().immutableSortedCopy(result);
  }

  private ImmutableList<Integer> primeExponents(FactorizationSieve sieve, int n) {
    ImmutableList.Builder<Integer> result = ImmutableList.builder();
    while (n > 1) {
      int p = sieve.smallestPrimeDivisor(n);
      int exp = 0;
      while (n % p== 0) {
        n /= p;
        exp++;
      }
      result.add(exp);
    }
    return result.build();
  }
}
