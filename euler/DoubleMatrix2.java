package euler;

import java.math.BigInteger;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

public class DoubleMatrix2 {
  private final double[][] m;

  private DoubleMatrix2(double[][] m) {
    this.m = m;
  }

  public double element(int row, int column) {
    return m[row][column];
  }

  public int rowCount() {
    return m.length;
  }

  public int columnCount() {
    return m.length == 0 ? 0 : m[0].length;
  }

  public static DoubleMatrix2 create(double[][] m) {
    for (double[] row : m) {
      checkArgument(row.length == m[0].length, "Different row lenghts in a");
    }
    return new DoubleMatrix2(m.clone());
  }

  public static DoubleMatrix2 identity(int rowCount, long mod) {
    double[][] res = zeroMatrix(rowCount, rowCount);
    for (int i = 0; i < rowCount; i++) {
      res[i][i] = 1;
    }
    return new DoubleMatrix2(res);
  }

  public DoubleMatrix2 add(DoubleMatrix2 that) {
    checkArgument(this.rowCount() == that.rowCount() && this.columnCount() == that.columnCount(),
        "Cannot add %s with %s", this, that);
    double[][] res = zeroMatrix(this.rowCount(), this.columnCount());
    for (int a = 0; a < rowCount(); a++) {
      for (int b = 0; b < columnCount(); b++) {
        res[a][b] = this.m[a][b] + that.m[a][b];
      }
    }
    return new DoubleMatrix2(res);
  }

  public DoubleMatrix2 multiply(DoubleMatrix2 that) {
    checkArgument(this.columnCount() == that.rowCount(),
        "Cannot multiply %s with %s", this, that);
//    return new LongModMatrix2(IntStream.range(0, this.rowCount()).parallel().mapToObj(a -> {
//      long[] row = new long[that.columnCount()];
//      for (int b = 0; b < that.columnCount(); b++) {
//        for (int c = 0; c < this.columnCount(); c++) {
//          row[b] = LongMath.checkedAdd(row[b], LongMath.checkedMultiply(this.m[a][c], that.m[c][b]));
////          row[b] += this.m[a][c] * that.m[c][b];
//          if (row[b] >= mod || row[b] < 0) {
//            row[b] = LongMath.mod(row[b], mod);
//          }
//        }
//      }
//      return row;
//    }).toArray(long[][]::new), mod);
    double[][] res = zeroMatrix(this.rowCount(), this.columnCount());
    for (int a = 0; a < this.rowCount(); a++) {
      for (int b = 0; b < that.columnCount(); b++) {
        for (int c = 0; c < this.columnCount(); c++) {
          res[a][b] += this.m[a][c] * that.m[c][b];
        }
      }
    }
    return new DoubleMatrix2(res);
  }

  public DoubleMatrix2 pow(long exp) {
    checkArgument(exp >= 1, "Unsupported exponent: %s", exp);
    if (exp == 1) {
      return this;
    }
    DoubleMatrix2 x = pow(exp / 2);
    if (exp % 2 == 0) {
      return x.multiply(x);
    } else {
      return x.multiply(x).multiply(this);
    }
  }

  public DoubleMatrix2 pow(BigInteger exp) {
    checkArgument(exp.compareTo(BigInteger.ONE) >= 0, "Unsupported exponent: %s", exp);
    if (exp.compareTo(BigInteger.ONE) == 0) {
      return this;
    }
    DoubleMatrix2 x = pow(exp.shiftRight(1));
    System.out.println(exp.bitLength());
    if (!exp.testBit(0)) {
      return x.multiply(x);
    } else {
      return x.multiply(x).multiply(this);
    }
  }

  public DoubleMatrix2 transpose() {
    double[][] res = zeroMatrix(columnCount(), rowCount());
    for (int row = 0; row < rowCount(); row++) {
      for (int column = 0; column < columnCount(); column++) {
        res[column][row] = m[row][column];
      }
    }
    return new DoubleMatrix2(res);
  }

  public DoubleMatrix2 removeRow(int row) {
    checkArgument(row >= 0 && row < rowCount(), "Cannot remove row %s from %s", row, this);
    double[][] res = new double[rowCount() - 1][];
    System.arraycopy(m, 0, res, 0, row);
    System.arraycopy(m, row + 1, res, row, rowCount() - row - 1);
    return new DoubleMatrix2(res);
  }

  public DoubleMatrix2 removeColumn(int column) {
    checkArgument(column >= 0 && column < columnCount(), "Cannot remove column %s from %s", column, this);
    double[][] res = new double[rowCount()][columnCount() - 1];
    for (int row = 0; row < rowCount(); row++) {
      System.arraycopy(m[row], 0, res[row], 0, column);
      System.arraycopy(m[row], column + 1, res[row], column, columnCount() - column - 1);
    }
    return new DoubleMatrix2(res);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DoubleMatrix2) {
      DoubleMatrix2 that = (DoubleMatrix2) obj;
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

  private static double[][] zeroMatrix(int rowCount, int columnCount) {
    double[][] m = new double[rowCount][];
    for (int row = 0; row < rowCount; row++) {
      m[row] = new double[columnCount];
    }
    return m;
  }
}