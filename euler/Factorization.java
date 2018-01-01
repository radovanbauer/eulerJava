package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.*;
import java.util.Map.Entry;

import com.google.auto.value.AutoValue;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.math.LongMath;

public class Factorization {
  @AutoValue
  public abstract static class PrimeFactor {
    public abstract long prime();
    public abstract int exp();
  
    public static PrimeFactor create(long prime, int exp) {
      checkArgument(prime > 0);
      checkArgument(exp > 0);
      return new AutoValue_Factorization_PrimeFactor(prime, exp);
    }
  }

  private final ImmutableSortedMap<Long, Integer> primeExponents;

  private Factorization(ImmutableSortedMap<Long, Integer> primeExponents) {
    this.primeExponents = primeExponents;
  }

  public List<PrimeFactor> factors() {
    return Lists.transform(primeExponents.entrySet().asList(),
        new Function<Map.Entry<Long, Integer>, PrimeFactor>() {
      @Override
      public PrimeFactor apply(Entry<Long, Integer> input) {
        return PrimeFactor.create(input.getKey(), input.getValue());
      }
    });
  }

  public static Factorization create(Map<Long, Integer> factors) {
    for (Map.Entry<Long, Integer> entry : factors.entrySet()) {
      checkArgument(entry.getKey() >= 2);
      checkArgument(entry.getValue() >= 0);
    }
    return new Factorization(ImmutableSortedMap.copyOf(factors));
  }

  public static Factorization create(Iterable<PrimeFactor> factors) {
    Map<Long, Integer> factorMap = new HashMap<>();
    for (PrimeFactor factor : factors) {
      checkArgument(!factorMap.containsKey(factor.prime()));
      factorMap.put(factor.prime(), factor.exp());
    }
    return create(factorMap);
  }

  public static Factorization create(PrimeFactor... factors) {
    return create(Arrays.asList(factors));
  }

  public ImmutableSet<Long> getPrimes() {
    return primeExponents.keySet();
  }

  public int getExponent(long prime) {
    if (prime > Integer.MAX_VALUE) {
      return 0;
    }
    Integer res = primeExponents.get(prime);
    return res == null ? 0 : res;
  }

  public Factorization multiply(Factorization that) {
    return product(ImmutableList.of(this, that));
  }

  public Factorization divide(Factorization that) {
    Map<Long, Integer> newFactors = new HashMap<>(primeExponents);
    for (Map.Entry<Long, Integer> entry : that.primeExponents.entrySet()) {
      long prime = entry.getKey();
      int exp = entry.getValue();
      int currentExp = newFactors.getOrDefault(prime, 0);
      checkArgument(currentExp - exp >= 0, "Cannot divide %s by %s", this, that);
      if (currentExp - exp == 0) {
        newFactors.remove(prime);
      } else {
        newFactors.put(prime, currentExp - exp);
      }
    }
    return create(newFactors);
  }

  public static Factorization product(Iterable<Factorization> factorizations) {
    Map<Long, Integer> newFactors = new HashMap<>();
    for (Factorization factorization : factorizations) {
      for (Map.Entry<Long, Integer> entry : factorization.primeExponents.entrySet()) {
        Long prime = entry.getKey();
        Integer exp = entry.getValue();
        if (!newFactors.containsKey(prime)) {
          newFactors.put(prime, exp);
        } else {
          newFactors.put(prime, newFactors.get(prime) + exp);
        }
      }
    }
    return create(newFactors);
  }

  public static Factorization product(Factorization... factorizations) {
    return product(Arrays.asList(factorizations));
  }

  public ImmutableList<Long> unsortedDivisors() {
    ArrayList<Long> result = new ArrayList<>();
    unsortedDivisors(result, primeExponents.keySet().asList(), primeExponents.values().asList(), 1L, 0);
    return ImmutableList.copyOf(result);
  }

  private void unsortedDivisors(List<Long> result, List<Long> primes, List<Integer> exponents, long product, int next) {
    if (next == primeExponents.size()) {
      result.add(product);
    } else {
      for (int exp = 0, maxExp = exponents.get(next); exp <= maxExp; exp++) {
        if (exp > 0) {
          product = LongMath.checkedMultiply(product, primes.get(next));
        }
        unsortedDivisors(result, primes, exponents, product, next + 1);
      }
    }
  }

  @Override
  public String toString() {
    if (factors().isEmpty()) {
      return "1";
    } else {
      StringBuilder result = new StringBuilder();
      for (Map.Entry<Long, Integer> entry : primeExponents.entrySet()) {
        result.append(entry.getKey() + "^" + entry.getValue() + "*");
      }
      result.deleteCharAt(result.length() - 1);
      return result.toString();
    }
  }
}