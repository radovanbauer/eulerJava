package euler;


public class Problem208 {

  public static void main(String[] args) {
    System.out.println(new Problem208().solve());
  }

  public long solve() {
    return solve(0, 14, 14, 14, 14, 14);
  }

  private Long[][][][][][] cache = new Long[5][15][15][15][15][15];

  private long solve(int dir, int c0, int c1, int c2, int c3, int c4) {
    if (c0 == 0 && c1 == 0 && c2 == 0 && c3 == 0 && c4 == 0) {
      return 1L;
    }
    if (cache[dir][c0][c1][c2][c3][c4] != null) {
      return cache[dir][c0][c1][c2][c3][c4];
    }
    long cnt = 0L;
    int[] c = {c0, c1, c2, c3, c4};
    if (c[dir] > 0) {
      c[dir]--;
      cnt += solve((dir + 1) % 5, c[0], c[1], c[2], c[3], c[4]); 
      c[dir]++;
    }
    if (c[(dir + 4) % 5] > 0) {
      c[(dir + 4) % 5]--;
      cnt += solve((dir + 4) % 5, c[0], c[1], c[2], c[3], c[4]);
    }
    return cache[dir][c0][c1][c2][c3][c4] = cnt;
  }
}
