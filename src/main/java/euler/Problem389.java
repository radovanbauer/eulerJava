package euler;

import java.util.HashMap;
import java.util.Map;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

public class Problem389 {

  public static void main(String[] args) {
    System.out.println(new Problem389().solve());
  }

  public double solve() {
    ImmutableList<Long> dices = ImmutableList.of(4L, 6L, 8L, 12L, 20L);
    double e2 = expected2(dices, 1);
    double e = expected(dices, 1);
    return e2 - e * e;
  }

  @AutoValue
  static abstract class Key1 {
    abstract ImmutableList<Long> dices();
    abstract long firstDiceThrows();
    static Key1 create(ImmutableList<Long> dices, long firstDiceThrows) {
      return new AutoValue_Problem389_Key1(dices, firstDiceThrows);
    }
  }

  private final Map<Key1, Double> cache1 = new HashMap<>();

  private double expected(ImmutableList<Long> dices, long firstDiceThrows) {
    if (dices.size() == 1) {
      double singleDiceExpected = (1D + dices.get(0)) / 2D;
      return firstDiceThrows * singleDiceExpected;
    } else {
      Key1 key = Key1.create(dices, firstDiceThrows);
      if (cache1.containsKey(key)) {
        return cache1.get(key);
      }
      double result = 0D;
      for (long firstDiceSum = 1; firstDiceSum <= firstDiceThrows * dices.get(0); firstDiceSum++) {
        result += expected(dices.subList(1, dices.size()), firstDiceSum)
            * prob(dices.get(0), firstDiceThrows, firstDiceSum);
      }
      cache1.put(key, result);
      return result;
    }
  }

  @AutoValue
  static abstract class Key2 {
    abstract ImmutableList<Long> dices();
    abstract long firstDiceThrows();
    static Key2 create(ImmutableList<Long> dices, long firstDiceThrows) {
      return new AutoValue_Problem389_Key2(dices, firstDiceThrows);
    }
  }

  private final Map<Key2, Double> cache2 = new HashMap<>();

  private double expected2(ImmutableList<Long> dices, long firstDiceThrows) {
    Long d = dices.get(0);
    if (dices.size() == 1) {
      return (1 + d) * firstDiceThrows * (3 * firstDiceThrows + 3 * d * firstDiceThrows + d - 1) / 12D;
    } else {
      Key2 key = Key2.create(dices, firstDiceThrows);
      if (cache2.containsKey(key)) {
        return cache2.get(key);
      }
      double result = 0D;
      for (long firstDiceSum = 1; firstDiceSum <= firstDiceThrows * d; firstDiceSum++) {
        result += expected2(dices.subList(1, dices.size()), firstDiceSum)
            * prob(d, firstDiceThrows, firstDiceSum);
      }
      cache2.put(key, result);
      return result;
    }
  }

  @AutoValue
  static abstract class Key3 {
    abstract long dice();
    abstract long diceThrows();
    abstract long sum();
    static Key3 create(long dice, long diceThrows, long sum) {
      return new AutoValue_Problem389_Key3(dice, diceThrows, sum);
    }
  }

  private final Map<Key3, Double> cache3 = new HashMap<>();

  private double prob(long dice, long diceThrows, long sum) {
    if (diceThrows == 0) {
      return sum == 0 ? 1D : 0D;
    } else {
      Key3 key = Key3.create(dice, diceThrows, sum);
      if (cache3.containsKey(key)) {
        return cache3.get(key);
      }
      double result = 0D;
      double singleThrowP = 1D / dice;
      for (long t = 1; sum - t >= 0 && t <= dice; t++) {
        result += singleThrowP * prob(dice, diceThrows - 1, sum - t);
      }
      cache3.put(key, result);
      return result;
    }
  }
}
