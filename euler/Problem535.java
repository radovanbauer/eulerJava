package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Problem535 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    Problem535 problem = new Problem535();
    System.out.println(problem.solve(20));
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
//    System.out.println(problem.solve2(1000000000));
  }

  public long solve(long max) {
    long[] s = bf(1_000_000_000);
    s[1] = 1;
    s[2] = 1;
    long b = 3;
    long bNext = 2;
    long bNextSqrt = 1;
    long c = 5;
    long cNext = 3;
    long mod = 1_000_000_000;
    LongMod sum = LongMod.create(5, mod);
    for (long a = 2; c <= max; a++) {
      if ((a & 0xFFFF) == 0) {
        System.out.println(c);
      }
      long aSqrt = LongMath.sqrt(s[Ints.checkedCast(a)], RoundingMode.FLOOR);

      if (c + aSqrt * (LongMath.sqrt(bNext + aSqrt - 1, RoundingMode.FLOOR) + 1) <= max + 1) {
        long bNextStart = bNext;
        while (bNext < bNextStart + aSqrt) {
          long bNextAdvance = Math.min((bNextSqrt + 1) * (bNextSqrt + 1) - bNext, bNextStart + aSqrt - bNext);
          sum = sum.add(multiplyDiv2(cNext + cNext + bNextAdvance * bNextSqrt - 1, bNextAdvance * bNextSqrt, mod));
          sum = sum.add(multiplyDiv2(bNext + bNext + bNextAdvance - 1, bNextAdvance, mod));
          c += bNextAdvance * (bNextSqrt + 1);
          cNext += bNextAdvance * bNextSqrt;
  
          bNextSqrt++;
          bNext += bNextAdvance;
          b += bNextAdvance;
        }
      } else {
        for (int i = 0; i < aSqrt; i++) {
          long cAdvance = Math.max(Math.min(LongMath.sqrt(bNext, RoundingMode.FLOOR), max + 1 - c), 0);
          c += cAdvance;
          sum = sum.add(multiplyDiv2(cNext + cNext + cAdvance - 1, cAdvance, mod));
          cNext += cAdvance;
  
          if (c <= max) {
            c++;
            sum = sum.add(bNext);
          }
  
          bNext++;
          b++;
        }
      }

      long cAdvance = Math.max(Math.min(LongMath.sqrt(s[Ints.checkedCast(a)], RoundingMode.FLOOR), max + 1 - c), 0);
      c += cAdvance;
      sum = sum.add(multiplyDiv2(cNext + cNext + cAdvance - 1, cAdvance, mod));
      cNext += cAdvance;

      if (c <= max) {
        c++;
        sum = sum.add(s[Ints.checkedCast(a)]);
      }

      b++;
    }
    return sum.n();
  }

  private long solve2(int max) {
    return Arrays.stream(bf(max)).sum();
  }

  private long[] bf(int max) {
    long[] s = new long[max + 1];
    s[1] = 1;
    s[2] = 1;
    long b = 3;
    long bNext = 2;
    for (long a = 2; b <= max; a++) {
      long aSqrt = LongMath.sqrt(s[Ints.checkedCast(a)], RoundingMode.FLOOR);
      long bStart = b;
      for (int i = 0; i < aSqrt; i++) {
        if (bStart + i < s.length) {
          s[Ints.checkedCast(bStart + i)] = Ints.checkedCast(bNext);
        }

        bNext++;
        b++;
      }
      if (b < s.length) {
        s[Ints.checkedCast(b)] = s[Ints.checkedCast(a)];
      }

      b++;
    }
    return s;
  }

  private LongMod multiplyDiv2(long a, long b, long mod) {
    if (a % 2 == 0) {
      return LongMod.create(a / 2, mod).multiply(b);
    } else {
      checkArgument(b % 2 == 0);
      return LongMod.create(b / 2, mod).multiply(a);
    }
  }
}
