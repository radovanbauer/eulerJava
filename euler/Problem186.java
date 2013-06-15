package euler;

import java.util.Iterator;

import com.google.common.collect.AbstractIterator;
import com.google.common.math.IntMath;

public class Problem186 {

  public static void main(String[] args) {
    System.out.println(new Problem186().solve());
  }

  public long solve() {
    Iterator<Integer> laggedFibIter = laggedFib();
    int calls = 0;
    DisjointSetStructure dss = new DisjointSetStructure(1000000);
    while (true) {
      int caller = laggedFibIter.next();
      int called = laggedFibIter.next();
      if (caller != called) {
        calls++;
        dss.union(caller, called);
        if (dss.count(524287) >= 990000) {
          return calls;
        }
      }
    }
  }

  private Iterator<Integer> laggedFib() {
    return new AbstractIterator<Integer>() {
      private int k = 1;
      private int[] last = new int[55];

      @Override
      protected Integer computeNext() {
        int res;
        if (k <= 55) {
          res = (int) ((100003L - 200003L * k + 300007L * k * k * k) % 1000000);
        } else {
          res = (last[IntMath.mod(k - 24, last.length)]
              + last[IntMath.mod(k - 55, last.length)]) % 1000000;
        }
        last[k % last.length] = res;
        k++;
        return res;
      }
    };
  }

  private static class DisjointSetStructure {
    private final int[] parent;
    private final int[] count;

    public DisjointSetStructure(int n) {
      this.parent  = new int[n];
      this.count = new int[n];
      for (int i = 0; i < n; i++) {
        this.parent[i] = i;
        this.count[i] = 1;
      }
    }

    public void union(int i, int j) {
      int p1 = find(i);
      int p2 = find(j);
      if (p1 != p2) {
        if (count[p1] <= count[p2]) {
          parent[p1] = p2;
          count[p2] += count[p1];
        } else {
          parent[p2] = p1;
          count[p1] += count[p2];
        }
      }
    }

    public int find(int i) {
      if (parent[i] == i) {
        return i;
      } else {
        return parent[i] = find(parent[i]);
      }
    }

    public int count(int i) {
      return count[find(i)];
    }
  }
}
