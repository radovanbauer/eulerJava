package euler;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;

import com.google.common.primitives.Ints;

public class IntArrayList extends AbstractList<Integer> {
  private int[] array;
  private int size;

  public IntArrayList() {
    this.array = new int[10];
    this.size = 0;
  }

  public IntArrayList(int[] array) {
    this.array = array.clone();
    this.size = array.length;
  }

  @Override
  public Integer get(int index) {
    return getInt(index);
  }

  public int getInt(int index) {
    rangeCheck(index);
    return array[index];
  }

  @Override
  public boolean add(Integer element) {
    return addInt(element);
  }

  public boolean addInt(int element) {
    array = Ints.ensureCapacity(array, size + 1, size >> 1);
    array[size] = element;
    size++;
    return true;
  }

  @Override
  public void add(int index, Integer element) {
    addInt(index, element);
  }

  public void addInt(int index, int element) {
    rangeCheckForAdd(index);
    array = Ints.ensureCapacity(array, size + 1, size >> 1);
    System.arraycopy(array, index, array, index + 1, size - index);
    array[index] = element;
    size++;
  }

  @Override
  public boolean addAll(Collection<? extends Integer> c) {
    if (c instanceof IntArrayList) {
      IntArrayList that = (IntArrayList) c;
      array = Ints.ensureCapacity(this.array, this.size + that.size, (this.size + that.size) >> 1);
      System.arraycopy(that.array, 0, this.array, this.size, that.size);
      size += that.size;
      return that.size != 0;
    } else {
      return super.addAll(c);
    }
  }

  @Override
  public Integer set(int index, Integer element) {
    return setInt(index, element);
  }

  public int setInt(int index, int element) {
    rangeCheck(index);
    int previous = array[index];
    array[index] = element;
    return previous;
  }

  @Override
  public Integer remove(int index) {
    return removeInt(index);
  }

  public int removeInt(int index) {
    rangeCheck(index);
    int previous = array[index];
    int numMoved = size - index - 1;
    if (numMoved > 0) {
        System.arraycopy(array, index+1, array, index, numMoved);
    }
    size--;
    return previous;
  }

  public int[] toIntArray() {
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