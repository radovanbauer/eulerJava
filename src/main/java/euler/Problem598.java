package euler;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;

import static euler.Runner.run;

public class Problem598 {
  public static void main(String[] args) {
    run(new Problem598()::solve);
  }

  public long solve() {
    int n = 100;
    FactorizationSieve sieve = new FactorizationSieve(n);
    ImmutableList<Long> primes = sieve.getAllPrimes();
    int[] coefs = new int[primes.size()];
    for (int i = 1; i <= n; i++) {
      Factorization factorization = sieve.factorization(i);
      for (int p = 0; p < primes.size(); p++) {
        coefs[p] += factorization.getExponent(primes.get(p));
      }
    }
    int[] coefs1 = new int[(coefs.length + 1) / 2];
    int[] coefs2 = new int[coefs.length / 2];
    for (int i = 0; i < coefs.length; i++) {
      if (i % 2 == 0) {
        coefs1[i / 2] = coefs[i];
      } else {
        coefs2[i / 2] = coefs[i];
      }
    }
    HashMultiset<LongFraction> products1 = HashMultiset.create();
    generate(coefs1, 0, LongFraction.ONE, products1);
    HashMultiset<LongFraction> products2 = HashMultiset.create();
    generate(coefs2, 0, LongFraction.ONE, products2);

    long sum = 0;

    for (Multiset.Entry<LongFraction> entry : products1.entrySet()) {
      LongFraction product = entry.getElement();
      sum += (long) entry.getCount() * products2.count(product);
    }

    return sum / 2;
  }

  private void generate(int[] coefs, int pos, LongFraction product, Multiset<LongFraction> result) {
    if (pos == coefs.length) {
      result.add(product);
    } else {
      for (int a = 0; a <= coefs[pos]; a++) {
        int b = coefs[pos] - a;
        generate(coefs, pos + 1, product.multiply(LongFraction.create(a + 1, b + 1)), result);
      }
    }
  }
}
