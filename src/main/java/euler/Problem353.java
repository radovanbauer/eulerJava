package euler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;

public class Problem353 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem353().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public String solve() {
    double sum = 0;
    for (int n = 1; n <= 15; n++) {
      double res = solve((1 << n) - 1);
      System.out.printf("%d,%.10f\n", n, res);
      sum += res;
    }
    return String.format("%.10f", sum);
  }

  private double solve(int r) {
    Map<Long, Integer> squareMap = new HashMap<>();
    for (int i = 0; i <= r; i++) {
      squareMap.put(1L * i * i, i);
    }
    Set<Vector3> points = new HashSet<>();
    for (int x = 0; x <= r; x++) {
      for (int y = 0; y <= r; y++) {
        Integer z = squareMap.get(1L * r * r - 1L * x * x - 1L * y * y);
        if (z != null && y <= z) {
          points.add(Vector3.create(-x, y, z));
          points.add(Vector3.create(x, y, z));
        }
      }
    }
    return solve(r, ImmutableList.copyOf(points));
  }

  private double solve(int r, List<Vector3> points) {
    int size = points.size();
    Graph graph = new Graph() {
      @Override
      public int nodeCount() {
        return size;
      }

      @Override
      public List<Edge> edgesFrom(int from) {
        List<Edge> edges = new ArrayList<>();
        for (int to = 0; to < size; to++) {
          edges.add(Edge.create(
              from, to, Math.pow(points.get(from).angle(points.get(to)) / Math.PI, 2)));
        }
        return edges;
      }
    };
    int start = points.indexOf(Vector3.create(r, 0, 0));
    int end = points.indexOf(Vector3.create(-r, 0, 0));
    System.out.println(graph.nodeCount());
    return shortestPath(graph, start, end);
  }

  @AutoValue
  static abstract class Vector3 {
    abstract double x();
    abstract double y();
    abstract double z();

    static Vector3 create(double x, double y, double z) {
      return new AutoValue_Problem353_Vector3(x, y, z);
    }

    double dotProduct(Vector3 that) {
      return this.x() * that.x() + this.y() * that.y() + this.z() * that.z();
    }

    double norm() {
      return Math.sqrt(dotProduct(this));
    }

    double angle(Vector3 that) {
      return Math.acos(this.dotProduct(that) / (this.norm() * that.norm()));
    }
  }

  private double shortestPath(Graph graph, int from, int to) {
    double[] distance = new double[graph.nodeCount()];
    int[] parent = new int[graph.nodeCount()];
    Arrays.fill(distance, Double.MAX_VALUE);
    distance[from] = 0D;
    parent[from] = -1;
    boolean[] visited = new boolean[graph.nodeCount()];
    TreeSet<NodeInfo> nodes = new TreeSet<>();
    nodes.add(NodeInfo.create(from, distance[from]));
    while (!visited[to] && !nodes.isEmpty()) {
      NodeInfo first = nodes.first();
      nodes.remove(first);
      int node = first.node();
//      for (int i = 0; i < distance.length; i++) {
//        if (!visited[i] && (node == -1 || distance[i] <= distance[node])) {
//          node = i;
//        }
//      }
//      if (node == -1) {
//        return Double.MAX_VALUE;
//      }
      visited[node] = true;
      for (Edge edge : graph.edgesFrom(node)) {
        if (!visited[edge.to()]) {
          if (distance[node] + edge.weight() < distance[edge.to()]) {
            nodes.remove(NodeInfo.create(edge.to(), distance[edge.to()]));
            distance[edge.to()] = distance[node] + edge.weight();
            nodes.add(NodeInfo.create(edge.to(), distance[edge.to()]));
            parent[edge.to()] = node;
          }
        }
      }
    }
    return distance[to];
  }

  @AutoValue
  static abstract class NodeInfo implements Comparable<NodeInfo> {
    abstract int node();
    abstract double distance();

    static NodeInfo create(int node, double distance) {
      return new AutoValue_Problem353_NodeInfo(node, distance);
    }

    @Override
    public int compareTo(NodeInfo o) {
      return ComparisonChain.start()
          .compare(this.distance(), o.distance())
          .compare(this.node(), o.node())
          .result();
    }
  }

  private interface Graph {
    int nodeCount();
    List<Edge> edgesFrom(int node);
  }

  @AutoValue
  static abstract class Edge {
    abstract int from();
    abstract int to();
    abstract double weight();

    static Edge create(int from, int to, double weight) {
      return new AutoValue_Problem353_Edge(from, to, weight);
    }
  }
}
