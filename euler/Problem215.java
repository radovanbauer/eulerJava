package euler;

public class Problem215 {

  public static void main(String[] args) {
    System.out.println(new Problem215().solve(32, 10));
  }

  public long solve(int w, int h) {
    return count(w, new int[h]);
  }

  private Long[][] cache = new Long[33][(1 << 20) + 1];

  private long count(int w, int[] edge) {
    int edgePacked = pack(edge);
    if (cache[w][edgePacked] != null) {
      return cache[w][edgePacked];
    }
    int max = 0;
    int maxRow = 0;
    for (int row = 0; row < edge.length; row++) {
      if (edge[row] > max) {
        max = edge[row];
        maxRow = row;
      }
    }
    long res;
    if (w == 0 && max == 0) {
      res = 1L;
    } else if (w > 0 && max < 3) {
      int[] newEdge = new int[edge.length];
      for (int row = 0; row < edge.length; row++) {
        newEdge[row] = edge[row] + 1;
      }
      res = count(w - 1, newEdge);
    } else {
      res = 0L;
      if (max >= 2) {
        int[] newEdge = edge.clone();
        newEdge[maxRow] -= 2;
        if (w + newEdge[maxRow] == 0
            || ((maxRow == 0 || newEdge[maxRow - 1] != newEdge[maxRow])
                && (maxRow == edge.length - 1 || newEdge[maxRow + 1] != newEdge[maxRow]))) {
          res += count(w, newEdge);
        }
      }
      if (max >= 3) {
        int[] newEdge = edge.clone();
        newEdge[maxRow] -= 3;
        if (w + newEdge[maxRow] == 0
            || (maxRow == 0 || newEdge[maxRow - 1] != newEdge[maxRow])
                && (maxRow == edge.length - 1 || newEdge[maxRow + 1] != newEdge[maxRow])) {
          res += count(w, newEdge);
        }
      }
    }
    return cache[w][edgePacked] = res;
  }

  private int pack(int[] sizes) {
    int res = 0;
    for (int i = 0; i < sizes.length; i++) {
      res |= sizes[i] << (2 * i);
    }
    return res;
  }
}
