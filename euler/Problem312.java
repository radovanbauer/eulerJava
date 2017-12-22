package euler;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Problem312 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem312().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long mod = LongMath.pow(13, 8);
    Period outerPeriod = findPeriod(seq(mod));
    Period innerPeriod = findPeriod(seq(outerPeriod.period()));
    int c1 = Ints.checkedCast(Iterables.get(seq(innerPeriod.period()), 10_000).c());
    if (c1 < innerPeriod.offset()) {
      c1 += innerPeriod.period();
    }
    int c2 = Ints.checkedCast(Iterables.get(seq(outerPeriod.period()), c1).c());
    if (c2 < outerPeriod.offset()) {
      c2 += outerPeriod.period();
    }
    int c3 = Ints.checkedCast(Iterables.get(seq(mod), c2).c());
    return c3;
  }

  @AutoValue
  static abstract class Period {
    abstract int period();
    abstract int offset();

    static Period create(int period, int offset) {
      return new AutoValue_Problem312_Period(period, offset);
    }
  }

  static <T> Period findPeriod(Iterable<T> iterable) {
    PeekingIterator<T> slow = Iterators.peekingIterator(iterable.iterator());
    PeekingIterator<T> fast = Iterators.peekingIterator(iterable.iterator());
    slow.next();
    fast.next(); fast.next();

    while (!slow.peek().equals(fast.peek())) {
      slow.next();
      fast.next(); fast.next();
    }

    int offset = 0;
    slow = Iterators.peekingIterator(iterable.iterator());
    while (!slow.peek().equals(fast.peek())) {
      slow.next();
      fast.next();
      offset++;
    }

    int period = 1;
    fast.next();
    while (!slow.peek().equals(fast.peek())) {
      fast.next();
      period++;
    }

    return Period.create(period, offset);
  }

  private Iterable<Seq> seq(long mod) {
    return new Iterable<Seq>() {
      @Override
      public Iterator<Seq> iterator() {
        return new AbstractIterator<Problem312.Seq>() {
          private int idx = 0;
          private Seq prev = null;

          @Override
          protected Seq computeNext() {
            Seq res = null;
            if (idx == 0) {
              res = Seq.create(0, 0, 0);
            } else if (idx == 1) {
              res = Seq.create(1, 1, 1);
            } else if (idx == 2) {
              res = Seq.create(1, 2, 3);
            } else {
              res = Seq.create(
                  prev.d() * prev.d() % mod * prev.d() % mod,
                  2 * prev.d() * prev.d() % mod * prev.e() % mod,
                  2 * prev.e() * prev.e() % mod * prev.d() % mod);
            }
            prev = res;
            idx++;
            return res;
          }
        };
      }
    };
  }

  @AutoValue
  static abstract class Seq {
    abstract long c();
    abstract long d();
    abstract long e();

    static Seq create(long c, long d, long e) {
      return new AutoValue_Problem312_Seq(c, d, e);
    }
  }
}
