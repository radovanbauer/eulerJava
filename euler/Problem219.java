package euler;

import com.google.common.collect.TreeMultiset;

public class Problem219 {

  public static void main(String[] args) {
    System.out.println(new Problem219().solve(1000000000));
  }

  public long solve(int n) {
    TreeMultiset<Integer> nodes = TreeMultiset.create();
    nodes.add(0);
    int cnt = 1;
    long cost = 0L;
    while (cnt < n) {
      int node = nodes.firstEntry().getElement();
      nodes.remove(node);
      cnt++;
      cost += node + 5;
      nodes.add(node + 1);
      nodes.add(node + 4);
    }
    return cost;
  }
}
