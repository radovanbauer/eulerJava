package euler;

import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;
import com.google.common.collect.Comparators;
import com.google.common.collect.ImmutableList;

import java.math.BigInteger;
import java.util.*;

import static com.google.common.base.Preconditions.checkState;
import static euler.Problem324.Tile.*;

public class Problem324 {
  public static void main(String[] args) {
    Runner.run(new Problem324()::solve);
  }

  public long solve() {
    long mod = 100000007;
    ArrayList<Tile> tiles = new ArrayList<>();
    for (int n = 1; n <= 9; n++) {
      tiles.add(null);
    }
    HashSet<Grid> gridSet = new HashSet<>();
    generateTiles(tiles, 0, gridSet);
    ImmutableList<Grid> allGrids = ImmutableList.sortedCopyOf(gridSet);
    int allN = allGrids.size();

    Map<Grid, Integer> idxMap = new HashMap<>();
    List<Grid> normalized = new ArrayList<>();
    int[] map = new int[allN];
    for (int i = 0; i < allN; i++) {
      Grid normalizedGrid = allGrids.get(i).normalized();
      if (!idxMap.containsKey(normalizedGrid)) {
        normalized.add(normalizedGrid);
        idxMap.put(normalizedGrid, normalized.size() - 1);
      }
      map[i] = idxMap.get(normalizedGrid);
    }

    for (int i = 0; i < normalized.size(); i++) {
      for (Grid similar : new HashSet<>(normalized.get(i).similarGrids())) {
        checkState(allGrids.contains(similar), similar.toString());
      }

      for (int j = i + 1; j < normalized.size(); j++) {
        checkState(!normalized.get(i).normalized().equals(normalized.get(j).normalized()));
      }
    }

    int normalizedN = normalized.size();
    System.out.println(idxMap.size());

    long[][] startingArr = new long[1][normalizedN];
    for (int i = 0; i < allN; i++) {
      if (!allGrids.get(i).tiles().contains(U)) {
        startingArr[0][map[i]]++;
      }
    }
    LongModMatrix2 starting = LongModMatrix2.create(startingArr, mod);

    long[][] transitionArr = new long[normalizedN][normalizedN];
    for (int i = 0; i < allN; i++) {
      if (!allGrids.get(i).normalized().equals(allGrids.get(i))) {
        continue;
      }
      for (int j = 0; j < allN; j++) {
        boolean good = true;
        for (int t = 0; t < 9; t++) {
          if ((allGrids.get(i).tiles().get(t) == D && allGrids.get(j).tiles().get(t) != U)
              || (allGrids.get(i).tiles().get(t) != D && allGrids.get(j).tiles().get(t) == U)) {
            good = false;
          }
        }
        if (good) {
          transitionArr[map[i]][map[j]]++;
          if (map[j] == 4) {
            System.out.println(map[i] + " " + startingArr[0][map[i]] + " " + allGrids.get(i) + "->" + allGrids.get(j));
          }
        }
      }
    }
    LongModMatrix2 transition = LongModMatrix2.create(transitionArr, mod);

//    Set<LongModMatrix2> seen = new HashSet<>();
//    seen.add(starting);
//
//    LongModMatrix2 mat = starting;
//    for (int i = 2;; i++) {
//      mat = mat.multiply(transition);
//      System.out.println(i);
//      if (seen.contains(mat)) {
//        System.out.println("Found");
//        break;
//      }
//      seen.add(mat);
//    }

    LongModMatrix2 res = starting.multiply(transition.pow(BigInteger.valueOf(10).pow(10_000).subtract(BigInteger.ONE)));

    long count = 0;
    for (int i = 0; i < normalizedN; i++) {
      if (!normalized.get(i).tiles().contains(D)) {
        count += res.element(0, i);
      }
    }

    return count % mod;
  }

  private void generateTiles(List<Tile> tiles, int next, Set<Grid> grids) {
    if (next == 9) {
      grids.add(Grid.create(tiles));
    } else {
      if (tiles.get(next) == null) {
        int y = next / 3;
        int x = next % 3;

        Set<Tile> possible = new HashSet<>();

        if (x > 0 && tiles.get(next - 1) == W) {
          checkState(!(y > 0 && tiles.get(next - 3) == S));
          possible.add(E);
        } else if (y > 0 && tiles.get(next - 3) == S) {
          possible.add(N);
        } else {
          possible.add(U);
          possible.add(D);
          if (x < 2 && !(y > 0 && tiles.get(next - 2) == S)) {
            possible.add(W);
          }
          if (y < 2) {
            possible.add(S);
          }
        }

        for (Tile nextTile : possible) {
          tiles.set(next, nextTile);
          generateTiles(tiles, next + 1, grids);
          tiles.set(next, null);
        }
      }
    }
  }

  @AutoValue
  static abstract class Grid implements Comparable<Grid> {

    private static final Comparator<Iterable<Tile>> LEXICOGRAPHICAL =
        Comparators.lexicographical(Comparator.<Tile>naturalOrder());

    abstract ImmutableList<Tile> tiles();

    static Grid create(Iterable<Tile> tiles) {
      ImmutableList<Tile> copy = ImmutableList.copyOf(tiles);
      Preconditions.checkArgument(copy.size() == 9);
      return new AutoValue_Problem324_Grid(copy);
    }

    Grid rotate() {
      Tile[] newTiles = new Tile[9];
      for (int x = 0; x < 3; x++) {
        for (int y = 0; y < 3; y++) {
          int nx = 2 - y;
          int ny = x;
          newTiles[ny * 3 + nx] =  tiles().get(y * 3 + x).rotate();
        }
      }
      return create(Arrays.asList(newTiles));
    }

    Grid reflect() {
      Tile[] newTiles = new Tile[9];
      for (int x = 0; x < 3; x++) {
        for (int y = 0; y < 3; y++) {
          int nx = 2 - x;
          int ny = y;
          newTiles[ny * 3 + nx] = tiles().get(y * 3 + x).reflect();
        }
      }
      return create(Arrays.asList(newTiles));
    }

    Grid normalized() {
      return Collections.min(similarGrids());
    }

    List<Grid> similarGrids() {
      Grid r1 = this.rotate();
      Grid r2 = r1.rotate();
      Grid r3 = r2.rotate();
      return Arrays.asList(this, r1, r2, r3, this.reflect(), r1.reflect(), r2.reflect(), r3.reflect());
    }

    @Override
    public String toString() {
      return String.format("[%s%s%s,%s%s%s,%s%s%s]", tiles().toArray());
    }

    @Override
    public int compareTo(Grid that) {
      return LEXICOGRAPHICAL.compare(this.tiles(), that.tiles());
    }
  }

  enum Tile {
    U, D, W, N, E, S;

    Tile rotate() {
      switch (this) {
        case U:
          return U;
        case D:
          return D;
        case W:
          return S;
        case N:
          return W;
        case E:
          return N;
        case S:
          return E;
        default:
          throw new AssertionError();
      }
    }

    Tile reflect() {
      switch (this) {
        case U:
          return U;
        case D:
          return D;
        case W:
          return E;
        case N:
          return N;
        case E:
          return W;
        case S:
          return S;
        default:
          throw new AssertionError();
      }
    }
  }
}
