package euler;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

public class ImmutableIntList extends AbstractList<Integer> {
  private int[] array;

  private ImmutableIntList(int[] array) {
    this.array = array;
  }

  public static ImmutableIntList of(int... elements) {
    return new ImmutableIntList(elements.clone());
  }

  public static ImmutableIntList copyOf(Iterable<Integer> elements) {
    return new ImmutableIntList(Ints.toArray(ImmutableList.copyOf(elements)));
  }

  public static ImmutableIntList copyOf(int[] elements) {
    return new ImmutableIntList(elements.clone());
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
    throw new UnsupportedOperationException();
  }

  @Override
  public void add(int index, Integer element) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(Collection<? extends Integer> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Integer set(int index, Integer element) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Integer remove(int index) {
    throw new UnsupportedOperationException();
  }

  public int[] toIntArray() {
    return array.clone();
  }

  private void rangeCheck(int index) {
    if (index >= array.length || index < 0) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + array.length);
    }
  }

  @Override
  public int size() {
    return array.length;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(array);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof ImmutableIntList) {
      return Arrays.equals(this.array, ((ImmutableIntList) o).array);
    } else {
      return super.equals(o);
    }
  }

  @Override
  public String toString() {
    return array.toString();
  }
}