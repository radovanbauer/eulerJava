package euler;

public class Problem194 {

  public static void main(String[] args) {
    System.out.println(new Problem194().solve());
  }

  public int solve() {
    boolean[][] g1 = createGraph(7, new int[][] {
        {0, 1},
        {0, 2},
        {0, 5},
        {1, 4},
        {1, 6},
        {2, 3},
        {2, 5},
        {3, 4},
        {4, 6},
        {5, 6}});
    boolean[][] g2 = createGraph(7, new int[][] {
        {0, 1},
        {0, 2},
        {0, 5},
        {1, 4},
        {2, 3},
        {2, 5},
        {3, 4},
        {4, 6},
        {5, 6}});

    int[] g1Colorings = colorings(g1, g1.length);
    int[] g2Colorings = colorings(g2, g2.length);
    return new SolutionCounter(1984, g1Colorings, g2Colorings).count(25, 75);
  }

  private static class SolutionCounter {
    private static final int MOD = 100000000;

    private final Integer[][] cache;
    private final int maxColors;
    private final int[] g1Colorings, g2Colorings;
    private final int[][] comb; 

    public SolutionCounter(int maxColors, int[] g1Colorings, int[] g2Colorings) {
      this.maxColors = maxColors;
      this.g1Colorings = g1Colorings;
      this.g2Colorings = g2Colorings;
      this.cache = new Integer[100][100];
      this.comb = calcComb(maxColors);
    }

    public int count(int a, int b) {
      if (cache[a][b] != null) {
        return cache[a][b];
      }
      int res = 0;
      if (a == 0 && b == 0) {
        res = 1;
      } else {
        boolean first = a + b == 1;
        if (a > 0) {
          for (int colors = 0; colors < g1Colorings.length; colors++) {
            if (g1Colorings[colors] > 0 && colors <= maxColors) {
              res = (int) ((res + 1L * g1Colorings[colors] / (first ? 1 : (colors * (colors - 1)))
                  * (first ? comb[maxColors][colors] : comb[maxColors - 2][colors - 2])
                  * count(a - 1, b)) % MOD);
            }
          }
        }
        if (b > 0) {
          for (int colors = 0; colors < g2Colorings.length; colors++) {
            if (g2Colorings[colors] > 0 && colors <= maxColors) {
              res = (int) ((res + 1L * g2Colorings[colors] / (first ? 1 : (colors * (colors - 1)))
                  * (first ? comb[maxColors][colors] : comb[maxColors - 2][colors - 2])
                  * count(a, b - 1)) % MOD);
            }
          }
        }
      }
      return cache[a][b] = res;
    }

    private int[][] calcComb(int n) {
      int[][] res = new int[n + 1][n + 1];
      res[0][0] = 1;
      for (int i = 1; i <= n; i++) {
        res[i][0] = res[i][i] = 1;
        for (int j = 1; j < i; j++) {
          res[i][j] = (res[i - 1][j - 1] + res[i - 1][j]) % MOD;
        }
      }
      return res;
    }
  }

  private int[] colorings(boolean[][] graph, int maxColors) {
    int[] res = new int[maxColors + 1];
    for (int numColors = 0; numColors <= maxColors; numColors++) {
      res[numColors] = color(graph, 0, new int[graph.length], numColors);
    }
    return res;
  }

  private int color(boolean[][] graph, int vertex, int[] colors, int numColors) {
    if (vertex == graph.length) {
      boolean[] colorsUsed = new boolean[numColors + 1];
      int cnt = 0;
      for (int c : colors) {
        if (!colorsUsed[c]) {
          cnt++;
          colorsUsed[c] = true;
        }
      }
      return (cnt == numColors) ? 1 : 0;
    }
    int cnt = 0;
    outer: for (int c = 1; c <= numColors; c++) {
      for (int otherVertex = 0; otherVertex < vertex; otherVertex++) {
        if (graph[vertex][otherVertex] && colors[otherVertex] == c) {
          continue outer;
        }
      }
      colors[vertex] = c;
      cnt += color(graph, vertex + 1, colors, numColors);
      colors[vertex] = 0;
    }
    return cnt;
  }

  private boolean[][] createGraph(int vertices, int[][] edges) {
    boolean[][] graph = new boolean[vertices][vertices];
    for (int[] edge : edges) {
      graph[edge[0]][edge[1]] = true;
      graph[edge[1]][edge[0]] = true;
    }
    return graph;
  }
}
