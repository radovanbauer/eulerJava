package euler;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.List;

public class Problem209 {

  public static void main(String[] args) {
    System.out.println(new Problem209().solve());
  }

  public long solve() {
    Node[] nodes = new Node[1 << 6];
    for (int x = 0; x < nodes.length; x++) {
      nodes[x] = new Node(x);
    }
    for (int x = 0; x < 1 << 6; x++) {
      int[] xx = decode(x);
      int[] yy = new int[] {
          xx[1], xx[2], xx[3], xx[4], xx[5], xx[0] ^ (xx[1] & xx[2]) };
      int y = encode(yy);
      nodes[x].neighbours.add(nodes[y]);
      if (x != y) {
        nodes[y].neighbours.add(nodes[x]);
      }
    }
    long cnt = 1L;
    boolean[] vis = new boolean[64];
    for (Node node : nodes) {
      if (!vis[node.id]) {
        cnt *= count(nodeCount(node, vis));
      }
    }
    return cnt; 
  }

  private int nodeCount(Node node, boolean[] vis) {
    if (vis[node.id]) return 0;
    vis[node.id] = true;
    int cnt = 1;
    for (Node neighbour : node.neighbours) {
      cnt += nodeCount(neighbour, vis);
    }
    return cnt;
  }

  private long count(int nodes) {
    if (nodes <= 1) {
      return 1L;
    } else {
      return count(nodes - 1, false) + count(nodes - 2, true);
    }
  }

  private Long[][] cache = new Long[64][2];

  private long count(int nodes, boolean lastZero) {
    if (cache[nodes][lastZero ? 1 : 0] != null) {
      return cache[nodes][lastZero ? 1 : 0];
    }
    long res;
    if (nodes == 0) {
      res = 1L;
    } else if (nodes == 1) {
      res = lastZero ? 1L : 2L;
    } else {
      res = count(nodes - 1, lastZero) + count(nodes - 2, lastZero);
    }
    return cache[nodes][lastZero ? 1 : 0] = res;
  }

  private static class Node {
    final int id;
    List<Node> neighbours = Lists.newArrayList();

    public Node(int x) {
      this.id = x;
    }

    @Override
    public String toString() {
      List<List<Integer>> xs = Lists.newArrayList();
      for (Node neighbour : neighbours) {
        xs.add(Ints.asList(decode(neighbour.id)));
      }
      return Arrays.toString(decode(id)) + ": " + Joiner.on(", ").join(xs);
    }
  }

  private static int encode(int[] x) {
    return x[0] << 5 | x[1] << 4 | x[2] << 3 | x[3] << 2 | x[4] << 1 | x[5];
  }

  private static int[] decode(int x) {
    int[] res = new int[6];
    res[5] = x & 1;
    res[4] = (x >> 1) & 1;
    res[3] = (x >> 2) & 1;
    res[2] = (x >> 3) & 1;
    res[1] = (x >> 4) & 1;
    res[0] = (x >> 5) & 1;
    return res;
  }
}
