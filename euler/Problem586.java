package euler;

import com.google.common.collect.ImmutableList;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.math.LongMath.checkedMultiply;

public class Problem586 {
  public static void main(String[] args) {
    Runner.run(new Problem586()::solve);
  }

  public long solve() {
    long n = LongMath.pow(10, 15);
    int r = 40;
    List<List<Integer>> expCombinations = ImmutableList.<List<Integer>>builder()
        .addAll(expCombinations(r * 2))
        .addAll(expCombinations(r * 2 + 1))
        .build();
    System.out.println(expCombinations);
    long maxPrime = maxPrime(expCombinations, n);
    long maxNeutralPrimeProduct = maxNeutralPrimeProduct(expCombinations, n);
    FactorizationSieve sieve = new FactorizationSieve(Math.max(maxPrime, maxNeutralPrimeProduct));
    long[] goodPrimes = sieve.getAllPrimes().stream().filter(p1 -> p1 % 5 == 1 || p1 % 5 == 4).mapToLong(l -> l).toArray();

    long[] neutralPrimeCount = new long[Ints.checkedCast(maxNeutralPrimeProduct + 1)];
    for (int i = 1; i <= maxNeutralPrimeProduct; i++) {
      neutralPrimeCount[i] = neutralPrimeCount[i - 1];
      if (sieve.primeDivisors(i).stream().allMatch(p -> p % 5 == 2 || p % 5 == 3)) {
        neutralPrimeCount[i]++;
      }
    }

    long res = 0;
    for (List<Integer> exps : expCombinations) {
      res += count(exps, 0, new ArrayDeque<>(), goodPrimes, 1, n, neutralPrimeCount);
    }
    return res;
  }

  private long count(List<Integer> exps, int nextExp, ArrayDeque<Long> usedPrimes, long[] goodPrimes, long product, long maxProduct, long[] neutralPrimeCount) {
    if (nextExp == exps.size()) {
      long productPow5 = product;
      long res = 0;
      while (productPow5 <= maxProduct) {
        res += neutralPrimeCount[IntMath.sqrt(Ints.checkedCast(maxProduct / productPow5), RoundingMode.FLOOR)];
        productPow5 *= 5;
      }
      return res;
    } else {
      long res = 0;
      for (long prime : goodPrimes) {
        long newProduct;
        try {
          newProduct = checkedMultiply(product, LongMath.checkedPow(prime, exps.get(nextExp)));
        } catch (ArithmeticException e) {
          break;
        }
        if (newProduct > maxProduct) {
          break;
        }
        if (nextExp > 0 && exps.get(nextExp).intValue() == exps.get(nextExp - 1).intValue() && prime < usedPrimes.getLast()) {
          continue;
        }
        if (usedPrimes.contains(prime)) {
          continue;
        }
        if (nextExp == 0) {
          System.out.println(prime);
        }
        usedPrimes.addLast(prime);
        res += count(exps, nextExp + 1, usedPrimes, goodPrimes, newProduct, maxProduct, neutralPrimeCount);
        usedPrimes.removeLast();
      }
      return res;
    }
  }

  private long maxNeutralPrimeProduct(List<List<Integer>> expCombinations, long n) {
    List<Long> firstGoodPrimes = getFirstGoodPrimes(expCombinations.stream().mapToInt(List::size).max().getAsInt());
    long res = 0L;
    for (List<Integer> exps : expCombinations) {
      res = Math.max(res, BigInteger.valueOf(n).divide(product(firstGoodPrimes, exps)).longValueExact());
    }
    return LongMath.sqrt(res, RoundingMode.FLOOR);
  }

  private long maxPrime(List<List<Integer>> expCombinations, long n) {
    List<Long> firstGoodPrimes = getFirstGoodPrimes(expCombinations.stream().mapToInt(List::size).max().getAsInt());
    long res = 0L;
    for (List<Integer> exps : expCombinations) {
      BigInteger product = BigInteger.valueOf(n).divide(product(firstGoodPrimes, exps.subList(0, exps.size() - 1)));
      BigInteger lo = BigInteger.ZERO;
      BigInteger hi = product.add(BigInteger.ONE);
      while (hi.subtract(lo).compareTo(BigInteger.ONE) > 0) {
        BigInteger mid = lo.add(hi).divide(BigInteger.valueOf(2));
        if (mid.pow(exps.get(exps.size() - 1)).compareTo(product) <= 0) {
          lo = mid;
        } else {
          hi = mid;
        }
      }
      res = Math.max(res, lo.longValueExact());
    }
    return res;
  }

  private BigInteger product(List<Long> firstGoodPrimes, List<Integer> exps) {
    BigInteger res = BigInteger.ONE;
    for (int i = 0; i < exps.size(); i++) {
      res = res.multiply(BigInteger.valueOf(firstGoodPrimes.get(i)).pow(exps.get(i)));
    }
    return res;
  }

  private List<Long> getFirstGoodPrimes(int k) {
    long n = 10;
    List<Long> result;
    while ((result = new FactorizationSieve(n).getAllPrimes().stream()
        .filter(p -> p % 5 == 1 || p % 5 == 4)
        .limit(k)
        .collect(toImmutableList()))
        .size() < k) {
      n *= 2;
    }
    return result;
  }

  private List<List<Integer>> expCombinations(int k) {
    FactorizationSieve sieve = new FactorizationSieve(k);
    ArrayList<List<Integer>> result = new ArrayList<>();
    expCombinations(k, k, sieve, new ArrayDeque<>(), result);
    return result;
  }

  private void expCombinations(int k, int maxDiv, FactorizationSieve sieve, ArrayDeque<Integer> combination, List<List<Integer>> result) {
    if (k == 1) {
      result.add(ImmutableList.copyOf(combination));
    } else {
      for (int div : sieve.divisors(k)) {
        if (div <= maxDiv && div > 1) {
          combination.addLast(div - 1);
          expCombinations(k / div, div, sieve, combination, result);
          combination.removeLast();
        }
      }
    }
  }
}
