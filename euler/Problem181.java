package euler;

public class Problem181 {

  public static void main(String[] args) {
    System.out.println(new Problem181().solve(60, 40));
  }

  public long solve(int b, int w) {
    return solve(b, w, 0, 1);
  }

  Long[][][][] cache = new Long[61][41][61][41];

  private long solve(int b, int w, int minB, int minW) {
    if (cache[b][w][minB][minW] != null) {
      return cache[b][w][minB][minW];
    }
    long res;
    if (b == 0 && w == 0) {
      res = 1L;
    } else {
      res = 0L;
      for (int nextB = minB; nextB <= b; nextB++) {
        for (int nextW = (nextB == minB) ? minW : 0; nextW <= w; nextW++) {
          res += solve(b - nextB, w - nextW, nextB, nextW);
        }
      }
    }
    return cache[b][w][minB][minW] = res;
  }
}
