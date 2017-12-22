package euler;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class Problem265 {

  public static void main(String[] args) {
    System.out.println(new Problem265().solve());
  }

  public long solve() {
    int n = 5;
    long sum = 0;
    for (ImmutableSet<Integer> sequence : sequences(1 << n, n)) {
      long bin = 0;
      for (int i : sequence.asList().subList(0, (1 << n) - n + 1)) {
        bin <<= 1;
        bin |= (i & 1);
      }
      sum += bin;
    }
    return sum;
  }

  private List<ImmutableSet<Integer>> sequences(int length, int n) {
    if (length == 1) {
      return ImmutableList.of(ImmutableSet.of(0));
    }
    List<ImmutableSet<Integer>> prevSequences = sequences(length - 1, n);
    ArrayList<ImmutableSet<Integer>> newSequences = new ArrayList<>();
    for (ImmutableSet<Integer> prevSequence : prevSequences) {
      int last = prevSequence.asList().get(prevSequence.size() - 1);
      int next1 = (last & ~(1 << (n - 1))) << 1;
      if (!prevSequence.contains(next1)) {
        newSequences.add(ImmutableSet.<Integer>builder().addAll(prevSequence).add(next1).build());
      }
      if (length <= (1 << n) - n + 1) {
        int next2 = next1 | 1;
        if (!prevSequence.contains(next2)) {
          newSequences.add(ImmutableSet.<Integer>builder().addAll(prevSequence).add(next2).build());
        }
      }
    }
    return newSequences;
  }
}
