package euler;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;

import com.google.common.primitives.Doubles;

public class DoubleArrayList extends AbstractList<Double> {
  private double[] array;
  private int size;

  public DoubleArrayList() {
    this.array = new double[10];
    this.size = 0;
  }

  @Override
  public Double get(int index) {
    return getDouble(index);
  }

  public double getDouble(int index) {
    rangeCheck(index);
    return array[index];
  }

  @Override
  public boolean add(Double element) {
    return addDouble(element);
  }

  public boolean addDouble(double element) {
    array = Doubles.ensureCapacity(array, size + 1, size >> 1);
    array[size] = element;
    size++;
    return true;
  }

  @Override
  public void add(int index, Double element) {
    addDouble(index, element);
  }

  public void addDouble(int index, double element) {
    rangeCheckForAdd(index);
    array = Doubles.ensureCapacity(array, size + 1, size >> 1);
    System.arraycopy(array, index, array, index + 1, size - index);
    array[index] = element;
    size++;
  }

  @Override
  public boolean addAll(Collection<? extends Double> c) {
    if (c instanceof DoubleArrayList) {
      DoubleArrayList that = (DoubleArrayList) c;
      array = Doubles.ensureCapacity(this.array, this.size + that.size, (this.size + that.size) >> 1);
      System.arraycopy(that.array, 0, this.array, this.size, that.size);
      size += that.size;
      return that.size != 0;
    } else {
      return super.addAll(c);
    }
  }

  @Override
  public Double set(int index, Double element) {
    return setDouble(index, element);
  }

  public double setDouble(int index, double element) {
    rangeCheck(index);
    double previous = array[index];
    array[index] = element;
    return previous;
  }

  @Override
  public Double remove(int index) {
    return removeDouble(index);
  }

  public double removeDouble(int index) {
    rangeCheck(index);
    double previous = array[index];
    int numMoved = size - index - 1;
    if (numMoved > 0) {
        System.arraycopy(array, index+1, array, index, numMoved);
    }
    size--;
    return previous;
  }

  public double[] toDoubleArray() {
    return Arrays.copyOfRange(array, 0, size);
  }

  private void rangeCheck(int index) {
    if (index >= size || index < 0) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
  }

  private void rangeCheckForAdd(int index) {
    if (index > size || index < 0) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
  }

  @Override
  public int size() {
    return size;
  }
}