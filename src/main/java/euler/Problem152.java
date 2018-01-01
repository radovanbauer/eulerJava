package euler;

import static com.google.common.collect.DiscreteDomain.longs;

import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

public class Problem152 {

  private ImmutableList<Long> numbers;
  private ImmutableList<Long> squares;
  private ImmutableList<Long> operands;
  private List<Long> remaining;
  private Long target;

  public static void main(String[] args) {
    System.out.println(new Problem152().solve());
  }

  public int solve() {
    numbers = FluentIterable.from(ContiguousSet.create(Range.closed(2L, 80L), longs()))
        .filter(new Predicate<Long>() {
          @Override public boolean apply(Long n) {
            return ImmutableSet.of(2L, 3L, 5L, 7L, 13L).containsAll(primeDivisors(n));
          }
        })
        .toList();
    System.out.println(numbers);
    squares = FluentIterable.from(numbers)
        .transform(new Function<Long, Long>() {
          @Override public Long apply(Long n) {
            return Long.valueOf(n * n);
          }
        })
        .toList();
    final Long lcm = lcm(squares);
    target = lcm / 2L;
    operands = FluentIterable.from(squares)
        .transform(new Function<Long, Long>() {
          @Override public Long apply(Long n) {
            return lcm / n;
          };
        })
        .toList();
    long sum = 0L;
    for (long n : operands) {
      sum += n;
    }
    System.out.printf("lcm: %d, target: %s, sum: %d\n", lcm, target, sum);
    remaining = Lists.newArrayList();
    remaining.add(sum);
    for (int i = 1; i < numbers.size(); i++) {
      sum -= operands.get(i - 1);
      remaining.add(sum);
    }
    return solve(0, 0L);
  }

  private int solve(int next, long sum) {
    if (sum == target) {
      return 1;
    }
    if (next >= operands.size()) {
      return 0;
    }
    if (sum + remaining.get(next) < target) {
      return 0;
    }
    return solve(next + 1, sum + operands.get(next)) + solve(next + 1, sum);
  }

  private Set<Long> primeDivisors(long n) {
    Set<Long> result = Sets.newHashSet();
    for (long d = 2; d <= n; d++) {
      if (n % d == 0) {
        result.add(d);
        do {
          n /= d;
        } while (n % d == 0);
      }
    }
    return result;
  }

  private long gcd(long a, long b) {
    return b == 0L ? a : gcd(b, a % b);
  }

  private Long lcm(Iterable<Long> numbers) {
    long res = 1L;
    for (Long number : numbers) {
      res = lcm(res, number);
    }
    return res;
  }

  private long lcm(long a, long b) {
    return a / gcd(a, b) * b;
  }
}
