package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;

public class LongMatrix {
  private final long[][] m;

  private LongMatrix(long[][] m) {
    this.m = m;
  }

  public long element(int row, int column) {
    return m[row][column];
  }

  public int rowCount() {
    return m.length;
  }

  public int columnCount() {
    return m.length == 0 ? 0 : m[0].length;
  }

  public static LongMatrix create(long[][] m) {
    for (long[] row : m) {
      Preconditions.checkArgument(row.length == m[0].length, "Different row lenghts in a");
    }
    return new LongMatrix(m.clone());
  }

  public LongMatrix add(LongMatrix that) {
    checkArgument(this.rowCount() == that.rowCount() && this.columnCount() == that.columnCount(),
        "Cannot add %s with %s", this, that);
    long[][] res = zeroMatrix(this.rowCount(), this.columnCount());
    for (int a = 0; a < rowCount(); a++) {
      for (int b = 0; b < columnCount(); b++) {
        res[a][b] = LongMath.checkedAdd(this.m[a][b], that.m[a][b]);
      }
    }
    return new LongMatrix(res);
  }

  public LongMatrix multiply(LongMatrix that) {
    checkArgument(this.columnCount() == that.rowCount(),
        "Cannot multiply %s with %s", this, that);
    return new LongMatrix(IntStream.range(0, this.rowCount()).sequential().mapToObj(a -> {
      long[] row = new long[that.columnCount()];
      for (int b = 0; b < that.columnCount(); b++) {
        for (int c = 0; c < this.columnCount(); c++) {
          row[b] = LongMath.checkedAdd(row[b],
              LongMath.checkedMultiply(this.m[a][c], that.m[c][b]));
        }
      }
      return row;
    }).toArray(long[][]::new));
  }

  public LongMatrix mod(long mod) {
    long[][] res = zeroMatrix(rowCount(), columnCount());
    for (int a = 0; a < rowCount(); a++) {
      for (int b = 0; b < columnCount(); b++) {
        res[a][b] = LongMath.mod(this.m[a][b], mod);
      }
    }
    return new LongMatrix(res);
  }

  public LongMatrix pow(long exp) {
    checkArgument(exp >= 1, "Unsupported exponent: %s", exp);
    if (exp == 1) {
      return this;
    }
    LongMatrix x = pow(exp / 2);
    if (exp % 2 == 0) {
      return x.multiply(x);
    } else {
      return x.multiply(x).multiply(this);
    }
  }

  public LongMatrix powMod(long exp, long mod) {
    checkArgument(exp >= 1, "Unsupported exponent: %s", exp);
    if (exp == 1) {
      return this;
    }
    LongMatrix x = powMod(exp / 2, mod);
    if (exp % 2 == 0) {
      return x.multiply(x).mod(mod);
    } else {
      return x.multiply(x).mod(mod).multiply(this).mod(mod);
    }
  }

  public LongMatrix removeRow(int row) {
    checkArgument(row >= 0 && row < rowCount(), "Cannot remove row %s from %s", row, this);
    long[][] res = new long[rowCount() - 1][];
    System.arraycopy(m, 0, res, 0, row);
    System.arraycopy(m, row + 1, res, row, rowCount() - row - 1);
    return new LongMatrix(res);
  }

  public LongMatrix removeColumn(int column) {
    checkArgument(column >= 0 && column < columnCount(), "Cannot remove column %s from %s", column, this);
    long[][] res = new long[rowCount()][columnCount() - 1];
    for (int row = 0; row < rowCount(); row++) {
      System.arraycopy(m[row], 0, res[row], 0, column);
      System.arraycopy(m[row], column + 1, res[row], column, columnCount() - column - 1);
    }
    return new LongMatrix(res);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof LongMatrix) {
      LongMatrix that = (LongMatrix) obj;
      return Arrays.deepEquals(this.m, that.m);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(m);
  }

  @Override
  public String toString() {
    return Arrays.deepToString(m);
  }

  private long[][] zeroMatrix(int rowCount, int columnCount) {
    long[][] m = new long[rowCount][];
    for (int row = 0; row < rowCount; row++) {
      m[row] = new long[columnCount];
    }
    return m;
  }
}