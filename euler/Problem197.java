package euler;

import static java.math.RoundingMode.FLOOR;

import java.util.Iterator;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import com.google.common.math.DoubleMath;

public class Problem197 {

  public static void main(String[] args) {
    System.out.println(new Problem197().solve(1000000000000L));
  }

  public long solve(long n) {
    int[] offsetAndPeriod = floyd(sequence());
    int offset = offsetAndPeriod[0];
    int period = offsetAndPeriod[1];
    return Iterables.get(sequence(), (int) (offset + ((n - offset) % period)))
        + Iterables.get(sequence(), (int) (offset + ((n - offset) % period)) + 1);
  }

  private int[] floyd(Iterable<Long> iterable) {
    Iterator<Long> i1 = iterable.iterator();
    Iterator<Long> i2 = iterable.iterator();

    i1.next();
    long l1 = i1.next();
    i2.next();
    i2.next();
    long l2 = i2.next();
    while (l1 != l2) {
      l1 = i1.next();
      i2.next();
      l2 = i2.next();
    }

    int offset = 0;
    i1 = iterable.iterator();
    l1 = i1.next();
    while (l1 != l2) {
      l1 = i1.next();
      l2 = i2.next();
      offset++;
    }

    int period = 1;
    l2 = i2.next();
    while (l1 != l2) {
      l2 = i2.next();
      period++;
    }

    return new int[] { offset, period };
  }

  private Iterable<Long> sequence() {
    return new Iterable<Long>() {
      @Override
      public Iterator<Long> iterator() {
        return new AbstractIterator<Long>() {
          private long x = -1000000000L;
          @Override
          protected Long computeNext() {
            return x = DoubleMath.roundToLong(Math.pow(2D, 30.403243784D - x * x * 1e-18), FLOOR);
          }
        };
      }
    };
  }
}
