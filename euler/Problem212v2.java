package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static euler.Problem212v2.EventType.END;
import static euler.Problem212v2.EventType.START;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Problem212v2 {

  public static void main(String[] args) {
    System.out.println(new Problem212v2().solve(50000));
  }

  public long solve(int n) {
    List<Box> cuboids = Lists.newArrayList();
    Iterator<Long> laggedFibIter = laggedFib();
    for (int i = 0; i < n; i++) {
      long x0 = laggedFibIter.next() % 10000;
      long y0 = laggedFibIter.next() % 10000;
      long z0 = laggedFibIter.next() % 10000;
      long dx = 1 + (laggedFibIter.next() % 399);
      long dy = 1 + (laggedFibIter.next() % 399);
      long dz = 1 + (laggedFibIter.next() % 399);
      cuboids.add(new Box(
          new Point(ImmutableList.of(x0, y0, z0)),
          new Point(ImmutableList.of(x0 + dx, y0 + dy, z0 + dz))));
    }
    return volume(cuboids);
  }

  private long volume(Collection<Box> boxes) {
    if (boxes.isEmpty()) {
      return 0L;
    }
    if (boxes.iterator().next().dimension() == 0) {
      return 1L;
    }
    List<Event> events = Lists.newArrayList();
    for (Box box : boxes) {
      Box subBox = box.subBox();
      events.add(new Event(subBox, box.a.coords.get(0), START));
      events.add(new Event(subBox, box.b.coords.get(0), END));
    }
    Collections.sort(events);
    long volume = 0L;
    long subVolume = 0L;
    long lastC = 0L;
    Set<Box> subBoxes = Sets.newHashSet();
    int idx = 0;
    for (Event event : events) {
      if (subVolume > 0) {
        volume += subVolume * (event.c - lastC);
      }
      switch (event.type) {
        case START: subBoxes.add(event.subBox); break;
        case END: subBoxes.remove(event.subBox); break;
      }
      subVolume = volume(subBoxes);
      lastC = event.c;
    }
    return volume;
  }

  enum EventType { START, END }

  private static class Event implements Comparable<Event> {
    final Box subBox;
    final long c;
    final EventType type;

    public Event(Box subBox, long c, EventType type) {
      this.subBox = subBox;
      this.c = c;
      this.type = type;
    }

    @Override
    public int compareTo(Event that) {
      return Longs.compare(c, that.c);
    }
  }

  private static class Point {
    final ImmutableList<Long> coords;

    public Point(Iterable<Long> coords) {
      this.coords = ImmutableList.copyOf(coords);
    }
 
    public int dimension() {
      return coords.size();
    }

    public Point subPoint() {
      checkArgument(dimension() >= 1);
      return new Point(coords.subList(1, coords.size()));
    }
  }

  private static class Box {
    final Point a, b;
 
    public Box(Point a, Point b) {
      checkArgument(a.dimension() == b.dimension());
      this.a = a;
      this.b = b;
    }

    public int dimension() {
      return a.dimension();
    }

    public Box subBox() {
      return new Box(a.subPoint(), b.subPoint());
    }
  }

  private Iterator<Long> laggedFib() {
    return new AbstractIterator<Long>() {
      long k = 1;
      long[] last = new long[55];
      @Override
      protected Long computeNext() {
        long res;
        if (k <= 55) {
          res = (100003 - 200003 * k + 300007 * k * k * k) % 1000000;
        } else {
          res = (last[(int) ((k - 24 + 55) % 55)]
              + last[(int) (k % 55)]) % 1000000;
        }
        last[(int) (k % 55)] = res;
        k++;
        return res;
      }
    };
  }
}
