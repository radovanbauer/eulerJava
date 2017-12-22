package euler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;

public class Problem315 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem315().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    return IntStream.rangeClosed(10_000_000, 20_000_000).parallel()
        .filter(this::isPrime)
        .map(n -> transitionsForDigitalRoot(n, 8, this::samTransitions)
            - transitionsForDigitalRoot(n, 8, this::maxTransitions))
        .sum();
  }

  private boolean isPrime(int n) {
    if (n <= 1) {
      return false;
    }
    for (long d = 2; d * d <= n; d++) {
      if (n % d == 0) {
        return false;
      }
    }
    return true;
  }

  private int transitionsForDigitalRoot(
      int n, int digitCount, TransitionFunction transitionFunction) {
    List<Integer> numbers = new ArrayList<>();
    numbers.add(0);
    numbers.add(n);
    while (n > 9) {
      n = sumOfDigits(n);
      numbers.add(n);
    }
    numbers.add(0);
    int transitions = 0;
    for (int i = 0; i < numbers.size() - 1; i++) {
      transitions += transitionFunction.transitions(
          Digits.create(numbers.get(i), digitCount),
          Digits.create(numbers.get(i + 1), digitCount));
    }
    return transitions;
  }

  private int sumOfDigits(int n) {
    return String.valueOf(n).chars().map(c -> c - '0').sum();
  }

  enum Digit {
    ZERO(0, 1, 2, 4, 5, 6),
    ONE(5, 6),
    TWO(1, 2, 3, 4, 5),
    THREE(2, 3, 4, 5, 6),
    FOUR(0, 3, 5, 6),
    FIVE(0, 2, 3, 4, 6),
    SIX(0, 1, 2, 3, 4, 6),
    SEVEN(0, 2, 5, 6),
    EIGHT(0, 1, 2, 3, 4, 5, 6),
    NINE(0, 2, 3, 4, 5, 6),
    NONE();

    private int segments;

    private Digit(int... segments) {
      this.segments = Arrays.stream(segments).map(segment -> 1 << segment).sum();
    }

    public static Digit forNumber(int n) {
      Preconditions.checkArgument(n >= 0 && n <= 9);
      return values()[n];
    }

    public int minTransitions(Digit that) {
      return Integer.bitCount(this.segments ^ that.segments);
    }
  }

  @AutoValue
  static abstract class Digits {
    public abstract ImmutableList<Digit> digits();

    public static Digits create(int n, int digitCount) {
      ImmutableList.Builder<Digit> digits = ImmutableList.builder();
      while (n > 0) {
        digits.add(Digit.forNumber(n % 10));
        n /= 10;
        digitCount--;
      }
      Preconditions.checkArgument(digitCount >= 0);
      while (digitCount > 0) {
        digits.add(Digit.NONE);
        digitCount--;
      }
      return new AutoValue_Problem315_Digits(digits.build());
    }
  }

  private int samTransitions(Digits d1, Digits d2) {
    Preconditions.checkArgument(d1.digits().size() == d2.digits().size());
    return IntStream.range(0, d1.digits().size())
        .map(d -> d1.digits().get(d).minTransitions(Digit.NONE)
            + Digit.NONE.minTransitions(d2.digits().get(d)))
        .sum();
  }

  private int maxTransitions(Digits d1, Digits d2) {
    Preconditions.checkArgument(d1.digits().size() == d2.digits().size());
    return IntStream.range(0, d1.digits().size())
        .map(d -> d1.digits().get(d).minTransitions(d2.digits().get(d)))
        .sum();
  }

  private interface TransitionFunction {
    int transitions(Digits d1, Digits d2);
  }
}
