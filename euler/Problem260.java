package euler;

import java.util.BitSet;

public class Problem260 {

  public static void main(String[] args) {
    System.out.println(new Problem260().solve());
  }

  public long solve() {
    int max = 1000;
    Positions positions = new Positions(max);
    int[][] moves = new int[][]
        {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}, {1, 1, 0}, {1, 0, 1}, {0, 1, 1}, {1, 1, 1}};
    for (int x = 0; x <= max; x++) {
      for (int y = 0; y <= max; y++) {
        for (int z = 0; z <= max; z++) {
          if (!positions.isWinning(x, y, z)) {
            for (int[] move : moves) {
              for (int n = 1; n <= 1000; n++) {
                int nx = x + n * move[0];
                int ny = y + n * move[1];
                int nz = z + n * move[2];
                if (nx > max || ny > max || nz > max) {
                  break;
                }
                positions.setWinning(nx, ny, nz);
              }
            }
          }
        }
      }
    }
    long sumLosing = 0;
    for (int x = 0; x <= max; x++) {
      for (int y = x; y <= max; y++) {
        for (int z = y; z <= max; z++) {
          if (!positions.isWinning(x, y, z)) {
            sumLosing += x + y + z;
          }
        }
      }
    }
    return sumLosing;
  }

  private static class Positions {
    private final int max;
    private final BitSet winning;

    public Positions(int max) {
      this.max = max;
      winning = new BitSet((max + 1) * (max + 1) * (max + 1));
    }

    public boolean isWinning(int x, int y, int z) {
      return winning.get(x * (max + 1) * (max + 1) + y * (max + 1) + z);
    }

    public void setWinning(int x, int y, int z) {
      winning.set(x * (max + 1) * (max + 1) + y * (max + 1) + z);
    }
  }
}
