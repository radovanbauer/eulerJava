package euler;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class Problem529 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem529().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long n = 1_000_000_000_000_000_000L;
    ArrayDeque<State> stateQueue = new ArrayDeque<>();
    for (int i = 1; i <= 9; i++) {
      stateQueue.add(State.create(ImmutableSet.of(), ImmutableSet.of(i)));
    }
    Map<State, Map<Integer, State>> transitions = new HashMap<>();
    while (!stateQueue.isEmpty()) {
      State state = stateQueue.removeFirst();
      if (!transitions.containsKey(state)) {
        transitions.put(state, new HashMap<>());
        nextDigit: for (int d = 0; d <= 9; d++) {
          Set<Integer> newFriendly = new HashSet<>();
          Set<Integer> newUnfriendly = new HashSet<>();
          if (state.friendly().contains(10 - d) || state.unfriendly().contains(10 - d)) {
            for (int i : state.friendly()) {
              if (i + d < 10) {
                newFriendly.add(i + d);
              }
            }
            for (int i : state.unfriendly()) {
              if (i + d > 10) {
                continue nextDigit;
              }
              if (i + d < 10) {
                newFriendly.add(i + d);
              }
            }
            if (d > 0) {
              newFriendly.add(d);
            }
          } else {
            for (int i : state.friendly()) {
              if (i + d < 10) {
                newFriendly.add(i + d);
              }
            }
            for (int i : state.unfriendly()) {
              if (i + d > 10) {
                continue nextDigit;
              }
              newUnfriendly.add(i + d);
            }
            if (d > 0) {
              newUnfriendly.add(d);
            }
          }
          State newState = State.create(newFriendly, newUnfriendly);
          transitions.get(state).put(d, newState);
          stateQueue.add(newState);
        }
      }
    }
    Map<State, Integer> stateIdx = new HashMap<>();
    int idx = 0;
    for (State state : transitions.keySet()) {
      stateIdx.put(state, idx++);
    }
    int s = stateIdx.size();
    long mod = 1_000_000_007;
    long[][] mat = new long[s + 1][s + 1];
    mat[s][s] = 1;
    for (State state : transitions.keySet()) {
      if (state.unfriendly().isEmpty()) {
        mat[s][stateIdx.get(state)] = 1;
      }
      for (State newState : transitions.get(state).values()) {
        mat[stateIdx.get(newState)][stateIdx.get(state)]++;
      }
    }
    long[][] a = new long[s + 1][1];
    for (int d = 1; d <= 9; d++) {
      a[stateIdx.get(State.create(ImmutableSet.of(), ImmutableSet.of(d)))][0] = 1;
    }
    LongModMatrix2 amat = LongModMatrix2.create(a, mod);
    LongModMatrix2 result = LongModMatrix2.create(mat, mod).pow(n).multiply(amat);

//    for (State state : transitions.keySet()) {
//      if (result.element(stateIdx.get(state), 0) != 0) {
//        System.out.println(state + ": " + result.element(stateIdx.get(state), 0));
//      }
//    }
//    Map<State, Integer> realCount = new HashMap<>();
//    for (int i = 1; i < IntMath.pow(10, (int) n + 1); i++) {
//      State state = stateFor(i, transitions);
//      checkState((state != null && state.unfriendly().isEmpty()) == is10Friendly(i),
//          "Error for %s, state %s", i, state);
//      if (state != null && i >= IntMath.pow(10, (int) n)) {
//        System.out.println(i + ": " + state);
//        if (!realCount.containsKey(state)) {
//          realCount.put(state, 0);
//        }
//        realCount.put(state, realCount.get(state) + 1);
//      }
//    }
//    System.out.println();
//    for (State state : realCount.keySet()) {
//      if (result.element(stateIdx.get(state), 0) != realCount.get(state)) {
//        System.out.println(state + ": " + result.element(stateIdx.get(state), 0) + " vs " + realCount.get(state));
//      }
//    }
    return result.element(s, 0);
  }

  private State stateFor(long n, Map<State, Map<Integer, State>> transitions) {
    char[] chars = String.valueOf(n).toCharArray();
    State state = State.create(ImmutableList.of(), ImmutableList.of(chars[0] - '0'));
    for (int i = 1; i < chars.length; i++) {
      int d = chars[i] - '0';
      State nextState = transitions.get(state).get(d);
      if (nextState == null) {
        return null;
      }
      state = nextState;
    }
    return state;
  }

  private boolean is10Friendly(long n) {
    char[] chars = String.valueOf(n).toCharArray();
    boolean[] friendly = new boolean[chars.length];
    int[] sums = new int[chars.length + 1];
    for (int i = 0; i < chars.length; i++) {
      sums[i + 1] = sums[i] + (chars[i] - '0');
    }
    for (int i = 0; i < chars.length; i++) {
      for (int j = i; j < chars.length; j++) {
        if (sums[j + 1] - sums[i] == 10) {
          Arrays.fill(friendly, i, j + 1, true);
        }
      }
    }
    for (boolean b : friendly) {
      if (!b) {
        return false;
      }
    }
    return true;
  }

  @AutoValue
  static abstract class State {
    abstract ImmutableSet<Integer> friendly();
    abstract ImmutableSet<Integer> unfriendly();

    static State create(Iterable<Integer> friendly, Iterable<Integer> unfriendly) {
      return new AutoValue_Problem529_State(
          ImmutableSet.copyOf(friendly), ImmutableSet.copyOf(unfriendly));
    }

    @Override
    public String toString() {
      return "<" + friendly() + ":" + unfriendly() + ">";
    }
  }
}
