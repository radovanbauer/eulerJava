package euler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.TreeMultiset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static euler.Runner.run;

public class Problem593 {

  public static void main(String[] args) {
    run(new Problem593()::solve);
  }

  public String solve() {
    int n = 10_000_000;
    int k = 100_000;
    ImmutableList<Long> primes = new SmallFactorizationSieve(200_000_000).getAllPrimes();
    checkState(primes.size() >= n);
    long[] S = new long[n + 1];
    long[] S2 = new long[n + 1];
    for (int i = 1; i <= n; i++) {
      S[i] = LongMod.create(primes.get(i - 1), 10007).pow(i).n();
      S2[i] = S[i] + S[i / 10000 + 1];
    }

    long sum = 0;
    TwoSet twoSet = new TwoSet();
    for (int i = 0; i < k; i++) {
      twoSet.add(S2[i]);
    }
    for (int i = 1; i <= n - k + 1; i++) {
      twoSet.add(S2[i + k - 1]);
      twoSet.remove(S2[i - 1]);
      sum += twoSet.median2();
    }
    return String.format("%.1f\n", BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(2L)));
  }

  private static class TwoSet {
    private final TreeMultiset<Long> low = TreeMultiset.create();
    private final TreeMultiset<Long> high = TreeMultiset.create();

    public void add(long x) {
      List<Long> elems = new ArrayList<>();
      elems.add(x);
      if (!low.isEmpty()) {
        long lowLast = low.lastEntry().getElement();
        low.remove(lowLast);
        elems.add(lowLast);
      }
      if (!high.isEmpty()) {
        long highFirst = high.firstEntry().getElement();
        high.remove(highFirst);
        elems.add(highFirst);
      }
      Collections.sort(elems);

      while (!elems.isEmpty()) {
        if (low.size() <= high.size()) {
          low.add(elems.get(0));
          elems = elems.subList(1, elems.size());
        } else {
          high.add(elems.get(elems.size() - 1));
          elems = elems.subList(0, elems.size() - 1);
        }
      }

      checkState(low.isEmpty() || high.isEmpty() || low.lastEntry().getElement() <= high.firstEntry().getElement());
      checkState(Math.abs(low.size() - high.size()) <= 1);
    }

    public void remove(long x) {
      if (low.remove(x)) {
        if (low.size() < high.size()) {
          long highFirst = high.firstEntry().getElement();
          high.remove(highFirst);
          low.add(highFirst);
        }
      } else if (high.remove(x)) {
        if (high.size() < low.size() - 1) {
          long lowLast = low.lastEntry().getElement();
          low.remove(lowLast);
          high.add(lowLast);
        }
      }
    }

    public long median2() {
      if (low.size() == high.size()) {
        return low.lastEntry().getElement() + high.firstEntry().getElement();
      } else {
        checkState(low.size() > high.size());
        return low.lastEntry().getElement() * 2;
      }
    }

    @Override
    public String toString() {
      return String.format("low=%s, high=%s", low, high);
    }
  }
}
