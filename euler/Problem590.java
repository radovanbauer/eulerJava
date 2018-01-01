package euler;

import com.google.common.collect.*;

public class Problem590 {
  public static void main(String[] args) {
    Runner.run(new Problem590()::solve);
  }

  private static final long mod = 1_000_000_000;
  private static final long modTotient = 400_000_000;
  private static final long[][] binom = new long[5134][5134];
  static {
    for (int n = 0; n < 5134; n++) {
      for (int k = 0; k <= n; k++) {
        if (k == 0 || k == n) {
          binom[n][k] = 1;
        } else {
          binom[n][k] = (binom[n - 1][k - 1] + binom[n - 1][k]) % mod;
        }
      }
    }
  }

  public long solve() {
    long n = 50_000;
    ImmutableList<Long> primes = ImmutableList.copyOf(PrimeSieve.create(n));
    Multiset<Integer> exps = HashMultiset.create();
    for (long p : primes) {
      long pPow = 1;
      int exp = 0;
      while (pPow * p <= n) {
        pPow *= p;
        exp++;
      }
      exps.add(exp);
    }
//    System.out.println("bf: " + bf(n));
    System.out.println(exps);
    return count(ImmutableSortedMultiset.copyOf(exps), HashMultiset.create(exps), 0, 0, LongMod.create(1, mod)).n();
  }

  private LongMod count(ImmutableMultiset<Integer> origExps, Multiset<Integer> newExps, int next, int changed, LongMod multiplier) {
    if (next == origExps.entrySet().size()) {
      int sign = changed % 2 == 0 ? 1 : -1;
      LongMod countAll = countAll(newExps);
      return multiplier.multiply(sign).multiply(countAll);
    }
    LongMod res = LongMod.zero(mod);
    Multiset.Entry<Integer> nextExp = origExps.entrySet().asList().get(next);
    int exp = nextExp.getElement();
    for (int expChanged = 0; expChanged <= nextExp.getCount(); expChanged++) {
      newExps.remove(exp, expChanged);
      newExps.add(exp - 1, expChanged);
      res = res.add(count(origExps, newExps, next + 1, changed + expChanged, multiplier.multiply(binom[nextExp.getCount()][expChanged])));
      newExps.add(exp, expChanged);
      newExps.remove(exp - 1, expChanged);
    }
    return res;
  }

  private LongMod countAll(Multiset<Integer> exps) {
    LongMod expProduct = LongMod.create(1, modTotient);
    for (Multiset.Entry<Integer> expEntry : exps.entrySet()) {
      expProduct = expProduct.multiply(LongMod.create(expEntry.getElement() + 1, modTotient).pow(expEntry.getCount()));
    }
    return LongMod.create(2, mod).pow(expProduct.n());
  }
}
