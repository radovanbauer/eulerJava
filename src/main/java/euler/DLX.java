package euler;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class DLX {

  private final int rows, columns;
  private final Node root;
  private final Node[][] nodes;
  private final int[] columnCnt;

  public DLX(Iterable<boolean[]> matrix) {
    this(Iterables.toArray(matrix, boolean[].class));
  }

  public DLX(boolean[][] matrix) {
    this.rows = matrix.length + 1;
    this.columns = matrix[0].length;
    this.nodes = new Node[rows][columns];
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        boolean inner = row > 0;
        nodes[row][column] = new Node(row - 1, column, inner);
      }
    }
    for (int row = 0; row < rows; row++) {
      Node last = null;
      for (int column = 0; column < columns; column++) {
        if (row == 0 || matrix[row - 1][column]) {
          Node cur = nodes[row][column];
          if (last != null) {
            cur.left = last;
            last.right = cur;
          }
          last = cur;
        }
      }
    }
    columnCnt = new int[columns];
    for (int column = 0; column < columns; column++) {
      Node last = nodes[0][column];
      for (int row = 1; row < rows; row++) {
        if (matrix[row - 1][column]) {
          Node cur = nodes[row][column];
          cur.top = last;
          last.bottom = cur;
          last = cur;
          columnCnt[column]++;
        }
      }
    }
    root = new Node(-1, -1, false);
    root.right = nodes[0][0];
    nodes[0][0].left = root;
  }

  int x = 0;

  public long solutionCount() {
    if (root.right == null) {
      ++x;
      if (x % 1000 == 0) {
        System.out.println(x);
      }
      return 1L;
    }
    long res = 0L;
    Node node = bestColumn().bottom;
    while (node != null) {
      checkState(node.inner, "Invalid node: %s", node);
      List<Node> deleted = Lists.newArrayList();
      Node x = node.leftMost();
      while (x != null) {
        Node y = x.topMost();
        deleted.add(y.delete());
        y = y.bottom;
        while (y != null) {
          if (y != x) {
            Node z = y.leftMost();
            while (z != null) {
              deleted.add(z.delete());
              z = z.right;
            }
          }
          y = y.bottom;
        }
        x = x.right;
      }
      x = node.leftMost();
      while (x != null) {
        deleted.add(x.delete());
        x = x.right;
      }
      res += solutionCount();
      for (Node toUndelete : Lists.reverse(deleted)) {
        toUndelete.undelete();
      }
      node = node.bottom;
    }
    return res;
  }

  private Node bestColumn() {
    int minCnt = Integer.MAX_VALUE;
    Node best = null;
    Node node = root.right;
    while (node != null) {
      if (columnCnt[node.column] < minCnt) {
        best = node;
        minCnt = columnCnt[node.column];
      }
      node = node.right;
    }
    return best;
  }

  private class Node {
    private final int row, column;
    final boolean inner;
    Node top, right, bottom, left;

    public Node(int row, int column, boolean inner) {
      this.row = row;
      this.column = column;
      this.inner = inner;
    }

    public Node delete() {
      if (top != null) {
        top.bottom = bottom;
      }
      if (right != null) {
        right.left = left;
      }
      if (bottom != null) {
        bottom.top = top;
      }
      if (left != null) {
        left.right = right;
      }
      columnCnt[column]--;
      return this;
    }

    public Node undelete() {
      if (top != null) {
        top.bottom = this;
      }
      if (right != null) {
        right.left = this;
      }
      if (bottom != null) {
        bottom.top = this;
      }
      if (left != null) {
        left.right = this;
      }
      columnCnt[column]++;
      return this;
    }

    private Node topMost() {
      Node res = this;
      while (res.top != null) {
        res = res.top;
      }
      return res;
    }

    private Node leftMost() {
      Node res = this;
      while (res.left != null) {
        res = res.left;
      }
      return res;
    }

    @Override
    public String toString() {
      return String.format("%d,%d,%s", row, column, inner ? "in" : "out");
    }
  }

  @Override
  public String toString() {
    boolean[][] matrix = new boolean[rows - 1][columns];
    Node x = root.right;
    while (x != null) {
      Node y = x.bottom;
      while (y != null) {
        matrix[y.row][y.column] = true;
        y = y.bottom;
      }
      x = x.right;
    }
    StringBuilder res = new StringBuilder();
    for (int i = 0; i < rows - 1; i++) {
      for (int j = 0; j < columns; j++) {
        res.append(matrix[i][j] ? 'X' : '.');
      }
      res.append('\n');
    }
    return res.toString();
  }
}
