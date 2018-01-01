package euler;

import java.util.BitSet;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Problem175 {

  public static void main(String[] args) {
    System.out.println(Joiner.on(",").join(new Problem175().solve(123456789, 987654321)));
  }

  public List<Integer> solve(int p, int q) {
    BitSet res = new BitSet();
    int idx = 0;
    while (p != q) {
      if (p < q) {
        q -= p;
        res.set(idx++, true);
      } else {
        p -= q;
        res.set(idx++, false);
      }
    }
    res.set(idx, true);
    return Lists.reverse(compress(res));
  }

  private ImmutableList<Integer> compress(BitSet set) {
    ImmutableList.Builder<Integer> parts = ImmutableList.builder();
    int cnt = 0;
    boolean last = true;
    for (int i = 0; i < set.length(); i++) {
      boolean cur = set.get(i);
      if (cur == last) {
        cnt++;
      } else {
        parts.add(cnt);
        cnt = 1;
      }
      last = cur;
    }
    parts.add(cnt);
    return parts.build();
  }
}
