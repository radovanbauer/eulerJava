package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSet;
import com.google.common.math.LongMath;


public class Problem442 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem442().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  private long solve() {
    long n = 1_000_000_000_000_000_000L;
    List<String> strings = new ArrayList<>();
    for (int k = 1; k <= 18; k++) {
      strings.add(String.valueOf(LongMath.pow(11, k)));
    }
    
    Node root =
        createGraph(strings, ImmutableSet.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
    int nodeCount = 0;
    Map<Node, Integer> nodeMap = new LinkedHashMap<>();
    ArrayDeque<Node> queue = new ArrayDeque<>();
    queue.add(root);
    while (!queue.isEmpty()) {
      Node node = queue.removeFirst();
      if (!nodeMap.containsKey(node) && node.getMatches().isEmpty()) {
        nodeMap.put(node, nodeCount++);
        for (Node child : node.getChildren()) {
          queue.add(child);
        }
      }
    }
    long[][] matrixArray = new long[nodeCount][nodeCount];
    for (Node from : nodeMap.keySet()) {
      for (Node to : from.getChildren()) {
        if (to.getMatches().isEmpty()) {
          matrixArray[nodeMap.get(to)][nodeMap.get(from)]++;
        }
      }
    }
    LongMatrix matrix = LongMatrix.create(matrixArray);
    long low = 0; long high = 2 * n;
    while (low < high - 1) {
      long mid = (low + high) / 2;
      if (count(matrix, nodeMap, root, mid) > n) {
        high = mid;
      } else {
        low = mid;
      }
    }
    return low;
  }

  private long count(LongMatrix matrix, Map<Node, Integer> nodeMap, Node root, long n) {
    long res = 0;
    int steps = 0;
    while (n > 0) {
      if (root.matchSubstrings(String.valueOf(n)).isEmpty()) {
        res += count(matrix, nodeMap, root, String.valueOf(n), steps);
      }
      if (n % 10 > 0) {
        n--;
      } else {
        while (n > 0 && n % 10 == 0) {
          n /= 10;
          steps++;
        }
        if (n > 0) {
          n--;
        }
      }
    }
    res += count(matrix, nodeMap, root, "0", steps) - 1;
    return res;
  }

  private long count(LongMatrix matrix, Map<Node, Integer> nodeMap, Node root, String prefix, int steps) {
    long[][] vectorArray = new long[nodeMap.size()][1];
    vectorArray[nodeMap.get(root.getChild(prefix))][0] = 1;
    LongMatrix vector = LongMatrix.create(vectorArray);
    if (steps == 0) {
      return sum(vector);
    } else {
      return sum(matrix.pow(steps).multiply(vector));
    }
  }

  private long sum(LongMatrix matrix) {
    long res = 0;
    for (int row = 0; row < matrix.rowCount(); row++) {
      for (int column = 0; column < matrix.columnCount(); column++) {
        res += matrix.element(row, column);
      }
    }
    return res;
  }

  private Node createGraph(List<String> strings, Set<Character> alphabet) {
    Node root = new Node((char) 0, ImmutableSet.of());
    HashBasedTable<String, Integer, Node> stringNodes = HashBasedTable.create();
    for (String s : strings) {
      Node prev = root;
      for (int i = 0; i < s.length(); i++) {
        Node node = prev.getChild(s.charAt(i));
        if (node == null) {
          Set<String> matches = new HashSet<>();
          for (String t : strings) {
            if (s.substring(0, i + 1).endsWith(t)) {
              matches.add(t);
            }
          }
          node = new Node(s.charAt(i), matches);
          prev.addChild(s.charAt(i), node);
        }
        stringNodes.put(s, i, node);
        prev = node;
      }
    }
    for (char c : alphabet) {
      if (root.getChild(c) == null) {
        root.addChild(c, root);
      }
    }
    for (String s : strings) {
      for (int i = 0; i < s.length(); i++) {
        Node node = stringNodes.get(s, i);
        nextC: for (char c : alphabet) {
          if (node.getChild(c) != null) {
            continue;
          }
          String suffix = s.substring(0, i + 1) + c;
          while (!suffix.isEmpty()) {
            for (String t : strings) {
              if (t.startsWith(suffix)) {
                node.addChild(c, stringNodes.get(t, suffix.length() - 1));
                continue nextC;
              }
            }
            suffix = suffix.substring(1);
          }
          node.addChild(c, root);
        }
      }
    }
    return root;
  }

  private static class Node {
    private final char c;
    private final ImmutableSet<String> matches;
    private final Map<Character, Node> children = new HashMap<>();

    public Node(char c, Iterable<String> matches) {
      this.c = c;
      this.matches = ImmutableSet.copyOf(matches);
    }

    public ImmutableSet<String> getMatches() {
      return matches;
    }

    public void addChild(char c, Node child) {
      checkNotNull(child);
      checkArgument(!children.containsKey(c));
      children.put(c, child);
    }

    public Node getChild(char c) {
      return children.get(c);
    }

    public Collection<Node> getChildren() {
      return Collections.unmodifiableCollection(children.values());
    }

    public Node getChild(String s) {
      Node res = this;
      for (int i = 0; i < s.length(); i++) {
        char letter = s.charAt(i);
        res = checkNotNull(res.getChild(letter));
      }
      return res;
    }

    public ImmutableSet<String> matchSubstrings(String s) {
      ImmutableSet.Builder<String> res = ImmutableSet.builder();
      Node node = this;
      res.addAll(node.getMatches());
      for (int i = 0; i < s.length(); i++) {
        char letter = s.charAt(i);
        node = checkNotNull(node.getChild(letter));
        res.addAll(node.getMatches());
      }
      return res.build();
    }

    @Override
    public String toString() {
      return c + ":" + matches + ":" + children.keySet();
    }
  }
}
