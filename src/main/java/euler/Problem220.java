package euler;

import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;

public class Problem220 {

  public static void main(String[] args) {
    System.out.println(new Problem220().solve(50, 1000000000000L));
  }

  public String solve(int n, long steps) {
    return Node.move(ImmutableList.of(L, F, new A(n)), steps).toString();
  }

  private static abstract class Node {
    public abstract long size();
    public abstract Pos move(long steps);

    public static Pos move(Iterable<Node> nodes, long steps) {
      Pos pos = new Problem220.Pos(0, 0, 0);
      for (Node node : nodes) {
        if (steps == 0) {
          break;
        }
        long nodeSize = node.size();
        pos = pos.compose(node.move(steps));
        steps -= Math.min(steps, nodeSize);
      }
      return pos;
    }
  }

  private static Node F = new SimpleNode(1, new Pos(1, 0, 0));
  private static Node L = new SimpleNode(0, new Pos(0, 0, 1));
  private static Node R = new SimpleNode(0, new Pos(0, 0, 3));

  private static class SimpleNode extends Node {
    private final int size;
    private final Pos pos;

    public SimpleNode(int size, Pos pos) {
      this.size = size;
      this.pos = pos;
    }

    @Override
    public long size() {
      return size;
    }

    @Override
    public Pos move(long steps) {
      if (steps == 0) {
        return new Pos(0, 0, 0);
      } else {
        return pos;
      }
    }
  }

  private static class A extends Node {
    private final int depth;
    private final long size;

    public A(int depth) {
      this.depth = depth;
      this.size = LongMath.pow(2, depth) - 1L;
    }

    @Override
    public long size() {
      return size;
    }

    private static Pos[] cache = new Pos[51];

    @Override
    public Pos move(long steps) {
      if (steps >= size) {
        if (cache[depth] != null) {
          return cache[depth];
        }
        return cache[depth] = move(getChildren(), steps);
      } else {
        return move(getChildren(), steps);
      }
    }

    private ImmutableList<Node> getChildren() {
      if (depth == 0) {
        return ImmutableList.of();
      } else {
        return ImmutableList.of(new A(depth - 1), R, new B(depth - 1), F, R);
      }
    }
  }

  private static class B extends Node {
    private final int depth;
    private final long size;

    public B(int depth) {
      this.depth = depth;
      this.size = LongMath.pow(2, depth) - 1L;
    }

    @Override
    public long size() {
      return size;
    }

    private static Pos[] cache = new Pos[51];

    @Override
    public Pos move(long steps) {
      if (steps >= size) {
        if (cache[depth] != null) {
          return cache[depth];
        }
        return cache[depth] = move(getChildren(), steps);
      } else {
        return move(getChildren(), steps);
      }
    }

    private ImmutableList<Node> getChildren() {
      if (depth == 0) {
        return ImmutableList.of();
      } else {
        return ImmutableList.of(L, F, new A(depth - 1), L, new B(depth - 1));
      }
    }
  }

  private static class Pos {
    private final long x, y;
    private final int dir;

    public Pos(long x, long y, int dir) {
      this.x = x;
      this.y = y;
      this.dir = dir;
    }

    public Pos compose(Pos that) {
      switch (dir) {
      case 0: return new Pos(x + that.x, y + that.y, (dir + that.dir) % 4);
      case 1: return new Pos(x - that.y, y + that.x, (dir + that.dir) % 4);
      case 2: return new Pos(x - that.x, y - that.y, (dir + that.dir) % 4);
      case 3: return new Pos(x + that.y, y - that.x, (dir + that.dir) % 4);
      default: throw new AssertionError();
      }
    }

    @Override
    public String toString() {
      return String.format("%d,%d", x, y);
    }
  }
}
