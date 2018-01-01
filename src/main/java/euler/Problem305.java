package euler;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

public class Problem305 {
  public static void main(String[] args) {
    Runner.run(new Problem305()::solve);
  }

  public long solve() {
    long sum = 0;
    for (int k = 1; k <= 13; k++) {
      sum += f(LongMath.pow(3, k));
    }
    return sum;
  }

  private long f(long n) {
    String nc = String.valueOf(n);
    checkArgument(!nc.matches("9*10*"), "unsupported n: %s", n);
    Set<Long> indices = new HashSet<>();

    StringBuilder smallNumbersString = new StringBuilder();
    for (long i = 1, max = LongMath.pow(10, nc.length() - 1); i < max; i++) {
      smallNumbersString.append(String.valueOf(i));
    }
    for (int i = 0; i <= smallNumbersString.length() - nc.length(); i++) {
      if (smallNumbersString.substring(i, i + nc.length()).equals(nc)) {
        indices.add(i + 1L);
        if (indices.size() == n) {
          return i + 1;
        }
      }
    }
    long prevLength = smallNumbersString.length();

    for (int d = nc.length();; d++) {
      long firstNumber = LongMath.pow(10, d - 1);

      Set<Long> newIndices = new HashSet<>();
      for (int s = 0; s <= nc.length(); s++) {
        String prefix = nc.substring(s);
        if (prefix.startsWith("0")) {
          continue;
        }
        String suffix = s == 0 ? "" : String.valueOf(Long.valueOf(nc.substring(0, s)) + 1);
        if (suffix.length() > s) {
          if (d == s) {
            continue;
          }
          suffix = suffix.substring(1);
        }
        int freeDigits = d - nc.length();
        long base = Long.valueOf(prefix + Strings.repeat("0", freeDigits) + suffix);
        long minMid = freeDigits == 0 ? 0 : prefix.isEmpty() ? LongMath.pow(10, freeDigits - 1) : 0;
        long maxMid = LongMath.pow(10, freeDigits);
        long midMultiplier = LongMath.pow(10, suffix.length());
        for (long mid = minMid; mid < maxMid; mid++) {
          long num = base + mid * midMultiplier;
          long index = prevLength + (num - firstNumber) * d - suffix.length() + 1;
          if (!indices.contains(index) && !newIndices.contains(index)) {
            newIndices.add(index);
          }
        }
      }

      for (int s = 1; s < d - nc.length(); s++) {
        long base = n * LongMath.pow(10, d - nc.length() - s);
        long prefixMin = LongMath.pow(10, s - 1);
        long prefixMax = LongMath.pow(10, s);
        long prefixMultiplier = LongMath.pow(10, d - s);
        long suffixMin = 0;
        long suffixMax = LongMath.pow(10, d - nc.length() - s);
        for (long prefix = prefixMin; prefix < prefixMax; prefix++) {
          for (long suffix = suffixMin; suffix < suffixMax; suffix++) {
            long num = base + prefix * prefixMultiplier + suffix;
            long index = prevLength + (num - firstNumber) * d + s + 1;
            if (!indices.contains(index) && !newIndices.contains(index)) {
              newIndices.add(index);
            }
          }
        }
      }

      if (indices.size() + newIndices.size() >= n) {
        return ImmutableList.sortedCopyOf(newIndices).get(Ints.checkedCast(n - indices.size() - 1));
      }
      indices.addAll(newIndices);

      prevLength += (LongMath.pow(10, d) - LongMath.pow(10, d - 1)) * d;
    }
  }
}
