package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ZDD {

  public final Node nullNode;
  public final Node emptySetNode;

  final List<Node> nodes = Lists.newArrayList();
  private final Map<NodeKey, Node> nodeMap = Maps.newHashMap();
  private final Map<OpKey, Node> opCache = Maps.newHashMap();

  public ZDD() {
    this.nullNode = new NullNode(0);
    this.emptySetNode = new EmptySetNode(1);
    nodes.add(nullNode);
    nodes.add(emptySetNode);
    nodeMap.put(new NodeKey(-2, 0, 0), nullNode);
    nodeMap.put(new NodeKey(-1, 0, 0), emptySetNode);
  }

  public Node addNode(int elem, Node left, Node right) {
    if (right == nullNode) {
      return left;
    }
    NodeKey nodeKey = new NodeKey(elem, left.index, right.index);
    Node node = nodeMap.get(nodeKey);
    if (node != null) {
      return node;
    }
    node = new RegNode(nodes.size(), elem, left, right);
    nodes.add(node);
    nodeMap.put(nodeKey, node);
    return node;
  }

  private Node cachedOp(OpKey opKey, Callable<Node> opCallable) {
    Node cached = opCache.get(opKey);
    if (cached != null) {
      return cached;
    }
    Node computed;
    try {
      computed = opCallable.call();
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
    opCache.put(opKey, computed);
    return computed;
  }

  public abstract class Node {
    final int index;

    protected Node(int index) {
      this.index = index;
    }

    public abstract Integer getElem();
    public abstract Node getLeft();
    public abstract Node getRight();
    public abstract boolean eval(List<Integer> set);

    public Node and(final Node that) {
      return cachedOp(new OpKey(Op.AND, this, that), new Callable<Node>() {
        @Override public Node call() {
          return doAnd(that);
        }
      });
    }

    public Node or(final Node that) {
      return cachedOp(new OpKey(Op.OR, this, that), new Callable<Node>() {
        @Override public Node call() {
          return doOr(that);
        }
      });
    }

    public Node minus(final Node that) {
      return cachedOp(new OpKey(Op.MINUS, this, that), new Callable<Node>() {
        @Override public Node call() {
          return doMinus(that);
        }
      });
    }

    public Node xor(final Node that) {
      return cachedOp(new OpKey(Op.XOR, this, that), new Callable<Node>() {
        @Override public Node call() {
          return doXor(that);
        }
      });
    }

    protected abstract Node doAnd(Node that);
    protected abstract Node doOr(Node that);
    protected abstract Node doMinus(Node that);
    protected abstract Node doXor(Node that);

    @Override
    public String toString() {
      List<Integer> elems = Lists.newArrayList();
      Node node = this;
      while (node.getElem() != null) {
        elems.add(node.getElem());
        node = node.getRight();
      }
      return elems.toString();
    }

    public int realSize() {
      IdentityHashMap<Node, Void> nodes = new IdentityHashMap<Node, Void>();
      addNode(this, nodes);
      return nodes.size();
    }

    private void addNode(Node node, IdentityHashMap<Node, Void> nodes) {
      if (!nodes.containsKey(node)) {
        nodes.put(node, null);
        if (node.getElem() != null) {
          addNode(node.getLeft(), nodes);
          addNode(node.getRight(), nodes);
        }
      }
    }
  }

  private class NullNode extends Node {
    private NullNode(int index) {
      super(index);
    }

    @Override
    public Integer getElem() {
      return null;
    }

    @Override
    public Node getLeft() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Node getRight() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean eval(List<Integer> set) {
      return false;
    }

    @Override
    public Node doAnd(Node that) {
      return nullNode;
    }

    @Override
    public Node doOr(Node that) {
      return that;
    }

    @Override
    public Node doMinus(Node that) {
      return nullNode;
    }

    @Override
    public Node doXor(Node that) {
      return that;
    }
  }

  private class EmptySetNode extends Node {
    private EmptySetNode(int index) {
      super(index);
    }

    @Override
    public Integer getElem() {
      return null;
    }

    @Override
    public Node getLeft() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Node getRight() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean eval(List<Integer> set) {
      return set.isEmpty();
    }

    @Override
    public Node doAnd(Node that) {
      Integer thatElem = that.getElem();
      if (thatElem == null) {
        if (that == emptySetNode) {
          return emptySetNode;
        } else {
          checkArgument(that == nullNode);
          return nullNode;
        }
      } else {
        return this.and(that.getLeft());
      }
    }

    @Override
    public Node doOr(Node that) {
      Integer thatElem = that.getElem();
      if (thatElem == null) {
        return emptySetNode;
      } else {
        return addNode(that.getElem(), that.getLeft().or(this), that.getRight());
      }
    }

    @Override
    public Node doMinus(Node that) {
      Integer thatElem = that.getElem();
      if (thatElem == null) {
        if (that == emptySetNode) {
          return nullNode;
        } else {
          checkArgument(that == nullNode);
          return emptySetNode;
        }
      } else {
        return this.minus(that.getLeft());
      }
    }

    @Override
    public Node doXor(Node that) {
      Integer thatElem = that.getElem();
      if (thatElem == null) {
        if (that == emptySetNode) {
          return nullNode;
        } else {
          checkArgument(that == nullNode);
          return emptySetNode;
        }
      } else {
        return addNode(that.getElem(), that.getLeft().xor(this), that.getRight());
      }
    }
  }

  private class RegNode extends Node {
    private final int elem;
    private final Node left;
    private final Node right;

    private RegNode(int index, int elem, Node left, Node right) {
      super(index);
      this.elem = elem;
      this.left = left;
      this.right = right;
    }

    @Override
    public Integer getElem() {
      return elem;
    }

    @Override
    public Node getLeft() {
      return left;
    }

    @Override
    public Node getRight() {
      return right;
    }

    @Override
    public boolean eval(List<Integer> set) {
      if (set.isEmpty()) {
        return left.eval(set);
      }
      int first = set.get(0);
      if (first < elem) {
        return false;
      } else if (first == elem) {
        return right.eval(set.subList(1, set.size()));
      } else {
        return left.eval(set);
      }
    }

    @Override
    public Node doAnd(Node that) {
      Integer thatElem = that.getElem();
      if (thatElem == null) {
        return left.and(that);
      } else if (this.elem == thatElem) {
        return addNode(this.elem, left.and(that.getLeft()), right.and(that.getRight()));
      } else if (this.elem < thatElem) {
        return left.and(that);
      } else {
        return that.getLeft().and(this);
      }
    }

    @Override
    public Node doOr(Node that) {
      Integer thatElem = that.getElem();
      if (thatElem == null) {
        return addNode(this.elem, left.or(that), right);
      } else if (this.elem == thatElem) {
        return addNode(this.elem, left.or(that.getLeft()), right.or(that.getRight()));
      } else if (this.elem < thatElem) {
        return addNode(this.elem, left.or(that), right);
      } else {
        return addNode(thatElem, that.getLeft().or(this), that.getRight());
      }
    }

    @Override
    public Node doMinus(Node that) {
      Integer thatElem = that.getElem();
      if (thatElem == null) {
        return addNode(this.elem, left.minus(that), right);
      } else if (this.elem == thatElem) {
        return addNode(this.elem, left.minus(that.getLeft()), right.minus(that.getRight()));
      } else if (this.elem < thatElem) {
        return addNode(this.elem, left.minus(that), right);
      } else {
        return this.minus(that.getLeft());
      }
    }

    @Override
    public Node doXor(Node that) {
      Integer thatElem = that.getElem();
      if (thatElem == null) {
        return addNode(this.elem, left.xor(that), right);
      } else if (this.elem == thatElem) {
        return addNode(this.elem, left.xor(that.getLeft()), right.xor(that.getRight()));
      } else if (this.elem < thatElem) {
        return addNode(this.elem, left.xor(that), right);
      } else {
        return addNode(thatElem, that.getLeft().xor(this), that.getRight());
      }
    }
  }

  private static class NodeKey {
    private final int elem;
    private final int left, right;

    private NodeKey(int elem, int left, int right) {
      this.elem = elem;
      this.left = left;
      this.right = right;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(elem, left, right);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof ZDD.NodeKey) {
        NodeKey that = (NodeKey) obj;
        return this.elem == that.elem
            && this.left == that.left
            && this.right == that.right;
      }
      return false;
    }
  }

  private enum Op {
    AND, OR, MINUS, XOR
  }

  private static class OpKey {
    private final Op op;
    private final int left, right;

    public OpKey(Op op, Node left, Node right) {
      this.op = op;
      this.left = left.index;
      this.right = right.index;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(op, left, right);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof OpKey) {
        OpKey that = (OpKey) obj;
        return this.op == that.op
            && this.left == that.left
            && this.right == that.right;
      }
      return false;
    }
  }
}
