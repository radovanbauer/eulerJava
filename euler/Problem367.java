package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.primitives.Ints;

public class Problem367 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem367().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int n = 11;
    Map<PermCycles, Integer> permIndex = new HashMap<>();
    for (List<Integer> perm : Collections2.permutations(Ints.asList(IntStream.range(0, n).toArray()))) {
      PermCycles cycles = PermCycles.create(perm);
      if (!permIndex.containsKey(cycles)) {
        permIndex.put(cycles, permIndex.size());
      }
    }
    long[][] transitions = new long[permIndex.size()][];
    long[] transitionCount = new long[permIndex.size()];
    long[] permCount = new long[permIndex.size()];
    long allPermCount = 0;
    for (List<Integer> perm : Collections2.permutations(Ints.asList(IntStream.range(0, n).toArray()))) {
      allPermCount++;
      int from = permIndex.get(PermCycles.create(perm));
      permCount[from]++;
      if (transitions[from] == null) {
        transitions[from] = new long[permIndex.size()];
        for (List<Integer> nextPerm : next(perm)) {
          int to = permIndex.get(PermCycles.create(nextPerm));;
          transitions[from][to]++;
          transitionCount[from]++;
        }
      }
    }
    BigFraction[][] m = new BigFraction[permIndex.size()][permIndex.size()];
    for (int from = 0; from < permIndex.size(); from++) {
      for (int to = 0; to < permIndex.size(); to++) {
        m[from][to] = transitionCount[from] > 0
            ? BigFraction.create(transitions[from][to], transitionCount[from])
            : BigFraction.ZERO;
      }
      m[from][from] = m[from][from].subtract(BigFraction.ONE);
    }
    BigFraction[] b = new BigFraction[permIndex.size()];
    Arrays.fill(b, BigFraction.create(-1));
    b[0] = BigFraction.ZERO;
    BigFraction[] x = linearSolve(m, b);
    BigFraction sum = BigFraction.ZERO;
    for (int i = 0; i < permCount.length; i++) {
      sum = sum.add(x[i].multiply(permCount[i]));
    }
    return Math.round(sum.divide(allPermCount).doubleValue());
  }

  private BigFraction[] linearSolve(BigFraction[][] m, BigFraction[] b) {
    BigFraction[][] _m = new BigFraction[m.length][];
    for (int i = 0; i < m.length; i++) {
      _m[i] = m[i].clone();
    }
    BigFraction[] _b = b.clone();
    for (int column = 0; column < m.length; column++) {
      int row = column;
      int nonZeroRow = -1;
      for (int r = column; r < m.length; r++) {
        if (!_m[r][column].equals(BigFraction.ZERO)) {
          nonZeroRow = r;
          break;
        }
      }
      checkArgument(nonZeroRow != -1, "No solution or multiple solutions");
      if (nonZeroRow > row) {
        for (int c = column; c < m.length; c++) {
          _m[row][c] = _m[row][c].add(_m[nonZeroRow][c]);
        }
        _b[row] = _b[row].add(_b[nonZeroRow]);
      }
      for (int r = row + 1; r < m.length; r++) {
        if (!_m[r][column].equals(BigFraction.ZERO)) {
          BigFraction ratio = _m[r][column].divide(_m[row][column]);
          for (int c = column; c < m.length; c++) {
            _m[r][c] = _m[r][c].subtract(_m[row][c].multiply(ratio));
          }
          _b[r] = _b[r].subtract(_b[row].multiply(ratio));
        }
      }
    }
    BigFraction[] res = new BigFraction[m.length];
    for (int i = m.length - 1; i >= 0; i--) {
      res[i] = _b[i];
      for (int j = m.length - 1; j > i; j--) {
        res[i] = res[i].subtract(_m[i][j].multiply(res[j]));
      }
      res[i] = res[i].divide(_m[i][i]);
    }
    return res;
  }

  private List<List<Integer>> next(List<Integer> perm) {
    boolean sorted = true;
    int n = perm.size();
    for (int i = 0; i < n - 1; i++) {
      if (perm.get(i) > perm.get(i + 1)) {
        sorted = false;
        break;
      }
    }
    if (sorted) {
      return ImmutableList.of();
    } else {
      List<List<Integer>> res = new ArrayList<>();
      for (int i1 = 0; i1 < n; i1++) {
        int v1 = perm.get(i1);
        for (int i2 = i1 + 1; i2 < n; i2++) {
          int v2 = perm.get(i2);
          for (int i3 = i2 + 1; i3 < n; i3++) {
            int v3 = perm.get(i3);
            res.add(createPerm(perm, i1, i2, i3, v1, v2, v3));
            res.add(createPerm(perm, i1, i2, i3, v1, v3, v2));
            res.add(createPerm(perm, i1, i2, i3, v2, v1, v3));
            res.add(createPerm(perm, i1, i2, i3, v2, v3, v1));
            res.add(createPerm(perm, i1, i2, i3, v3, v1, v2));
            res.add(createPerm(perm, i1, i2, i3, v3, v2, v1));
          }
        }
      }
      return res;
    }
  }

  private List<Integer> createPerm(List<Integer> base, int i1, int i2, int i3, int v1, int v2, int v3) {
    ArrayList<Integer> res = new ArrayList<>(base);
    res.set(i1, v1);
    res.set(i2, v2);
    res.set(i3, v3);
    return res;
  }

  @AutoValue
  static abstract class PermCycles {
    abstract ImmutableMultiset<Integer> cycles();

    static PermCycles create(List<Integer> perm) {
      ImmutableMultiset.Builder<Integer> cycles = ImmutableMultiset.builder();
      boolean[] visited = new boolean[perm.size()];
      for (int i = 0; i < perm.size(); i++) {
        if (!visited[i]) {
          int cur = i;
          int cycle = 1;
          visited[cur] = true;
          while (perm.get(cur) != i) {
            cycle++;
            cur = perm.get(cur);
            visited[cur] = true;
          }
          cycles.add(cycle);
        }
      }
      return new AutoValue_Problem367_PermCycles(cycles.build());
    }
  }
}
