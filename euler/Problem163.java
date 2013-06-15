package euler;

import static com.google.common.base.Preconditions.checkState;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Problem163 {

  public static void main(String[] args) {
    System.out.println(new Problem163().solve(36));
  }

  public long solve(int n) {
    Graph graph = new Graph();
    for (int x = 0; x < n; x++) {
      for (int y = 0; x + y < n; y++) {
        Point base = new Point(4 * x, 4 * y);
        Point p1 = base;
        Point p2 = base.plus(new Point(2, 0));
        Point p3 = base.plus(new Point(4, 0));
        Point p4 = base.plus(new Point(1, 1));
        Point p5 = base.plus(new Point(0, 2));
        Point p6 = base.plus(new Point(2, 2));
        Point p7 = base.plus(new Point(0, 4));
        graph.addEdge(p1, p2, 0);
        graph.addEdge(p1, p4, 30);
        graph.addEdge(p1, p5, 60);
        graph.addEdge(p2, p3, 0);
        graph.addEdge(p2, p4, 90);
        graph.addEdge(p3, p4, 150);
        graph.addEdge(p3, p6, 120);
        graph.addEdge(p4, p5, 150);
        graph.addEdge(p4, p6, 30);
        graph.addEdge(p4, p7, 90);
        graph.addEdge(p5, p7, 60);
        graph.addEdge(p6, p7, 120);
        if (x + y < n - 1) {
          graph.addEdge(p1.mirror(p6), p2.mirror(p6), 0);
          graph.addEdge(p1.mirror(p6), p4.mirror(p6), 30);
          graph.addEdge(p1.mirror(p6), p5.mirror(p6), 60);
          graph.addEdge(p2.mirror(p6), p3.mirror(p6), 0);
          graph.addEdge(p2.mirror(p6), p4.mirror(p6), 90);
          graph.addEdge(p3.mirror(p6), p4.mirror(p6), 150);
          graph.addEdge(p3.mirror(p6), p6.mirror(p6), 120);
          graph.addEdge(p4.mirror(p6), p5.mirror(p6), 150);
          graph.addEdge(p4.mirror(p6), p6.mirror(p6), 30);
          graph.addEdge(p4.mirror(p6), p7.mirror(p6), 90);
          graph.addEdge(p5.mirror(p6), p7.mirror(p6), 60);
          graph.addEdge(p6.mirror(p6), p7.mirror(p6), 120);
        }
      }
    }
    Graph extendedGraph = new Graph();
    for (Vertex vertex : graph.vertices.values()) {
      for (Edge edge : vertex.edges) {
        extend(extendedGraph, vertex, edge, Sets.newHashSet(vertex));
      }
    }
    int vertexCount = extendedGraph.vertices.size();
    int[][] matrix = new int[vertexCount][vertexCount];
    for (int a = 0; a < vertexCount; a++) {
      for (int b = 0; b < vertexCount; b++) {
        matrix[a][b] = -1;
      }
    }
    for (Vertex vertex : extendedGraph.vertices.values()) {
      for (Edge edge : vertex.edges) {
        checkState(matrix[edge.source.index][edge.target.index] == -1);
        matrix[edge.source.index][edge.target.index] = edge.angle;
      }
    }
    long count = 0L;
    for (int a = 0; a < vertexCount; a++) {
      for (int b = a + 1; b < vertexCount; b++) {
        if (matrix[a][b] < 0) {
          continue;
        }
        for (int c = b + 1; c < vertexCount; c++) {
          if (matrix[b][c] >= 0 && matrix[a][c] >= 0 && matrix[a][b] != matrix[b][c]) {
            count++;
          }
        }
      }
    }
    return count;
  }

  private void extend(Graph extendedGraph, Vertex origin, Edge edge, Set<Vertex> visited) {
    if (visited.contains(edge.target)) {
      return;
    }
    visited.add(edge.target);
    extendedGraph.addEdge(origin.point, edge.target.point, edge.angle);
    for (Edge nextEdge : edge.target.edges) {
      if (nextEdge.angle == edge.angle) {
        extend(extendedGraph, origin, nextEdge, visited);
      }
    }
  }

  private static class Graph {
    private final Map<Point, Vertex> vertices = Maps.newHashMap();

    public void addEdge(Point point1, Point point2, int angle) {
      Vertex vertex1 = getVertex(point1);
      Vertex vertex2 = getVertex(point2);
      vertex1.addEdge(new Edge(vertex1, vertex2, angle));
      vertex2.addEdge(new Edge(vertex2, vertex1, angle));
    }

    private Vertex getVertex(Point p) {
      Vertex vertex = vertices.get(p);
      if (vertex != null) {
        return vertex;
      }
      vertex = new Vertex(p, vertices.size());
      vertices.put(p, vertex);
      return vertex;
    }
  }

  private static class Vertex {
    private final Point point;
    private final int index;
    private final Set<Edge> edges = Sets.newHashSet();

    public Vertex(Point point, int index) {
      this.point = point;
      this.index = index;
    }

    private void addEdge(Edge edge) {
      edges.add(edge);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(index);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Vertex) {
        Vertex that = (Vertex) obj;
        return this.index == that.index;
      }
      return false;
    }

    @Override
    public String toString() {
      return point.toString();
    }
  }

  private static class Edge {
    private final Vertex source, target;
    private final int angle;

    public Edge(Vertex source, Vertex target, int angle) {
      this.source = source;
      this.target = target;
      this.angle = angle;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(source, target, angle);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Edge) {
        Edge that = (Edge) obj;
        return Objects.equal(this.source, that.source)
            && Objects.equal(this.target, that.target)
            && this.angle == that.angle;
      }
      return false;
    }

    @Override
    public String toString() {
      return String.format("(%s,%s:%d)", source, target, angle);
    }
  }

  private static class Point {
    private final int x, y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public Point plus(Point that) {
      return new Point(this.x + that.x, this.y + that.y);
    }

    public Point mirror(Point center) {
      return new Point(center.x * 2 - this.x, center.y * 2 - this.y);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(x, y);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Point) {
        Point that = (Point) obj;
        return this.x == that.x
            && this.y == that.y;
      }
      return false;
    }

    @Override
    public String toString() {
      return String.format("[%d,%d]", x, y);
    }
  }
}
