package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.Array;
import java.util.Arrays;

public abstract class Matrix<T, M extends Matrix<T, M>> {
  protected final T[][] m;

  protected Matrix(T[][] m) {
    this.m = m;
  }

  protected abstract Domain<T> domain();
  protected abstract M newInstance(T[][] m);

  public T element(int row, int column) {
    return m[row][column];
  }

  public int rowCount() {
    return m.length;
  }

  public int columnCount() {
    return m.length == 0 ? 0 : m[0].length;
  }

  public M add(M that) {
    checkArgument(this.rowCount() == that.rowCount() && this.columnCount() == that.columnCount(),
        "Cannot add %s with %s", this, that);
    T[][] res = zeroMatrix(this.rowCount(), this.columnCount());
    for (int a = 0; a < rowCount(); a++) {
      for (int b = 0; b < columnCount(); b++) {
        res[a][b] = domain().add(this.m[a][b], that.m[a][b]);
      }
    }
    return newInstance(res);
  }

  public M multiply(M that) {
    checkArgument(this.columnCount() == that.rowCount(),
        "Cannot multiply %s with %s", this, that);
    T[][] res = zeroMatrix(this.rowCount(), that.columnCount());
    for (int a = 0; a < this.rowCount(); a++) {
      for (int b = 0; b < that.columnCount(); b++) {
        for (int c = 0; c < this.columnCount(); c++) {
          res[a][b] = domain().add(res[a][b],
              domain().multiply(this.m[a][c], that.m[c][b]));
        }
      }
    }
    return newInstance(res);
  }

  public M pow(long exp) {
    checkArgument(exp >= 1, "Unsupported exponent: %s", exp);
    if (exp == 1) {
      return (M) this;
    }
    M x = pow(exp / 2);
    if (exp % 2 == 0) {
      return x.multiply(x);
    } else {
      return this.multiply(x).multiply(x);
    }
  }

  public M removeRow(int row) {
    checkArgument(row >= 0 && row < rowCount(), "Cannot remove row %s from %s", row, this);
    T[][] res = (T[][]) Array.newInstance(Object.class, rowCount() - 1);
    System.arraycopy(m, 0, res, 0, row);
    System.arraycopy(m, row + 1, res, row, rowCount() - row - 1);
    return newInstance(res);
  }

  public M removeColumn(int column) {
    checkArgument(column >= 0 && column < columnCount(), "Cannot remove column %s from %s", column, this);
    T[][] res = zeroMatrix(rowCount(), columnCount() - 1);
    for (int row = 0; row < rowCount(); row++) {
      System.arraycopy(m[row], 0, res[row], 0, column);
      System.arraycopy(m[row], column + 1, res[row], column, columnCount() - column - 1);
    }
    return newInstance(res);
  }

  public M transpose() {
    T[][] res = zeroMatrix(columnCount(), rowCount());
    for (int row = 0; row < rowCount(); row++) {
      for (int column = 0; column < columnCount(); column++) {
        res[column][row] = m[row][column];
      }
    }
    return newInstance(res);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this.getClass() == obj.getClass()) {
      Matrix<?, ?> that = (Matrix<?, ?>) obj;
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

  @SuppressWarnings("unchecked")
  private T[][] zeroMatrix(int rowCount, int columnCount) {
    T[][] m = (T[][]) Array.newInstance(Array.newInstance(domain().clazz(), 0).getClass(), rowCount);
    for (int row = 0; row < rowCount; row++) {
      m[row] = (T[]) Array.newInstance(domain().clazz(), columnCount);
      Arrays.fill(m[row], domain().zero());
    }
    return m;
  }
}