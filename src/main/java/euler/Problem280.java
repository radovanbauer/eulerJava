package euler;

import com.google.auto.value.AutoValue;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrix;

import java.util.*;

public class Problem280 {

  public static void main(String[] args) {
    Runner.run(new Problem280()::solve);
  }

  public String solve() {
    HashBasedTable<Integer, Integer, Boolean> seeds = HashBasedTable.create();
    for (int x = 0; x <= 4; x++) {
      for (int y = 0; y <= 4; y++) {
        seeds.put(x, y, x == 4);
      }
    }
    State startState = State.create(2, 2, false, seeds);
    Set<State> allStatesSet = new HashSet<>();
    allStatesSet.add(startState);
    ArrayDeque<State> nextStates = new ArrayDeque<>();
    nextStates.add(startState);
    while (!nextStates.isEmpty()) {
      State state = nextStates.pollFirst();
      for (State nextState : state.nextStates().keySet()) {
        if (!allStatesSet.contains(nextState)) {
          nextStates.add(nextState);
          allStatesSet.add(nextState);
        }
      }
    }
    ImmutableList<State> states = ImmutableList.copyOf(allStatesSet);
    int n = states.size();
    Map<State, Integer> statesIdx = new HashMap<>();
    for (int i = 0; i < n; i++) {
      statesIdx.put(states.get(i), i);
    }

    Matrix T = new DenseMatrix(n, n);
    for (int i = 0; i < n; i++) {
      for (Map.Entry<State, Double> entry : states.get(i).nextStates().entrySet()) {
        State nextState = entry.getKey();
        double p = entry.getValue();
        T.set(statesIdx.get(nextState), i, -p);
      }
      T.set(i, i, 1);
    }

    DenseMatrix startVector = new DenseMatrix(n, 1);
    startVector.set(statesIdx.get(startState), 0, 1);
    DenseMatrix oneVector = new DenseMatrix(1, n);
    for (int i = 0; i < n; i++) {
      oneVector.set(0, i, 1);
    }

    return String.format("%.6f", oneVector
        .mult(T.solve(startVector, new DenseMatrix(n, 1)), new DenseMatrix(1, 1))
        .get(0, 0));
  }

  @AutoValue
  static abstract class State {
    abstract int x();
    abstract int y();
    abstract boolean hasSeed();
    abstract Table<Integer, Integer, Boolean> seeds();

    static State create(int x, int y, boolean hasSeed, Table<Integer, Integer, Boolean> seeds) {
      return new AutoValue_Problem280_State(x, y, hasSeed, ImmutableTable.copyOf(seeds));
    }

    Map<State, Double> nextStates() {
      int allStates = 0;
      List<State> nextStates = new ArrayList<>();
      int[][] dirs = new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
      for (int[] dir : dirs) {
        HashBasedTable<Integer, Integer, Boolean> newSeeds = HashBasedTable.create(seeds());
        int newX = x() + dir[0];
        int newY = y() + dir[1];
        boolean newHasSeed = hasSeed();
        if (newX >= 0 && newX <= 4 && newY >= 0 && newY <= 4) {
          if (!hasSeed() && newX == 4 && seeds().get(newX, newY)) {
            newHasSeed = true;
            newSeeds.put(newX, newY, false);
          } else if (hasSeed() && newX == 0 && !seeds().get(newX, newY)) {
            newHasSeed = false;
            newSeeds.put(newX, newY, true);
          }
          allStates++;
          State newState = State.create(newX, newY, newHasSeed, newSeeds);
          if (!newState.isTerminal()) {
            nextStates.add(newState);
          }
        }
      }
      double p = 1D / allStates;
      HashMap<State, Double> res = new HashMap<>();
      for (State state : nextStates) {
        res.put(state, p);
      }
      return res;
    }

    private boolean isTerminal() {
      for (int y = 0; y <= 4; y++) {
        if (!seeds().get(0, y)) {
          return false;
        }
      }
      return true;
    }

    @Override
    public String toString() {
      StringBuilder res = new StringBuilder();
      for (int x = 0; x <= 4; x++) {
        for (int y = 0; y <= 4; y++) {
          boolean ant = x() == x && y() == y;
          if (!ant && !seeds().get(x, y)) {
            res.append(".  ");
          } else if (!ant && seeds().get(x, y)) {
            res.append("!  ");
          } else if (ant && !hasSeed() && !seeds().get(x, y)) {
            res.append("X  ");
          } else if (ant && !hasSeed() && seeds().get(x, y)) {
            res.append("X! ");
          } else if (ant && hasSeed() && !seeds().get(x, y)) {
            res.append("!X ");
          } else if (ant && hasSeed() && seeds().get(x, y)) {
            res.append("!X!");
          }
        }
        res.append("\n");
      }
      return res.toString();
    }
  }
}
