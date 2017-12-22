package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.math.LongMath.checkedMultiply;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

import com.google.common.primitives.Ints;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

public class Problem435 {

  public static void main(String[] args) {
    System.out.println(new Problem435().solve());
  }

  public BigInteger solve() {
    long n = 1_000_000_000_000_000L;
    long[] mods = new long[] {2048, 729, 125, 49, 11, 13};
    long[] rems = new long[mods.length];
    for (int i = 0; i < mods.length; i++) {
      long mod = mods[i];
      rems[i] = LongStream.rangeClosed(0, 100)
          .mapToObj(x -> sum(n, x, mod))
          .reduce(LongMod.zero(mod), (l1, l2) -> l1.add(l2)).n();
    }
    return chineseRem(rems, mods).n();
  }

  private BigMod chineseRem(long[] rems, long[] mods) {
    checkArgument(rems.length == mods.length && rems.length > 0);
    long N = 1L;
    for (long mod : mods) {
      N = checkedMultiply(N, mod);
    }
    BigMod res = BigMod.zero(N);
    for (int i = 0; i < rems.length; i++) {
      res = res.add(BigMod.create(rems[i], N).multiply(N / mods[i])
          .multiply(LongMod.create(N / mods[i], mods[i]).invert().n()));
    }
    return res;
  }

  private LongMod sum(long n, long x, long mod) {
    Map<ImmutableList<LongMod>, Integer> seen = new HashMap<>();
    List<LongMod> sequence = new ArrayList<>();
    PeekingIterator<LongMod> iter = Iterators.peekingIterator(f(x, mod).iterator());
    while (true) {
      LongMod f1 = iter.next();
      LongMod f2 = iter.peek();
      if (seen.containsKey(ImmutableList.of(f1, f2))) {
        int start = seen.get(ImmutableList.of(f1, f2));
        int period = sequence.size() - start;
        LongMod periodSum = sequence.subList(start, start + period).stream()
            .reduce(LongMod.zero(mod), (l1, l2) -> l1.add(l2));
        LongMod startSum = sequence.subList(0, start).stream()
            .reduce(LongMod.zero(mod), (l1, l2) -> l1.add(l2));
        LongMod endSum = sequence.subList(start, start + Ints.checkedCast((n - start) % period)).stream()
            .reduce(LongMod.zero(mod), (l1, l2) -> l1.add(l2));
        return startSum.add(endSum).add(periodSum.multiply((n - start) / period));
      }
      sequence.add(f1);
      seen.put(ImmutableList.of(f1, f2), sequence.size() - 1);
    }
  }

  private Iterable<LongMod> f(long x, long mod) {
    return new Iterable<LongMod>() {
      @Override
      public Iterator<LongMod> iterator() {
        return new AbstractIterator<LongMod>() {
          private LongMod fib1 = LongMod.create(1, mod);
          private LongMod fib2 = LongMod.create(0, mod);
          private LongMod xPow = LongMod.create(1, mod);

          @Override
          protected LongMod computeNext() {
            LongMod fib = fib1.add(fib2);
            fib1 = fib2;
            fib2 = fib;
            xPow = xPow.multiply(x);
            return fib.multiply(xPow);
          }
        };
      }
    };
  }
}
