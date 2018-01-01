package euler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.common.primitives.Ints;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;

public class Problem493 {

  public static void main(String[] args) {
    System.out.println(new Problem493().solve(20));
  }

  public BigDecimal solve(int balls) {
    BigInteger weightedTotal = BigInteger.ZERO;
    BigInteger total = BigInteger.ZERO;
    for (int c1 = 0; c1 <= 10 && c1 <= balls; c1++) {
      for (int c2 = 0; c2 <= 10 && c1 + c2 <= balls; c2++) {
        for (int c3 = 0; c3 <= 10 && c1 + c2 + c3 <= balls; c3++) {
          for (int c4 = 0; c4 <= 10 && c1 + c2 + c3 + c4 <= balls; c4++) {
            for (int c5 = 0; c5 <= 10 && c1 + c2 + c3 + c4 + c5 <= balls; c5++) {
              for (int c6 = 0; c6 <= 10 && c1 + c2 + c3 + c4 + c5 + c6 <= balls; c6++) {
                int c7 = balls - c1 - c2 - c3 - c4 - c5 - c6;
                if (c7 <= 10) {
                  BigInteger multiplier = BigInteger.ONE;
                  multiplier = multiplier.multiply(BigInteger.valueOf(LongMath.factorial(10) / LongMath.factorial(10 - c1)));
                  multiplier = multiplier.multiply(BigInteger.valueOf(LongMath.factorial(10) / LongMath.factorial(10 - c2)));
                  multiplier = multiplier.multiply(BigInteger.valueOf(LongMath.factorial(10) / LongMath.factorial(10 - c3)));
                  multiplier = multiplier.multiply(BigInteger.valueOf(LongMath.factorial(10) / LongMath.factorial(10 - c4)));
                  multiplier = multiplier.multiply(BigInteger.valueOf(LongMath.factorial(10) / LongMath.factorial(10 - c5)));
                  multiplier = multiplier.multiply(BigInteger.valueOf(LongMath.factorial(10) / LongMath.factorial(10 - c6)));
                  multiplier = multiplier.multiply(BigInteger.valueOf(LongMath.factorial(10) / LongMath.factorial(10 - c7)));
                  int[] colors = new int[] {c1, c2, c3, c4, c5, c6, c7};
                  weightedTotal = weightedTotal.add(
                      BigInteger.valueOf(nonZeroCount(colors))
                          .multiply(multiplier).multiply(BigInteger.valueOf(count(colors))));
                  total = total.add(multiplier.multiply(BigInteger.valueOf(count(colors))));
                }
              }
            }
          }
        }
      }
    }
    System.out.println(weightedTotal);
    System.out.println(total);
    return new BigDecimal(weightedTotal).divide(new BigDecimal(total), 9, RoundingMode.HALF_UP);
  }

  private long nonZeroCount(int[] arr) {
    return Arrays.stream(arr).filter(i -> i != 0).count();
  }

  private Map<Key, Long> cache = new HashMap<>();

  @AutoValue
  static abstract class Key {
    abstract ImmutableList<Integer> colors();
    public static Key create(int[] colors) {
      return new AutoValue_Problem493_Key(ImmutableList.copyOf(Ints.asList(colors)));
    }
  }

  private long count(int[] colors) {
    colors = normalize(colors);
    Key key = Key.create(colors);
    if (cache.containsKey(key)) {
      return cache.get(key);
    }
    long result = 0;
    if (Arrays.stream(colors).allMatch(i -> i == 0)) {
      result = 1;
    } else {
      for (int color = 0; color < colors.length; color++) {
        if (colors[color] > 0) {
          int[] newColors = colors.clone();
          newColors[color]--;
          result = LongMath.checkedAdd(result, count(newColors));
        }
      }
    }
    cache.put(key, result);
    return result;
  }

  private int[] normalize(int[] colors) {
    int[] result = colors.clone();
    for (int i = 0; i < colors.length; i++) {
      for (int j = i - 1; j >= 0 && colors[j + 1] < colors[j]; j--) {
        int tmp = colors[i];
        colors[i] = colors[j];
        colors[j] = tmp;
      }
    }
    return result;
  }
}
