package euler;

import static euler.Problem212.EventType.END;
import static euler.Problem212.EventType.START;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Problem212 {

  public static void main(String[] args) {
    System.out.println(new Problem212().solve(50000));
  }

  public long solve(int n) {
    List<Cuboid> cuboids = Lists.newArrayList();
    Iterator<Long> laggedFibIter = laggedFib().iterator();
    for (int i = 0; i < n; i++) {
      long x0 = laggedFibIter.next() % 10000;
      long y0 = laggedFibIter.next() % 10000;
      long z0 = laggedFibIter.next() % 10000;
      long dx = 1 + (laggedFibIter.next() % 399);
      long dy = 1 + (laggedFibIter.next() % 399);
      long dz = 1 + (laggedFibIter.next() % 399);
      cuboids.add(new Cuboid(x0, y0, z0, x0 + dx, y0 + dy, z0 + dz));
    }
    return volume(cuboids);
  }

  private long volume(Iterable<Cuboid> cuboids) {
    List<Event<Rectangle>> events = Lists.newArrayList();
    for (Cuboid cuboid : cuboids) {
      Rectangle rectangle =
          new Rectangle(cuboid.x1, cuboid.y1, cuboid.x2, cuboid.y2);
      events.add(new Event<Rectangle>(rectangle, cuboid.z1, START));
      events.add(new Event<Rectangle>(rectangle, cuboid.z2, END));
    }
    Collections.sort(events);
    long volume = 0L;
    long area = 0L;
    long lastZ = 0L;
    Set<Rectangle> rectangles = Sets.newHashSet();
    int idx = 0;
    for (Event<Rectangle> event : events) {
      System.out.println(idx++);
      if (area > 0) {
        volume += area * (event.c - lastZ);
      }
      switch (event.type) {
        case START: rectangles.add(event.object); break;
        case END: rectangles.remove(event.object); break;
      }
      area = area(rectangles);
      lastZ = event.c;
    }
    return volume;
  }

  private long area(Iterable<Rectangle> rectangles) {
    List<Event<Interval>> events = Lists.newArrayList();
    for (Rectangle rectangle : rectangles) {
      Interval interval = new Interval(rectangle.x1, rectangle.x2);
      events.add(new Event<Interval>(interval, rectangle.y1, START));
      events.add(new Event<Interval>(interval, rectangle.y2, END));
    }
    Collections.sort(events);
    long area = 0L;
    long length = 0L;
    long lastY = 0L;
    Set<Interval> intervals = Sets.newHashSet();
    for (Event<Interval> event : events) {
      if (length > 0) {
        area += length * (event.c - lastY);
      }
      switch (event.type) {
        case START: intervals.add(event.object); break;
        case END: intervals.remove(event.object); break;
      }
      length = length(intervals);
      lastY = event.c;
    }
    return area;
  }

  private long length(Iterable<Interval> intervals) {
    List<Event<Void>> events = Lists.newArrayList();
    for (Interval interval : intervals) {
      events.add(new Event<Void>(null, interval.x1, START));
      events.add(new Event<Void>(null, interval.x2, END));
    }
    Collections.sort(events);
    long length = 0L;
    int cnt = 0;
    long lastX = 0L;
    for (Event<Void> event : events) {
      if (cnt > 0) {
        length += event.c - lastX;
      }
      switch (event.type) {
        case START: cnt++; break;
        case END: cnt--; break;
      }
      lastX = event.c;
    }
    return length;
  }

  enum EventType { START, END }

  private static class Event<T> implements Comparable<Event<T>> {
    final T object;
    final long c;
    final EventType type;

    public Event(T object, long c, EventType type) {
      this.object = object;
      this.c = c;
      this.type = type;
    }

    @Override
    public int compareTo(Event<T> that) {
      return Longs.compare(c, that.c);
    }
  }

  private static class Interval {
    final long x1, x2;

    public Interval(long x1, long x2) {
      this.x1 = x1;
      this.x2 = x2;
    }
  }

  private static class Rectangle {
    final long x1, y1;
    final long x2, y2;

    public Rectangle(long x1, long y1, long x2, long y2) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
    }
  }

  private static class Cuboid {
    final long x1, y1, z1;
    final long x2, y2, z2;

    public Cuboid(long x1, long y1, long z1, long x2, long y2, long z2) {
      this.x1 = x1;
      this.y1 = y1;
      this.z1 = z1;
      this.x2 = x2;
      this.y2 = y2;
      this.z2 = z2;
    }
  }

  private Iterable<Long> laggedFib() {
    return new Iterable<Long>() {
      @Override
      public Iterator<Long> iterator() {
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
    };
  }
}
