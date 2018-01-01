package euler;

import com.google.auto.value.AutoValue;
import com.google.common.math.IntMath;

import java.math.RoundingMode;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

public class Problem292 {
  public static void main(String[] args) {
    Runner.run(new Problem292()::solve);
  }

  public long solve() {
    int maxLength = 120;
    List<Step> steps = new ArrayList<>();
    for (int x = -maxLength / 2; x <= maxLength / 2; x++) {
      for (int y = -maxLength / 2; y <= maxLength / 2; y++) {
        if (IntMath.gcd(Math.abs(x), Math.abs(y)) != 1) {
          continue;
        }
        int lengthSquare = x * x + y * y;
        int length = IntMath.sqrt(lengthSquare, RoundingMode.FLOOR);
        if (length * length == lengthSquare && length <= maxLength / 2 && length > 0) {
          int quadrant = getQuadrant(x, y);
          steps.add(Step.create(quadrant, x, y, length));
        }
      }
    }
    TreeSet<Step> sortedSteps = new TreeSet<>(steps);
    return count(sortedSteps, maxLength, 0, 0, 0, Step.create(-1, maxLength, -1, maxLength));
  }

  private int getQuadrant(int x, int y) {
    if (x > 0 && y >= 0) {
      return 0;
    } else if (y > 0 && x <= 0) {
      return 1;
    } else if (x < 0 && y <= 0) {
      return 2;
    } else if (y < 0 && x >= 0) {
      return 3;
    } else {
      throw new AssertionError();
    }
  }

  private final Map<Input, Long> cache = new HashMap<>();

  private long count(TreeSet<Step> steps, int maxLength, int x, int y, int totalLength, Step lastStep) {
    checkArgument(totalLength <= maxLength);
    int remainingLength = maxLength - totalLength;
    if (Math.max(Math.abs(x), Math.abs(y)) > remainingLength) {
      return 0;
    }
    Input input = Input.create(x, y, totalLength, lastStep);
    if (cache.containsKey(input)) {
      return cache.get(input);
    }
    long res = 0;
    if (x == 0 && y == 0 && totalLength > 0) {
      res = 1;
    } else {
      NavigableSet<Step> nextSteps = steps.subSet(lastStep, false, lastStep.addPi(), false);
      for (Step nextStep : nextSteps) {
        for (int multiple = 1; multiple * nextStep.length() <= remainingLength; multiple++) {
          res += count(
              steps,
              maxLength,
              x + multiple * nextStep.x(),
              y + multiple * nextStep.y(),
              totalLength + multiple * nextStep.length(),
              nextStep);
        }
      }
    }
    cache.put(input, res);
    return res;
  }

  @AutoValue
  static abstract class Point {
    abstract int x();
    abstract int y();
    static Point create(int x, int y) {
      return new AutoValue_Problem292_Point(x, y);
    }
  }

  @AutoValue
  static abstract class Input {
    abstract int x();
    abstract int y();
    abstract int totalLength();
    abstract Step lastStep();

    static Input create(int x, int y, int totalLength, Step lastStep) {
      return new AutoValue_Problem292_Input(x, y, totalLength, lastStep);
    }
  }

  @AutoValue
  static abstract class Step implements Comparable<Step> {
    abstract int quadrant();
    abstract int x();
    abstract int y();
    abstract int length();

    static Step create(int quadrant, int x, int y, int length) {
      checkArgument(length > 0);
      return new AutoValue_Problem292_Step(quadrant, x, y, length);
    }

    Step addPi() {
      return Step.create(quadrant() + 2, -x(), -y(), length());
    }

    private static final Comparator<Step> COMPARATOR = Comparator
        .comparing(Step::quadrant)
        .thenComparing(step -> step.quadrant() % 2 == 0
            ? LongFraction.create(step.y(), step.x())
            : LongFraction.create(-step.x(), step.y()))
        .thenComparing(Step::length);

    @Override
    public int compareTo(Step that) {
      return COMPARATOR.compare(this, that);
    }
  }
}
