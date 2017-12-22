package euler;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;

import com.google.common.primitives.Longs;

public class LongArrayList extends AbstractList<Long> {
  private long[] array;
  private int size;

  public LongArrayList() {
    this.array = new long[10];
    this.size = 0;
  }

  @Override
  public Long get(int index) {
    return getLong(index);
  }

  public long getLong(int index) {
    rangeCheck(index);
    return array[index];
  }

  @Override
  public boolean add(Long element) {
    return addLong(element);
  }

  public boolean addLong(long element) {
    array = Longs.ensureCapacity(array, size + 1, size >> 1);
    array[size] = element;
    size++;
    return true;
  }

  @Override
  public void add(int index, Long element) {
    addLong(index, element);
  }

  public void addLong(int index, long element) {
    rangeCheckForAdd(index);
    array = Longs.ensureCapacity(array, size + 1, size >> 1);
    System.arraycopy(array, index, array, index + 1, size - index);
    array[index] = element;
    size++;
  }

  @Override
  public boolean addAll(Collection<? extends Long> c) {
    if (c instanceof LongArrayList) {
      LongArrayList that = (LongArrayList) c;
      array = Longs.ensureCapacity(this.array, this.size + that.size, (this.size + that.size) >> 1);
      System.arraycopy(that.array, 0, this.array, this.size, that.size);
      size += that.size;
      return that.size != 0;
    } else {
      return super.addAll(c);
    }
  }

  @Override
  public Long set(int index, Long element) {
    return setLong(index, element);
  }

  public long setLong(int index, long element) {
    rangeCheck(index);
    long previous = array[index];
    array[index] = element;
    return previous;
  }

  @Override
  public Long remove(int index) {
    return removeLong(index);
  }

  public long removeLong(int index) {
    rangeCheck(index);
    long previous = array[index];
    int numMoved = size - index - 1;
    if (numMoved > 0) {
        System.arraycopy(array, index+1, array, index, numMoved);
    }
    size--;
    return previous;
  }

  public long[] toLongArray() {
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