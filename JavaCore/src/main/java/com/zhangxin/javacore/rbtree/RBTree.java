package com.zhangxin.javacore.rbtree;

import lombok.Getter;
import lombok.Setter;


@Getter
public class RBTree<K extends Comparable<K>, V> {

  private static final boolean RED = false;
  private static final boolean BLACK = true;

  private RBNode root;

  @Getter
  @Setter
  public class RBNode {

    private K key;
    private V value;
    private RBNode left;
    private RBNode right;
    private RBNode parent;
    private boolean color;

    public RBNode(K key, V value, RBNode parent) {
      this.key = key;
      this.value = value;
      this.parent = parent;
    }

  }


  public void put(K key, V value) {
    RBNode node = insert(key, value);
    fixAfterInsert(node);
  }

  private RBNode insert(K key, V value) {

    RBNode c = root;
    RBNode p = null;
    int cmp = 0;
    while (c != null) {
      cmp = c.key.compareTo(key);
      if (cmp < 0) {
        p = c;
        c = c.right;
      } else if (cmp > 0) {
        p = c;
        c = c.left;
      } else {
        c.value = value;
        return c;
      }
    }

    if (cmp < 0) {
      p.right = new RBNode(key, value, p);
      return p.right;
    } else if (cmp > 0) {
      p.left = new RBNode(key, value, p);
      return p.left;
    } else {
      // (cmp == 0 at this line) -> it didn't enter the while loop -> root == null
      root = new RBNode(key, value, null);
      return root;
    }
  }

  /**
   * Case 1:
   *
   *      3B                                2R                      2B
   *     /    Right Rotate Pivot at 3B     /  \   Switch Color     /  \
   *    2R    ----------------------->   1R  3B   ----------->   1R   3R
   *   /
   *  1R
   *
   * Case 2:
   *
   *     4B                                 4B
   *     /    Left Rotate Pivot at 2R      /         Case 1
   *    2R    ---------------------->    3R          ----->
   *     \                              /
   *     3R                            2R
   *
   * Case 3:
   *
   *      3B                        3R                                 10B
   *     /  \     Switch Color     /  \     If 3R's Parent is Red      /      Case 1
   *    2R  4R    ----------->   2B   4B    -------------------->     5R      ----->
   *   /                         /                                    /
   *  1R                        1R                                  3R
   *                                                               /  \
   *                                                              2B  4B
   *
   *
   *                                                                0B
   *                                        If 3R's Parent is Red     \       Reversed Case 2
   *                                        -------------------->     5R      -------------->
   *                                                                  /
   *                                                                3R
   *                                                               /  \
   *                                                              2B  4B
   *
   * Case 4:
   *
   *      4B                        4R                                  10B
   *     /  \     Switch Color     /  \     If 4R's Parent is Red       /      Case 1
   *    2R  5R    ----------->   2B   5B    -------------------->     6R       ----->
   *     \                        \                                   /
   *     3R                       3R                                4R
   *                                                               /  \
   *                                                              2B  5B
   *
   *
   *                                                                0B
   *                                        If 4R's Parent is Red     \       Reversed Case 2
   *                                        -------------------->     6R      -------------->
   *                                                                  /
   *                                                                4R
   *                                                               /  \
   *                                                              2B  5B
   */
  private void fixAfterInsert(RBNode x) {
    // root node
    if (x.parent == null) {
      x.color = BLACK;
      return ;
    }

    // if parent's color is BLACK, there is no need to fixAfterInsert
    if (x.parent.color == BLACK) {
      return ;
    }

    // parent's color being RED means node must have grand parent, which is of course BLACK
    RBNode p = x.parent;
    RBNode pp = p.parent;

    if (p == pp.left) {
      RBNode u = pp.right;
      // uncle matters only when it is RED
      if (u != null && u.color == RED) { // Case 3 Or Case 4
        p.color = BLACK;
        u.color = BLACK;
        pp.color = RED;
        fixAfterInsert(pp);
        return ;
      }

      if (x == p.left) { // Case 1
        p.color = BLACK;
        pp.color = RED;
        rightRotate(pp);
      } else { // Case 2
        leftRotate(p);
        p.color = BLACK;
        pp.color = RED;
        rightRotate(pp);
      }

      return ;

    } else { // the rest is the L-R reverse of the logic above
      RBNode u = pp.left;
      // uncle matters only when it is RED
      if (u != null && u.color == RED) { // Reversed Case 3 Or Reversed Case 4
        p.color = BLACK;
        u.color = BLACK;
        pp.color = RED;
        fixAfterInsert(pp);
        return ;
      }

      if (x == p.right) { // Reversed Case 1
        p.color = BLACK;
        pp.color = RED;
        leftRotate(pp);
      } else { // Reversed Case 2
        rightRotate(p);
        p.color = BLACK;
        pp.color = RED;
        leftRotate(pp);
      }

      return ;
    }
  }

  public V remove(K key) {
    RBNode node = find(key);
    if (node == null) {
      return null;
    }
    delete(node);
    return node.value;
  }

  /**
   * Given a key, return the corresponding node if it exists in this tree. Otherwise, return null;
   */
  private RBNode find(K key) {
    RBNode c = root;
    int cmp = c.key.compareTo(key);
    while (c != null && cmp != 0) {
      if (cmp > 0) {
        c = c.left;
      } else {
        c = c.right;
      }
      cmp = c.key.compareTo(key);
    }
    return c;
  }

  /**
   * Case 1: If the target node is a leaf node, null out it.
   *
   * Case 2: If the target node has one child, connect the child to its parent.
   *
   * Case 3: If the target node has two children, replace the target's KV with its successor's KV,
   *         then delete the successor.
   */
  private void delete(RBNode x) {
    // Case 1
    if (x.left == null && x.right == null) {
      // x is root
      if (x.parent == null) {
        root = null;
        return ;
      }
      // rebalance the tree as if BLACK x is taken out
      RBNode p = x.parent;
      if (x.color == BLACK) {
        rebalanceBeforeDelete(x);
      }
      // null out x
      x.parent = null;
      if (p.left == x) {
        p.left = null;
      } else {
        p.right = null;
      }
      return ;
    }
    // Case 3
    if (x.left != null && x.right != null) {
      RBNode s = predecessor(x);
      x.key = s.key;
      x.value = s.value;
      delete(s);
      return ;
    }
    // Case 2
    RBNode c = x.left != null ? x.left : x.right;
    if (x.parent == null) {
      c.parent = null;
      root = c;
    } else {
      RBNode p = x.parent;
      c.parent = p;
      if (p.left == x) {
        p.left = c;
      } else {
        p.right = c;
      }
    }
    // null out x
    x.parent = x.left = x.right = null;
    // since BLACK x is replaced by c, make c BLACK to maintain balance
    if (x.color == BLACK) {
      c.color = BLACK;
    }
    return ;
  }

  /**
   * Self-sustained Case 1.1
   *
   *           4                                          4
   *          /                                          /
   *      (X)3B       Move Child Up                     2B
   *        /         ------------>
   *       2R         Switch Color
   *
   * Self-sustained Case 1.2
   *
   *
   *      (X)3B       Move Child Up       4B
   *        / \       ------------>      /
   *       2   4      Switch Color      2
   *
   *
   * If sibling has at least 1 Red child, borrow - Case 2.1
   *
   *            7B                             7B
   *           / \                              \                             8B
   *       (X)6B  9B   Right Rotate at 9B       8B     Left Rotate at 7      / \
   *             /     ----------------->        \     ----------------->   7B  9B
   *            8R     Switch Color              9R
   *
   * If sibling has at least 1 Red child, borrow - Case 2.2
   *
   *            7B                                9B                              9B
   *           / \                               /                               /
   *       (X)6B  9R   Left Rotate at 7         7R      Left Rotate at 7        8B
   *             /     ----------------->      / \     ----------------->      / \
   *            8B     Switch Color        (X)6B 8B                          7R   8.5R
   *             \                                \
   *            8.5R                             8.5R
   *
   * If sibling has 0 Red child, can't borrow - Case 3.1
   *
   *            60B                           60B
   *           /   \       Switch Color      /   \
   *         50R   70B     ----------->    50B   70B
   *        / \                              \
   *   (X)35B 55B                            55R
   *
   * If sibling has 0 Red child, can't borrow - Case 3.2
   *
   *            60B                              60B
   *           /   \       Switch Color         /   \       If 60 is not root, switch its siblings color
   *         50B   70B     ----------->       50B   70R     ------------------------------------------->
   *        / \     / \                        \    /  \    recursively till hitting the root.
   *   (X)35B 55B 65B 75B                     55R  65B 75B
   *
   */
  private void rebalanceBeforeDelete(RBNode x) {
    // terminate when x is root
    if (x.parent == null) {
      return ;
    }

    RBNode p = x.parent;
    if (x == p.left) {
      RBNode s = p.right;

      // deal with RED sibling before going further (Case 2.2)
      if (s.color == RED) {
        s.color = BLACK;
        p.color = RED;
        leftRotate(p);
        s = p.right;
      }

      // Sibling has 0 RED child. Can't borrow from sibling.
      if (isBlackOrNull(s.left) && isBlackOrNull(s.right)) {
        if (p.color == RED) {  // Case 3.1
          p.color = BLACK;
          s.color = RED;
        } else {               // Case 3.2
          s.color = RED;
          rebalanceBeforeDelete(p); // recursively make parent's sibling RED till hitting the root
        }
      } else { // Sibling has at least 1 RED child
        if (isBlackOrNull(s.right)) { // Case 2.1
          s.color = RED;
          s.left.color = BLACK;
          rightRotate(s); // only required when s == p.right but s.left is RED because s.left is BLACK or null
        }
        // finalize color and do the last rotation in Case 2.1
        s.color = p.color;
        p.color = BLACK;
        if (s.right != null) s.right.color = BLACK;
        leftRotate(p);
      }

    } else { // L-R reverse
      RBNode s = p.left;

      // deal with RED sibling before going further (Case 2.2)
      if (s.color == RED) {
        s.color = BLACK;
        p.color = RED;
        rightRotate(p);
        s = p.left;
      }

      // Sibling has 0 RED child. Can't borrow from sibling.
      if (isBlackOrNull(s.left) && isBlackOrNull(s.right)) {

        if (p.color == RED) {  // Case 3.1
          p.color = BLACK;
          s.color = RED;
        } else {               // Case 3.2
          s.color = RED;
          rebalanceBeforeDelete(p); // recursively make parent's sibling RED till hitting the root
        }
      } else { // Sibling has at least 1 RED child
        if (isBlackOrNull(s.left)) { // Case 2.1
          s.color = RED;
          s.right.color = BLACK;
          leftRotate(s); // only required when s == p.left but s.right is RED because s.left is BLACK or null
        }
        // finalize color and do the last rotation in Case 2.1
        s.color = p.color;
        p.color = BLACK;
        if (s.left != null) s.left.color = BLACK;
        rightRotate(p);
      }
    }
  }

  private boolean isBlackOrNull(RBNode node) {
    return node == null || node.color == BLACK;
  }

  /**
   * Start from given node, find its successor, which is sequentially the next bigger one.
   */
  private RBNode successor(RBNode node) {
    if (node == null) {
      return null;
    }
    RBNode p = null;
    RBNode rc = node.right;
    while (rc != null) {
      p = rc;
      rc = rc.left;
    }
    return p;
  }

  private RBNode predecessor(RBNode node) {
    if (node == null) {
      return null;
    }
    RBNode p = null;
    RBNode lc = node.left;
    while (lc != null) {
      p = lc;
      lc = lc.right;
    }
    return p;
  }

  /**
   * Prerequisite: the given pivot must have right child
   *
   *      5              7
   *     / \            / \
   *    3  7           5  8
   *       /\         / \
   *      6 8        3  6
   */
  public void leftRotate(RBNode pivot) {
    if (pivot != null) {

      RBNode right = pivot.right;
      RBNode parent = pivot.parent;

      // make right.left the right child of pivot
      if (right.left != null) {
        pivot.right = right.left;
        right.left.parent = pivot;
      } else {
        pivot.right = null;
      }

      // make pivot the left child of right
      right.left = pivot;
      pivot.parent = right;

      // make pivot.parent the parent of right
      if (parent == null) {
        root = right;
        right.parent = null;
      } else if (parent.left == pivot) {
        parent.left = right;
        right.parent = parent;
      } else {
        parent.right = right;
        right.parent = parent;
      }

    }
  }

  /**
   * Prerequisite: the given target must have left child
   *
   *      5              3
   *     / \            / \
   *    3  7           2  5
   *   /\                / \
   *  2 4               4  7
   */
  public void rightRotate(RBNode pivot) {
    if (pivot != null) {

      RBNode left = pivot.left;
      RBNode parent = pivot.parent;

      // make left.right the left child of pivot
      if (left.right != null) {
        pivot.left = left.right;
        left.right.parent = pivot;
      } else {
        pivot.left = null;
      }

      // make pivot the right child of left
      left.right = pivot;
      pivot.parent = left;

      // make pivot.parent the parent of left
      if (parent == null) {
        root = left;
        left.parent = null;
      } else if (parent.left == pivot) {
        parent.left = left;
        left.parent = parent;
      } else {
        parent.right = left;
        left.parent = parent;
      }
    }
  }

}