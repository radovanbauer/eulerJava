package euler;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class Problem161v2 {

  public static void main(String[] args) {
    System.out.println(new Problem161v2().solve(9, 12));
  }

  private static final Tile[] PATTERNS = {
    new Tile(new Point(0, 0), new Point(0, 1), new Point(0, 2)),
    new Tile(new Point(0, 0), new Point(1, 0), new Point(2, 0)),
    new Tile(new Point(0, 0), new Point(0, 1), new Point(1, 0)),
    new Tile(new Point(0, 0), new Point(0, 1), new Point(1, 1)),
    new Tile(new Point(0, 0), new Point(1, 0), new Point(1, 1)),
    new Tile(new Point(0, 0), new Point(1, 0), new Point(1, -1)),
  };

  public long solve(int n, int m) {
    boolean[][] grid = new boolean[n][m];
    List<boolean[]> tiles = Lists.newArrayList();
    for (Tile pattern : PATTERNS) {
      for (int x = 0; x < n; x++) {
        for (int y = 0; y < m; y++) {
          Tile tile = pattern.plus(new Point(x, y));
          if (fits(grid, tile)) {
            boolean[] tileArr = new boolean[n * m];
            for (Point point : tile.points) {
              tileArr[point.x * m + point.y] = true;
            }
            tiles.add(tileArr);
          }
        }
      }
    }
    return new DLX(tiles).solutionCount();
  }

  private boolean fits(boolean[][] grid, Tile tile) {
    for (Point point : tile.points) {
      if (!inside(grid, point.x, point.y) || grid[point.x][point.y]) {
        return false;
      }
    }
    return true;
  }

  private boolean inside(boolean[][] grid, int x, int y) {
    return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
  }

  private static class Tile {
    private final ImmutableSet<Point> points;

    public Tile(Iterable<Point> points) {
      this.points = ImmutableSet.copyOf(points);
    }

    public Tile(Point... points) {
      this(Arrays.asList(points));
    }

    public Tile plus(Point point) {
      List<Point> newPoints = Lists.newArrayList();
      for (Point thisPoint : points) {
        newPoints.add(thisPoint.plus(point));
      }
      return new Tile(newPoints);
    }

    @Override
    public int hashCode() {
      return points.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Tile) {
        Tile that = (Tile) obj;
        return this.points.equals(that.points);
      }
      return false;
    }

    @Override
    public String toString() {
      return points.toString();
    }
  }

  private static class Point {
    private final int x, y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public Point plus(Point that) {
      return new Point(this.x + that.x, this.y + that.y);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(x, y);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Point) {
        Point that = (Point) obj;
        return this.x == that.x
            && this.y == that.y;
      }
      return false;
    }

    @Override
    public String toString() {
      return String.format("[%d,%d]", x, y);
    }
  }
}
