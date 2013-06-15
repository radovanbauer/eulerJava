package euler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Problem185 {

  public static void main(String[] args) {
    System.out.println(new Problem185().solve());
  }

  public Long solve() {
    int n = 16;
    ImmutableMap<Long, Integer> input1 = ImmutableMap.<Long, Integer>builder()
        .put(5616185650518293L, 2)
        .put(3847439647293047L, 1)
        .put(5855462940810587L, 3)
        .put(9742855507068353L, 3)
        .put(4296849643607543L, 3)
        .put(3174248439465858L, 1)
        .put(4513559094146117L, 2)
        .put(7890971548908067L, 3)
        .put(8157356344118483L, 1)
        .put(2615250744386899L, 2)
        .put(8690095851526254L, 3)
        .build();
    ImmutableMap<Long, Integer> input2 = ImmutableMap.<Long, Integer>builder()
        .put(6375711915077050L, 1)
        .put(6913859173121360L, 1)
        .put(6442889055042768L, 2)
        .put(2321386104303845L, 0)
        .put(2326509471271448L, 2)
        .put(5251583379644322L, 2)
        .put(1748270476758276L, 3)
        .put(4895722652190306L, 1)
        .put(3041631117224635L, 3)
        .put(1841236454324589L, 3)
        .put(2659862637316867L, 2)
        .build();
    Optional<Node> solution1 = solve(n, input1);
    Optional<Node> solution2 = solve(n, input2);
    return Iterables.getOnlyElement(solution1.get().intersect(solution2.get()).enumerate());
  }

  private Optional<Node> solve(int n, ImmutableMap<Long, Integer> input) {
    List<Integer>[][] posLists = new List[n][10];
    for (int digit = 0; digit < n; digit++) {
      for (int d = 0; d <= 9; d++) {
        posLists[digit][d] = Lists.newArrayList();
      }
    }
    int idx = 0;
    for (long l : input.keySet()) {
      int digit = 0;
      while (l != 0) {
        posLists[digit][(int) (l % 10)].add(idx);
        digit++;
        l /= 10;
      }
      idx++;
    }
    pos = new int[n][10][];
    cache = Maps.newHashMap();
    for (int digit = 0; digit < n; digit++) {
      for (int d = 0; d <= 9; d++) {
        List<Integer> list = posLists[digit][d];
        pos[digit][d] = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
          pos[digit][d][i] = list.get(i);
        }
      }
    }
    int[] cnt = new int[input.size()];
    idx = 0;
    for (int c : input.values()) {
      cnt[idx++] = c;
    }
    return solve(0, cnt);
  }

  private int[][][] pos;
  private Map<CacheKey, Optional<Node>> cache;

  private Optional<Node> solve(int digit, int[] cnt) {
    CacheKey cacheKey = new CacheKey(digit, cnt);
    Optional<Node> res = cache.get(cacheKey);
    if (res != null) {
      return res;
    }
    if (digit == pos.length) {
      res = Optional.of(Node.EMPTY);
      for (int c : cnt) {
        if (c != 0) {
          res = Optional.absent();
        }
      }
    } else {
      Node[] children = new Node[10];
      int numChildren = 0;
      outer: for (int d = 0; d <= 9; d++) {
        int[] list = pos[digit][d];
        for (int i : list) {
          if (cnt[i] == 0 || cnt[i] > pos.length - digit) {
            continue outer;
          }
        }
        int[] newCnt = cnt.clone();
        for (int i : list) {
          newCnt[i]--;
        }
        Optional<Node> solution = solve(digit + 1, newCnt);
        if (solution.isPresent()) {
          children[d] = solution.get();
          numChildren++;
        }
      }
      if (numChildren > 0) {
        res = Optional.of(new Node(children));
      } else {
        res = Optional.absent();
      }
    }
    cache.put(cacheKey, res);
    return res;
  }

  private static class Node {
    private final Node[] children;

    public static final Node EMPTY = new Node(new Node[10]);

    public Node(Node[] children) {
      this.children = children;
    }

    public Node intersect(Node that) {
      if (this == EMPTY && that == EMPTY) {
        return EMPTY;
      }
      Node[] newChildren = new Node[10];
      int numChildren = 0;
      for (int d = 0; d <= 9; d++) {
        if (this.children[d] != null && that.children[d] != null) {
          Node intersection = this.children[d].intersect(that.children[d]);
          if (intersection != null) {
            newChildren[d] = intersection;
            numChildren++;
          }
        }
      }
      if (numChildren != 0) {
        return new Node(newChildren);
      } else {
        return null;
      }
    }

    public Set<Long> enumerate() {
      if (this == EMPTY) {
        return ImmutableSet.of(0L);
      }
      Set<Long> res = Sets.newHashSet();
      for (int d = 0; d <= 9; d++) {
        if (children[d] != null) {
          for (long l : children[d].enumerate()) {
            res.add(l * 10L + d);
          }
        }
      }
      return res;
    }
  }

  private static class CacheKey {
    private final int digit;
    private final int[] cnt;

    public CacheKey(int digit, int[] cnt) {
      this.digit = digit;
      this.cnt = cnt;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(digit, Arrays.hashCode(cnt));
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof CacheKey) {
        CacheKey that = (CacheKey) obj;
        return this.digit == that.digit
            && Arrays.equals(this.cnt, that.cnt);
      }
      return false;
    }
  }
}
