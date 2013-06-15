package euler;

import static com.google.common.collect.Collections2.permutations;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Problem170 {

  public static void main(String[] args) {
    System.out.println(new Problem170().solve());
  }

  public long solve() {
    long max = 0L;
    List<List<Integer>> splits = Lists.newArrayList();
    genSplits(10, Lists.<Integer>newLinkedList(), splits);
    System.out.println(splits.size());
    Collection<List<Integer>> perms = permutations(ImmutableList.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    int permCount = perms.size();
    int cnt = 0;
    for (List<Integer> perm : perms) {
      if (cnt % 100000 == 0) {
        System.out.printf("%d/%d\n", cnt, permCount);
      }
      cnt++;
      outer: for (List<Integer> split : splits) {
        StringBuilder product = new StringBuilder();
        int first = split.get(0);
        if (perm.get(0) == 0) {
          continue outer;
        }
        int base = fromDigits(perm.subList(0, first));
        int idx = first;
        for (int next : split.subList(1, split.size())) {
          if (perm.get(idx) == 0) {
            continue outer;
          }
          int num = fromDigits(perm.subList(idx, idx + next));
          product.append(String.valueOf(base * num));
          idx += next;
        }
        if (isPandigital(product.toString())) {
          if (Long.valueOf(product.toString()) > max) {
            max = Long.valueOf(product.toString());
            System.out.printf("%s = %s * %s\n", product, perm, split);
          }
        }
      }
    }
    return max;
  }

  private boolean isPandigital(String x) {
    if (x.length() != 10) {
      return false;
    }
    boolean[] present = new boolean[10];
    for (char c : x.toCharArray()) {
      if (c < '0' || c > '9' || present[c - '0']) {
        return false;
      } else {
        present[c - '0'] = true;
      }
    }
    return true;
  }

  private int fromDigits(Iterable<Integer> digits) {
    int res = 0;
    for (int digit : digits) {
      res *= 10;
      res += digit;
    }
    return res;
  }

  private void genSplits(int n, LinkedList<Integer> curSplit, List<List<Integer>> splits) {
    if (n == 0) {
      if (curSplit.size() <= 1) {
        return;
      }
      int first = curSplit.get(0);
      int sum = 0;
      for (int split : curSplit.subList(1, curSplit.size())) {
        sum += Math.min(1, first + split - 1);
      }
      if (sum <= 10) {
        splits.add(ImmutableList.copyOf(curSplit));
      }
    } else {
      for (int next = 1; next <= n; next++) {
        curSplit.addLast(next);
        genSplits(n - next, curSplit, splits);
        curSplit.removeLast();
      }
    }
  }
}
