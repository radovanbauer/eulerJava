package euler;

import com.google.common.collect.Lists;

import java.util.Queue;

public class Problem198v2 {

  public static void main(String[] args) {
    System.out.println(new Problem198v2().solve(100000000));
  }

  public long solve(int n) {
    long cnt = 0L;
    Queue<Node> queue = Lists.newLinkedList();
    queue.add(new Node(0, 1, 1, 1));
    while (!queue.isEmpty()) {
      Node nd = queue.poll();
      if (nd.ld > n ||
          nd.hd > n ||
          100L * nd.ln >= nd.ld ||
          2L * nd.ld * nd.hd > n) continue;
      if (50L * (nd.ln * nd.hd + nd.hn * nd.ld) < nd.ld * nd.hd) {
        cnt++;
      }
      queue.add(new Node(nd.ln, nd.ld, nd.ln + nd.hn, nd.ld + nd.hd));
      queue.add(new Node(nd.ln + nd.hn, nd.ld + nd.hd, nd.hn, nd.hd));
    }
    return cnt;
  }

  private static class Node {
    private final int ln, ld, hn, hd;
    private Node(int ln, int ld, int hn, int hd) {
      this.ln = ln;
      this.ld = ld;
      this.hn = hn;
      this.hd = hd;
    }
  }
}
