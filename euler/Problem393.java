package euler;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

public class Problem393 {
  public static void main(String[] args) {
    Runner.run(new Problem393()::solve);
  }

  public long solve() {
    int n = 10;
    return count(n, 0, 0, HEdge.NONE, Iterables.toArray(Collections.nCopies(n, VEdge.NONE), VEdge.class));
  }

  @AutoValue
  static abstract class Input {
    abstract int n();
    abstract int row();
    abstract int column();
    abstract HEdge leftHEdge();
    abstract ImmutableList<VEdge> vEdges();

    static Input create(int n, int row, int column, HEdge leftHEdge, VEdge[] vedges) {
      return new AutoValue_Problem393_Input(n, row, column, leftHEdge, ImmutableList.copyOf(vedges));
    }
  }

  private final Map<Input, Long> cache = new HashMap<>();

  private long count(int n, int row, int column, HEdge leftHEdge, VEdge[] vEdges) {
    if (row == n && column == 0) {
      checkState(Arrays.stream(vEdges).allMatch(edge -> edge == VEdge.NONE));
      return 1;
    }
    Input input = Input.create(n, row, column, leftHEdge, vEdges);
    if (cache.containsKey(input)) {
      return cache.get(input);
    }
    VEdge[] newVEdges = vEdges.clone();
    long sum = 0;
    VEdge downVEdge = vEdges[column];
    for (VEdge upVEdge : VEdge.values()) {
      newVEdges[column] = upVEdge;
      if (row == n - 1 && upVEdge != VEdge.NONE) {
        continue;
      }
      for (HEdge rightHEdge : HEdge.values()) {
        int ins = 0;
        int outs = 0;
        if (column == n - 1 && rightHEdge != HEdge.NONE) {
          continue;
        }
        switch (leftHEdge) {
          case LEFT:
            outs++;
            break;
          case RIGHT:
            ins++;
            break;
        }
        switch (downVEdge) {
          case UP:
            ins++;
            break;
          case DOWN:
            outs++;
            break;
        }
        switch (upVEdge) {
          case UP:
            outs++;
            break;
          case DOWN:
            ins++;
            break;
        }
        switch (rightHEdge) {
          case LEFT:
            ins++;
            break;
          case RIGHT:
            outs++;
            break;
        }
        if (ins == 1 && outs == 1) {
          sum += count(n, column == n - 1 ? row + 1 : row, column == n - 1 ? 0 : column + 1, rightHEdge, newVEdges);
        }
      }
    }
    cache.put(input, sum);
    return sum;
  }

  enum VEdge { NONE, UP, DOWN }
  enum HEdge { NONE, LEFT, RIGHT }
}
