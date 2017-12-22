package euler;

import static com.google.common.math.LongMath.checkedAdd;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem259 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem259().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    Set<LongFraction> results = solve(1, 9);
    long sum = 0;
    for (LongFraction result : results) {
      if (result.d() == 1 && result.n() > 0) {
        sum = checkedAdd(sum, result.n());
      }
    }
    return sum;
  }

  private Set<LongFraction> solve(int lo, int hi) {
    HashSet<LongFraction> result = new HashSet<>();
    long concat = 0L;
    for (long i = lo; i <= hi; i++) {
      concat *= 10;
      concat += i;
    }
    result.add(LongFraction.create(concat));
    for (int split = lo + 1; split <= hi; split++) {
      Set<LongFraction> left = solve(lo, split - 1);
      Set<LongFraction> right = solve(split, hi);
      for (LongFraction l : left) {
        for (LongFraction r : right) {
          result.add(l.add(r));
          result.add(l.subtract(r));
          result.add(l.multiply(r));
          if (!r.isZero()) {
            result.add(l.divide(r));
          }
        }
      }
    }
    return result;
  }
}
