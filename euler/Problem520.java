package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.auto.value.AutoValue;

public class Problem520 {

  public static void main(String[] args) {
    System.out.println(new Problem520().solve());
  }

  public long solve() {
    long mod = 1_000_000_123L;
    List<State> states = new ArrayList<>();
    Map<State, Integer> stateMap = new HashMap<>();
    for (int evenDigitOdds = 0; evenDigitOdds <= 5; evenDigitOdds++) {
      for (int oddDigitZeroes = 0; oddDigitZeroes <= 5; oddDigitZeroes++) {
        for (int oddDigitEvens = 0; oddDigitZeroes + oddDigitEvens <= 5; oddDigitEvens++) {
          State state = State.create(evenDigitOdds, oddDigitZeroes, oddDigitEvens);
          stateMap.put(state, states.size());
          states.add(state);
        }
      }
    }
    long[][] count1 = new long[states.size() * 2][1];
    for (int d = 1; d <= 9; d++) {
      int evenDigitOdds = d % 2 == 0 ? 1 : 0;
      int oddDigitZeroes = d % 2 == 1 ? 4 : 5;
      int oddDigitEvens = 0;
      count1[stateMap.get(State.create(evenDigitOdds, oddDigitZeroes, oddDigitEvens))][0]++;
    }
    LongModMatrix2 count1Matrix = LongModMatrix2.create(count1, mod);
    long[][] mat = new long[states.size() * 2][states.size() * 2];
    for (State from : states) {
      for (State to : states) {
        mat[stateMap.get(to)][stateMap.get(from)] = from.waysToGetTo(to);
      }
      mat[states.size() + stateMap.get(from)][states.size() + stateMap.get(from)] = 1;
      mat[states.size() + stateMap.get(from)][stateMap.get(from)] = 1;
    }
    LongModMatrix2 transitionMatrix = LongModMatrix2.create(mat, mod);
    LongMod count = LongMod.zero(mod);
    LongModMatrix2 transitionMatrixPow = transitionMatrix;
    for (int i = 1; i <= 39; i++) {
      transitionMatrixPow = transitionMatrixPow.multiply(transitionMatrixPow);
      LongModMatrix2 res = transitionMatrixPow.multiply(count1Matrix);
      for (State state : states) {
        if (state.isSimber()) {
          count = count.add(res.element(states.size() + stateMap.get(state), 0));
        }
      }
    }
    return count.n();
  }

  @AutoValue
  static abstract class State {
    abstract int evenDigitOdds();
    abstract int oddDigitZeroes();
    abstract int oddDigitEvens();

    int evenDigitEvens() {
      return 5 - evenDigitOdds();
    }

    int oddDigitOdds() {
      return 5 - oddDigitZeroes() - oddDigitEvens();
    }

    static State create(int evenDigitOdds, int oddDigitZeroes, int oddDigitEvens) {
      checkArgument(evenDigitOdds <= 5);
      checkArgument(oddDigitZeroes + oddDigitEvens <= 5);
      return new AutoValue_Problem520_State(evenDigitOdds, oddDigitZeroes, oddDigitEvens);
    }

    private boolean isSimber() {
      return evenDigitOdds() == 0 && oddDigitEvens() == 0;
    }

    private int waysToGetTo(State that) {
      if (this.evenDigitOdds() == that.evenDigitOdds() && this.oddDigitZeroes() == that.oddDigitZeroes()) {
        if (this.oddDigitEvens() + 1 == that.oddDigitEvens()) {
          return this.oddDigitOdds();
        } else if (this.oddDigitEvens() - 1 == that.oddDigitEvens()) {
          return this.oddDigitEvens();
        }
      } else if (this.evenDigitOdds() == that.evenDigitOdds() && this.oddDigitEvens() == that.oddDigitEvens()) {
        if (this.oddDigitZeroes() - 1 == that.oddDigitZeroes()) {
          return this.oddDigitZeroes();
        }
      } else if (this.oddDigitZeroes() == that.oddDigitZeroes() && this.oddDigitEvens() == that.oddDigitEvens()) {
        if (this.evenDigitOdds() + 1 == that.evenDigitOdds()) {
          return this.evenDigitEvens();
        } else if (this.evenDigitOdds() - 1 == that.evenDigitOdds()) {
          return this.evenDigitOdds();
        }
      }
      return 0;
    }
  }
}
