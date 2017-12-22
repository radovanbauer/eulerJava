package euler;

import com.google.auto.value.AutoValue;
import com.google.common.primitives.ImmutableIntArray;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class Problem298 {
  public static void main(String[] args) {
    Runner.run(new Problem298()::solve);
  }

  public String solve() {
    return String.format("%.8f", calc(50));
  }

  private double calc(int turns) {
    Map<State, BigInteger> count = new HashMap<>();
    count.put(
        State.create(Pos.create(0, ImmutableIntArray.of()), Pos.create(0, ImmutableIntArray.of())),
        BigInteger.ONE);
    for (int turn = 0; turn < turns; turn++) {
      Map<State, BigInteger> newCount = new HashMap<>();
      for (State state : count.keySet()) {
        for (int next = 1; next <= 10; next++) {
          State newState = normalize(nextState(state, next));
          newCount.merge(newState, count.get(state), BigInteger::add);
        }
      }
      count = newCount;
    }

    BigInteger scoreDiffSum = BigInteger.ZERO;
    BigInteger totalCount = BigInteger.ZERO;
    for (State state : count.keySet()) {
      scoreDiffSum = scoreDiffSum.add(
          count.get(state).multiply(BigInteger.valueOf(Math.abs(state.larry().score() - state.robin().score()))));
      totalCount = totalCount.add(count.get(state));
    }
    return new BigDecimal(scoreDiffSum).divide(new BigDecimal(totalCount), MathContext.DECIMAL128).doubleValue();
  }

  private State normalize(State state) {
    int[] remapping = new int[11];
    int next = 1;
    int[] larryNewPos = state.larry().memory().toArray();
    int[] robinNewPos = state.robin().memory().toArray();
    for (int i = 0; i < larryNewPos.length; i++) {
      if (remapping[larryNewPos[i]] == 0) {
        remapping[larryNewPos[i]] = next++;
      }
      larryNewPos[i] = remapping[larryNewPos[i]];
    }
    for (int i = 0; i < robinNewPos.length; i++) {
      if (remapping[robinNewPos[i]] == 0) {
        remapping[robinNewPos[i]] = next++;
      }
      robinNewPos[i] = remapping[robinNewPos[i]];
    }
    int minScore = Math.min(state.larry().score(), state.robin().score());
    return State.create(
        Pos.create(state.larry().score() - minScore, ImmutableIntArray.copyOf(larryNewPos)),
        Pos.create(state.robin().score() - minScore, ImmutableIntArray.copyOf(robinNewPos)));
  }

  private State nextState(State state, int next) {
    return State.create(nextLarry(state.larry(), next), nextRobin(state.robin(), next));
  }

  private Pos nextLarry(Pos pos, int next) {
    int pl = pos.memory().length();
    if (pos.memory().contains(next)) {
      int[] result = pos.memory().toArray();
      for (int i = 0; i < pl - 1; i++) {
        if (result[i] == next) {
          int tmp = result[i];
          result[i] = result[i + 1];
          result[i + 1] = tmp;
        }
      }
      return Pos.create(pos.score() + 1, ImmutableIntArray.copyOf(result));
    } else {
      if (pl < 5) {
        int[] result = new int[pl + 1];
        for (int i = 0; i < pl; i++) {
          result[i] = pos.memory().get(i);
        }
        result[pl] = next;
        return Pos.create(pos.score(), ImmutableIntArray.copyOf(result));
      } else {
        int[] result = new int[5];
        for (int i = 0; i < 4; i++) {
          result[i] = pos.memory().get(i + 1);
        }
        result[4] = next;
        return Pos.create(pos.score(), ImmutableIntArray.copyOf(result));
      }
    }
  }

  private Pos nextRobin(Pos pos, int next) {
    int pl = pos.memory().length();
    if (pos.memory().contains(next)) {
      return Pos.create(pos.score() + 1, pos.memory());
    } else {
      if (pl < 5) {
        int[] result = new int[pl + 1];
        for (int i = 0; i < pl; i++) {
          result[i] = pos.memory().get(i);
        }
        result[pl] = next;
        return Pos.create(pos.score(), ImmutableIntArray.copyOf(result));
      } else {
        int[] result = new int[5];
        for (int i = 0; i < 4; i++) {
          result[i] = pos.memory().get(i + 1);
        }
        result[4] = next;
        return Pos.create(pos.score(), ImmutableIntArray.copyOf(result));
      }
    }
  }

  @AutoValue
  static abstract class State {
    abstract Pos larry();
    abstract Pos robin();

    static State create(Pos larry, Pos robin) {
      return new AutoValue_Problem298_State(larry, robin);
    }
  }

  @AutoValue
  static abstract class Pos {
    abstract int score();
    abstract ImmutableIntArray memory();

    static Pos create(int score, ImmutableIntArray memory) {
      return new AutoValue_Problem298_Pos(score, memory);
    }
  }
}
