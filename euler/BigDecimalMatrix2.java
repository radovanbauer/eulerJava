package euler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

public class BigDecimalMatrix2 {
  private final BigDecimal[][] m;
  private final MathContext ctx;

  private BigDecimalMatrix2(BigDecimal[][] m, MathContext ctx) {
    this.m = m;
    this.ctx = ctx;
  }

  public BigDecimal element(int row, int column) {
    return m[row][column];
  }

  public MathContext getMathContext() {
    return ctx;
  }

  public int rowCount() {
    return m.length;
  }

  public int columnCount() {
    return m.length == 0 ? 0 : m[0].length;
  }

  public static BigDecimalMatrix2 create128(double[][] m) {
    return create(m, MathContext.DECIMAL128);
  }

  public static BigDecimalMatrix2 create(double[][] m, MathContext ctx) {
    BigDecimal[][] copy = new BigDecimal[m.length][];
    for (int row = 0; row < m.length; row++) {
      copy[row] = new BigDecimal[m[row].length];
      for (int column = 0; column < m[row].length; column++) {
        copy[row][column] = BigDecimal.valueOf(m[row][column]);
      }
    }
    return create(copy, ctx);
  }

  public static BigDecimalMatrix2 create128(BigDecimal[][] m) {
    return create(m, MathContext.DECIMAL128);
  }

  public static BigDecimalMatrix2 create(BigDecimal[][] m, MathContext ctx) {
    for (BigDecimal[] row : m) {
      checkArgument(row.length == m[0].length, "Different row lenghts in a");
    }
    return new BigDecimalMatrix2(m.clone(), ctx);
  }

  public static BigDecimalMatrix2 identity(int rowCount, MathContext ctx) {
    BigDecimal[][] res = zeroMatrix(rowCount, rowCount);
    for (int i = 0; i < rowCount; i++) {
      res[i][i] = BigDecimal.ONE;
    }
    return new BigDecimalMatrix2(res, ctx);
  }

  public BigDecimalMatrix2 add(BigDecimalMatrix2 that) {
    checkArgument(this.rowCount() == that.rowCount() && this.columnCount() == that.columnCount(),
        "Cannot add %s with %s", this, that);
    BigDecimal[][] res = zeroMatrix(this.rowCount(), this.columnCount());
    for (int a = 0; a < rowCount(); a++) {
      for (int b = 0; b < columnCount(); b++) {
        res[a][b] = this.m[a][b].add(that.m[a][b], this.ctx);
      }
    }
    return new BigDecimalMatrix2(res, this.ctx);
  }

  public BigDecimalMatrix2 multiply(BigDecimalMatrix2 that) {
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
    BigDecimal[][] res = zeroMatrix(this.rowCount(), this.columnCount());
    for (int a = 0; a < this.rowCount(); a++) {
      for (int b = 0; b < that.columnCount(); b++) {
        for (int c = 0; c < this.columnCount(); c++) {
          res[a][b] = res[a][b].add(this.m[a][c].multiply(that.m[c][b], this.ctx), this.ctx);
        }
      }
    }
    return new BigDecimalMatrix2(res, this.ctx);
  }

  public BigDecimalMatrix2 pow(long exp) {
    checkArgument(exp >= 1, "Unsupported exponent: %s", exp);
    if (exp == 1) {
      return this;
    }
    BigDecimalMatrix2 x = pow(exp / 2);
    if (exp % 2 == 0) {
      return x.multiply(x);
    } else {
      return x.multiply(x).multiply(this);
    }
  }

  public BigDecimalMatrix2 pow(BigInteger exp) {
    checkArgument(exp.compareTo(BigInteger.ONE) >= 0, "Unsupported exponent: %s", exp);
    if (exp.compareTo(BigInteger.ONE) == 0) {
      return this;
    }
    BigDecimalMatrix2 x = pow(exp.shiftRight(1));
    System.out.println(exp.bitLength());
    if (!exp.testBit(0)) {
      return x.multiply(x);
    } else {
      return x.multiply(x).multiply(this);
    }
  }

  public BigDecimalMatrix2 transpose() {
    BigDecimal[][] res = zeroMatrix(columnCount(), rowCount());
    for (int row = 0; row < rowCount(); row++) {
      for (int column = 0; column < columnCount(); column++) {
        res[column][row] = m[row][column];
      }
    }
    return new BigDecimalMatrix2(res, ctx);
  }

  public BigDecimalMatrix2 removeRow(int row) {
    checkArgument(row >= 0 && row < rowCount(), "Cannot remove row %s from %s", row, this);
    BigDecimal[][] res = new BigDecimal[rowCount() - 1][];
    System.arraycopy(m, 0, res, 0, row);
    System.arraycopy(m, row + 1, res, row, rowCount() - row - 1);
    return new BigDecimalMatrix2(res, ctx);
  }

  public BigDecimalMatrix2 removeColumn(int column) {
    checkArgument(column >= 0 && column < columnCount(), "Cannot remove column %s from %s", column, this);
    BigDecimal[][] res = new BigDecimal[rowCount()][columnCount() - 1];
    for (int row = 0; row < rowCount(); row++) {
      System.arraycopy(m[row], 0, res[row], 0, column);
      System.arraycopy(m[row], column + 1, res[row], column, columnCount() - column - 1);
    }
    return new BigDecimalMatrix2(res, ctx);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BigDecimalMatrix2) {
      BigDecimalMatrix2 that = (BigDecimalMatrix2) obj;
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

  private static BigDecimal[][] zeroMatrix(int rowCount, int columnCount) {
    BigDecimal[][] m = new BigDecimal[rowCount][];
    for (int row = 0; row < rowCount; row++) {
      m[row] = new BigDecimal[columnCount];
      Arrays.fill(m[row], BigDecimal.ZERO);
    }
    return m;
  }
}