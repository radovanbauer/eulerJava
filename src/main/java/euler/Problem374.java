package euler;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem374 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem374().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long max = LongMath.pow(10, 14);
    long mod = 982451653;
    LongMod sum = LongMod.create(1 + 2 + 3 + 4, mod);
    LongMod mid = LongMod.create(5, mod);
    long n = 4;
    long l = 1;
    LongMod fact2 = LongMod.create(2, mod);
    LongMod fact3 = LongMod.create(1, mod);
    while (n + (l + 3) <= max) {
      l++;
      fact2 = fact2.multiply(l + 1);
      fact3 = fact3.multiply(l + 1);
      mid = fact2.add(mid.multiply(l + 2));
      sum = sum.add(fact3.multiply(l + 3).add(mid).multiply(l));
      n += l + 2;
    }
    if (n < max) {
      l++;
      fact2 = fact2.multiply(l + 1);
      LongMod x = fact2.multiply(l + 2);
      long missing = l + 2;
      while (n < max) {
        sum = sum.add(x.divide(missing).multiply(l));
        missing--;
        n++;
      }
    }
    return sum.n();
  }
}
