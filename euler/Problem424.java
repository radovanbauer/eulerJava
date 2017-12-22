package euler;

import com.google.auto.value.AutoValue;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import com.google.common.primitives.Ints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.Integer.bitCount;
import static java.lang.Integer.numberOfTrailingZeros;

public class Problem424 {
  public static void main(String[] args) {
    Runner.run(new Problem424()::solve);
  }

  public long solve() {
    try {
      List<String> lines = CharStreams.readLines(new BufferedReader(new InputStreamReader(
          getClass().getClassLoader().getResourceAsStream("euler/p424_kakuro200.txt"), StandardCharsets.UTF_8)));
      AtomicInteger count = new AtomicInteger();
      return lines.parallelStream().mapToLong(line -> {
        long result = solve(parse(line));
        System.out.printf("%d/%d\n", count.incrementAndGet(), lines.size());
        return result;
      }).sum();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private long solve(Grid grid) {
    Long solution = null;
    Collection<List<Integer>> permutations = Collections2.permutations(ImmutableList.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    Constraints constraints = Constraints.fromGrid(grid);
    for (List<Integer> permutation : permutations) {
      Assignment assignment = new Assignment(permutation);
      if (!constraints.validAssignment(assignment)) {
        continue;
      }
      Kakuro kakuro = Kakuro.fromGrid(grid, constraints, assignment);
      if (kakuro.hasSolution()) {
        System.out.println(permutation);
        System.out.println(kakuro);
        long newSolution = Long.valueOf(permutation.stream().map(String::valueOf).collect(Collectors.joining()));
        checkState(solution == null, "%s vs %s", solution, newSolution);
        solution = newSolution;
      }
    }
    return solution;
  }

  private static class Kakuro {
    private final int n;
    private final Grid grid;
    private final Assignment assignment;
    private int[][] cells;
    private final Constraints constraints;

    private Kakuro(Grid grid, Assignment assignment, int[][] cells, Constraints constraints) {
      this.n = grid.n();
      this.grid = grid;
      this.assignment = assignment;
      this.cells = cells;
      this.constraints = checkNotNull(constraints);
    }

    static Kakuro fromGrid(Grid grid, Constraints constraints, Assignment assignment) {
      int[][] cells = new int[grid.n()][grid.n()];
      for (int r = 0; r < grid.n(); r++) {
        for (int c = 0; c < grid.n(); c++) {
          Cell cell = grid.cell(r, c);
          if (cell.type() == Type.NUMBER) {
            int number = assignment.translate(cell.number().get());
            checkState(number >= 0 && number <= 9);
            if (number == 0) {
              cells[r][c] = 0;
            } else {
              cells[r][c] = 1 << number;
            }
          } else if (cell.type() == Type.O) {
            cells[r][c] = (1 << 10) - 2;
          } else {
            cells[r][c] = -1;
          }
        }
      }
      Kakuro kakuro = new Kakuro(grid, assignment, cells, constraints);
      List<Point> pointsToProcess = new ArrayList<>();
      for (int r = 0; r < grid.n(); r++) {
        for (int c = 0; c < grid.n(); c++) {
          if (kakuro.cells[r][c] >= 0) {
            pointsToProcess.add(Point.create(r, c));
          }
        }
      }
      kakuro.process(pointsToProcess);
      return kakuro;
    }

    boolean hasSolution() {
      int min = 10;
      Point minPoint = null;
      for (int r = 0; r < n; r++) {
        for (int c = 0; c < n; c++) {
          if (cells[r][c] == 0) {
            return false;
          } else if (cells[r][c] > 0) {
            int bitCount = bitCount(cells[r][c]);
            if (bitCount > 1 && bitCount < min) {
              min = bitCount;
              minPoint = Point.create(r, c);
            }
          }
        }
      }
      if (minPoint == null) {
        return isValidSolution();
      }
      int cell = cells[minPoint.r()][minPoint.c()];
      for (int d = 1; d <= 9; d++) {
        if ((cell & (1 << d)) != 0) {
          int[][] oldCells = copyCells();
          cells[minPoint.r()][minPoint.c()] = 1 << d;
          process(ImmutableList.of(minPoint));
          if (hasSolution()) {
            return true;
          }
          this.cells = oldCells;
        }
      }
      return false;
    }

    private boolean isValidSolution() {
      for (Constraint constraint : constraints.getAll()) {
        int sum = 0;
        int used = 0;
        for (Point point : constraint.points()) {
          int cell = cells[point.r()][point.c()];
          if (cell <= 0) {
            return false;
          }
          if (bitCount(cell) != 1) {
            return false;
          }
          int val = numberOfTrailingZeros(cell);
          if ((used & cell) != 0) {
            return false;
          }
          used |= cell;
          sum += val;
        }
        if (sum != constraint.sum(assignment)) {
          return false;
        }
      }
      return true;
    }

    private int[][] copyCells() {
      int[][] newCells = new int[n][];
      for (int i = 0; i < n; i++) {
        newCells[i] = cells[i].clone();
      }
      return newCells;
    }

    private void process(Iterable<Point> points) {
      Set<Constraint> queue = new HashSet<>();
      for (Point point : points) {
        checkState(cells[point.r()][point.c()] >= 0);
        queue.addAll(constraints.get(point));
      }
      while (!queue.isEmpty()) {
        Constraint constraint = queue.iterator().next();
        List<Point> changedPoints = process(constraint);
        if (changedPoints == null) {
          return;
        }
        for (Point changedPoint : changedPoints) {
          queue.addAll(constraints.get(changedPoint));
        }
        queue.remove(constraint);
      }
    }

    private List<Point> process(Constraint constraint) {
      int sum = constraint.sum(assignment);
      int used = 0;
      boolean zero = false;
      List<Point> freePoints = new ArrayList<>();
      List<Change> changes = new ArrayList<>();
      for (Point point : constraint.points()) {
        int cell = cells[point.r()][point.c()];
        if (cell == 0) {
          zero = true;
        } else if (bitCount(cell) == 1) {
          sum -= Integer.numberOfTrailingZeros(cell);
          used |= cell;
        } else {
          freePoints.add(point);
        }
      }
      if (zero || bitCount(used) != constraint.points().size() - freePoints.size()) {
        for (Point point : constraint.points()) {
          changes.add(Change.create(point, 0));
        }
      } else if (freePoints.size() == 0) {
        if (sum != 0) {
          for (Point point : constraint.points()) {
            changes.add(Change.create(point, 0));
          }
        }
      } else if (freePoints.size() == 1) {
        int cell = cells[freePoints.get(0).r()][freePoints.get(0).c()];
        if ((cell & (1 << sum)) != 0 && (used & (1 << sum)) == 0 && sum >= 1 && sum <= 9) {
          changes.add(Change.create(freePoints.get(0), 1 << sum));
        } else {
          changes.add(Change.create(freePoints.get(0), 0));
        }
      } else {
        int loCount = 0;
        int loSum = 0;
        int max = 0;
        for (int i = 1; i <= 9; i++) {
          if ((used & (1 << i)) == 0) {
            if (loCount < freePoints.size() - 1) {
              loSum += i;
              loCount++;
            } else {
              if (loSum + i <= sum) {
                max = i;
              }
            }
          }
        }
        int hiCount = 0;
        int hiSum = 0;
        int min = 10;
        for (int i = 9; i >= 1; i--) {
          if ((used & (1 << i)) == 0) {
            if (hiCount < freePoints.size() - 1) {
              hiSum += i;
              hiCount++;
            } else {
              if (hiSum + i >= sum) {
                min = i;
              }
            }
          }
        }
        int options = 0;
        for (int i = min; i <= max; i++) {
          if ((used & (1 << i)) == 0) {
            options |= 1 << i;
          }
        }
        if (bitCount(options) >= freePoints.size()) {
          for (Point point : freePoints) {
            changes.add(Change.create(point, cells[point.r()][point.c()] & options));
          }
        } else {
          for (Point point : freePoints) {
            changes.add(Change.create(point, 0));
          }
        }
      }
      List<Point> changed = new ArrayList<>();
      boolean newZero = false;
      for (Change change : changes) {
        Point point = change.point();
        int oldValue = cells[point.r()][point.c()];
        int newValue = change.newValue();
        if (newValue == 0) {
          newZero = true;
        }
        if (oldValue != newValue) {
          checkState((oldValue | newValue) == oldValue);
          changed.add(point);
          cells[point.r()][point.c()] = newValue;
        }
      }
      return newZero ? null : changed;
    }

    @Override
    public String toString() {
      StringBuilder result = new StringBuilder();
      for (int r = 0; r < n; r++) {
        for (int c = 0; c < n; c++) {
          Cell cell = grid.cell(r, c);
          String cellString = "";
          if (cell.type() == Type.X) {
            cellString = "X";
          } else if (cell.type() == Type.SUM) {
            if (cell.vSum().isPresent()) {
              cellString += cell.vSum().get() + "=" + assignment.translate(cell.vSum().get());
            }
            cellString += "/";
            if (cell.hSum().isPresent()) {
              cellString += cell.hSum().get() + "=" + assignment.translate(cell.hSum().get());
            }
          } else {
            for (int d = 1; d <= 9; d++) {
              if ((cells[r][c] & (1 << d)) != 0) {
                cellString += d;
              }
            }
          }
          result.append(cellString);
          for (int i = 0; i < 11 - cellString.length(); i++) {
            result.append(' ');
          }
        }
        result.append("\n");
      }
      return result.toString();
    }
  }

  @AutoValue
  abstract static class Change {
    abstract Point point();
    abstract int newValue();

    static Change create(Point point, int newValue) {
      return new AutoValue_Problem424_Change(point, newValue);
    }
  }

  static class Constraints {
    private final ImmutableList<Constraint> allConstraints;
    private final Constraint[][][] constraints;

    Constraints(Grid grid, Iterable<Constraint> constraints) {
      this.allConstraints = ImmutableList.copyOf(constraints);
      this.constraints = new Constraint[2][grid.n()][grid.n()];
      for (Constraint constraint : constraints) {
        for (Point point : constraint.points()) {
          if (this.constraints[0][point.r()][point.c()] == null) {
            this.constraints[0][point.r()][point.c()] = constraint;
          } else {
            checkState(this.constraints[1][point.r()][point.c()] == null);
            this.constraints[1][point.r()][point.c()] = constraint;
          }
        }
      }
      for (int r = 0; r < grid.n(); r++) {
        for (int c = 0; c < grid.n(); c++) {
          Cell cell = grid.cell(r, c);
          if (cell.type() == Type.O || cell.type() == Type.NUMBER) {
            checkState(this.constraints[0][r][c] != null && this.constraints[1][r][c] != null);
          }
        }
      }
    }

    static Constraints fromGrid(Grid grid) {
      List<Constraint> constraints = new ArrayList<>();
      for (int r = 0; r < grid.n(); r++) {
        for (int c = 0; c < grid.n(); c++) {
          Cell cell = grid.cell(r, c);
          if (cell.type() == Type.SUM) {
            if (cell.vSum().isPresent()) {
              List<Point> points = new ArrayList<>();
              for (int r2 = r + 1; r2 < grid.n() && (grid.cell(r2, c).type() == Type.O || grid.cell(r2, c).type() == Type.NUMBER); r2++) {
                points.add(Point.create(r2, c));
              }
              checkState(!points.isEmpty());
              constraints.add(Constraint.create(cell.vSum().get(), points));
            }
            if (cell.hSum().isPresent()) {
              List<Point> points = new ArrayList<>();
              for (int c2 = c + 1; c2 < grid.n() && (grid.cell(r, c2).type() == Type.O || grid.cell(r, c2).type() == Type.NUMBER); c2++) {
                points.add(Point.create(r, c2));
              }
              checkState(!points.isEmpty());
              constraints.add(Constraint.create(cell.hSum().get(), points));
            }
          }
        }
      }
      return new Constraints(grid, constraints);
    }

    List<Constraint> get(Point point) {
      checkNotNull(constraints[0][point.r()][point.c()]);
      return ImmutableList.of(constraints[0][point.r()][point.c()], constraints[1][point.r()][point.c()]);
    }

    ImmutableList<Constraint> getAll() {
      return allConstraints;
    }

    boolean validAssignment(Assignment assignment) {
      return allConstraints.stream().allMatch(constraint -> constraint.validAssignment(assignment));
    }
  }

  static class Assignment {
    private final int[] assignment;

    Assignment(Collection<Integer> assignment) {
      this.assignment = Ints.toArray(assignment);
    }

    int translate(String string) {
      int result = 0;
      for (int i = 0; i < string.length(); i++) {
        result *= 10;
        result += assignment[string.charAt(i) - 'A'];
      }
      return result;
    }
  }

  @AutoValue
  abstract static class Constraint {
    abstract String sum();
    abstract ImmutableList<Point> points();

    int sum(Assignment assignment) {
      return assignment.translate(sum());
    }

    boolean validAssignment(Assignment assignment) {
      int firstDigit = assignment.translate(sum().substring(0, 1));
      int sum = assignment.translate(sum());
      int max = 9;
      int min = 9 - points().size() + 1;
      return firstDigit != 0 && sum <= (max + min) * points().size() / 2;
    }

    static Constraint create(String sum, Iterable<Point> points) {
      return new AutoValue_Problem424_Constraint(sum, ImmutableList.copyOf(points));
    }
  }

  @AutoValue
  abstract static class Point {
    abstract int r();
    abstract int c();

    static Point create(int x, int y) {
      return new AutoValue_Problem424_Point(x, y);
    }
  }

  private Grid parse(String line) {
    Matcher matcher = Pattern.compile("([0-9A-JOX]+|\\([^()]+\\))(,|$)").matcher(line);
    List<String> cellStrings = new ArrayList<>();
    while (matcher.find()) {
      cellStrings.add(matcher.group(1));
    }
    int n = Integer.parseInt(cellStrings.get(0));
    Cell[][] cells = new Cell[n][n];
    for (int r = 0; r < n; r++) {
      for (int c = 0; c < n; c++) {
        int idx = 1 + r * n + c;
        cells[r][c] = parseCell(cellStrings.get(idx));
      }
    }
    return new Grid(cells);
  }

  private Cell parseCell(String cellString) {
    switch (cellString.charAt(0)) {
      case 'O':
        return Cell.create(Type.O, Optional.empty(), Optional.empty(), Optional.empty());
      case 'X':
        return Cell.create(Type.X, Optional.empty(), Optional.empty(), Optional.empty());
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'I':
      case 'J':
        return Cell.create(Type.NUMBER, Optional.empty(), Optional.empty(), Optional.of(cellString));
      case '(':
        Matcher hSumMatcher = Pattern.compile("h[A-Z]+").matcher(cellString);
        Matcher vSumMatcher = Pattern.compile("v[A-Z]+").matcher(cellString);
        Optional<String> hSum = Optional.empty();
        Optional<String> vSum = Optional.empty();
        if (hSumMatcher.find()) {
          hSum = Optional.of(hSumMatcher.group().substring(1));
        }
        if (vSumMatcher.find()) {
          vSum = Optional.of(vSumMatcher.group().substring(1));
        }
        return Cell.create(Type.SUM, vSum, hSum, Optional.empty());
      default:
        throw new AssertionError();
    }
  }

  enum Type {
    X,
    O,
    SUM,
    NUMBER
  }

  static class Grid {
    private final Cell[][] cells;

    Grid(Cell[][] cells) {
      this.cells = checkNotNull(cells);
    }

    int n() {
      return cells.length;
    }

    Cell cell(int r, int c) {
      return cells[r][c];
    }
  }

  @AutoValue
  static abstract class Cell {
    abstract Type type();
    abstract Optional<String> vSum();
    abstract Optional<String> hSum();
    abstract Optional<String> number();

    static Cell create(Type type, Optional<String> vSum, Optional<String> hSum, Optional<String> number) {
      return new AutoValue_Problem424_Cell(type, vSum, hSum, number);
    }
  }
}
