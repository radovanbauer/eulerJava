package euler;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Problem167 {

  public static void main(String[] args) {
    System.out.println(new Problem167().solve(100000000000L));
  }

  public long solve(long k) {
    long res = 0L;
    for (int n = 2; n <= 10; n++) {
      UlamGenerator ulamGenerator = new UlamGenerator(2, 2 * n + 1);
      int[] result = findPeriod(diff(ulamGenerator));
      int period = result[0];
      int first = result[1] + 1;
      List<Integer> elems =
          ImmutableList.copyOf(Iterables.limit(ulamGenerator, first + period + 1));
      long sum = 0L;
      for (int i = first; i < first + period; i++) {
        sum += elems.get(i + 1) - elems.get(i);
      }
      res += sum * ((k - first) / period)
          + elems.get(first + (int) ((k - first) % period) - 1);
    }
    return res;
  }

  private int[] findPeriod(Iterable<Integer> iterable) {
    Iterator<Integer> iterator = iterable.iterator();
    List<Integer> elems = Lists.newArrayList();
    for (int p = 1;; p++) {
      int len = Math.max(1000, p / 10);
      while (elems.size() < len + p) {
        elems.add(iterator.next());
      }
      int common = commonSuffixSize(elems.subList(0, len), elems.subList(p, p + len));
      if (common >= len * 9 / 10) {
        return new int[] {p, len - common};
      }
    }
  }

  private int commonSuffixSize(List<Integer> a, List<Integer> b) {
    int cnt = 0;
    int i = a.size() - 1;
    int j = b.size() - 1;
    while (i >= 0 && j >= 0 && a.get(i).intValue() == b.get(j).intValue()) {
      cnt++;
      i--;
      j--;
    }
    return cnt;
  }

  private Iterable<Integer> diff(final Iterable<Integer> iterable) {
    return new Iterable<Integer>() {
      @Override
      public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
          private final Iterator<Integer> originalIterator = iterable.iterator();
          private int last = originalIterator.next();

          @Override
          public boolean hasNext() {
            return originalIterator.hasNext();
          }

          @Override
          public Integer next() {
            Integer next = originalIterator.next();
            int res = next - last;
            last = next;
            return res;
          }

          @Override
          public void remove() {
            throw new UnsupportedOperationException();
          }
        };
      }
    };
  }

  private static class UlamGenerator implements Iterable<Integer> {
    private final int a, b;

    public UlamGenerator(int a, int b) {
      this.a = a;
      this.b = b;
    }

    @Override
    public Iterator<Integer> iterator() {
      return Iterators.concat(Arrays.asList(a, b).iterator(), new AbstractIterator<Integer>() {
        private final List<Integer> numbers = Lists.newArrayList(a, b);
        private int even = -1;
        private int a2, aEven;

        @Override
        protected Integer computeNext() {
          if (even == -1) {
            int size = numbers.size();
            int max = numbers.get(size - 1);
            Map<Integer, Integer> counts = Maps.newHashMap();
            for (int i = 0; i < size; i++) {
              for (int j = size - 1; j > i; j--) {
                int sum = numbers.get(i) + numbers.get(j);
                if (sum <= max) {
                  break;
                }
                Integer oldCount = counts.get(sum);
                if (oldCount == null) {
                  oldCount = 0;
                }
                counts.put(sum, oldCount + 1);
              }
            }
            for (int k = max + 1;; k++) {
              Integer count = counts.get(k);
              if (count != null && count == 1) {
                numbers.add(k);
                if (k % 2 == 0) {
                  even = k;
                  while (numbers.get(a2) + 2 <= even) {
                    a2++;
                  }
                  while (numbers.get(aEven) + even <= even) {
                    aEven++;
                  }
                }
                break;
              }
            }
          } else {
            while (true) {
              if (numbers.get(aEven) == even) {
                aEven++;
              }
              Integer next2 = a2 < numbers.size() ? numbers.get(a2) + 2 : null;
              Integer nextEven = aEven < numbers.size() ? numbers.get(aEven) + even : null;
              if (next2 == null || nextEven < next2) {
                checkNotNull(nextEven);
                numbers.add(nextEven);
                aEven++;
                break;
              } else if (next2 < nextEven) {
                checkNotNull(next2);
                numbers.add(next2);
                a2++;
                break;
              } else {
                checkNotNull(next2);
                checkNotNull(nextEven);
                a2++;
                aEven++;
              }
            }
          }
          return numbers.get(numbers.size() - 1);
        }
      });
    }
  }
}
