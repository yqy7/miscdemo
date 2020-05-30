package com.github.yqy7.miscdemo;

/**
 * 红黑树：这个版本删除了 value，在学习红黑树的时候通常不会提到 value，value 可有可无。<br/>
 * 红黑树是由节点构成的树，节点中保存的东西(或者用payload来描述)只要是可比较的(在 java 里面就是实现了 Comparable)，就能构造红黑树。<br/>
 * 在 TreeMap 中节点用 Entry 来描述，可能会让初学者看不懂，这里改成 Node 更符合很多资料上的描述。<br/>
 * Node 或者 Entry 都可以理解为一个容器，里面保存了一个可以比较的东西，即 key，另外可以附加保存一些东西比如 value(也可以保存更多，但是都不是构造红黑树必要的)。<br/>
 * Node 中虽然没有 value，但是如果需要保存一些和 key 相关的东西，可以放在 key 这个对象本身。比如 key 的类型 K 中增加一个 value 字段来保存 key 对应的东西。<br/>
 * <br/>
 * 红黑树的5个性质：<br/>
 * 1.节点由红和黑构成 2.根节点黑色 3.叶子结点黑色 <br/>
 * 4.如果一个节点是红的，则其两个孩子是黑的（不能存在两个连续红色节点）<br/>
 * 5.每个节点，从该节点出发的每条路径中黑色节点的数量相同<br/>
 * 红黑树主要通过<strong>变色</strong>和<strong>旋转</strong>两种操作来保持平衡。
 */
public class RBTree3<K extends Comparable> {
    private static final boolean RED   = false;
    private static final boolean BLACK = true;

    private transient Node<K> root;
    private transient int size = 0;

    static final class Node<K> {
        K key;
        Node<K> left;
        Node<K> right;
        Node<K> parent;
        boolean color = BLACK;

        Node(K key, Node<K> parent) {
            this.key = key;
            this.parent = parent;
        }

        public K getKey() {
            return key;
        }

        public String toString() {
            return key.toString();
        }
    }

    // 基本操作

    // put 改成 add 更合适
    public void put(K key) {
        if (key == null)
            throw new NullPointerException();

        Node<K> t = root;
        // 根节点为空的情况
        if (t == null) {
            root = new Node<>(key, null); // 已经是黑色
            size = 1;
            return;
        }

        int cmp;
        Node<K> parent;
        // 从根节点开始，寻找插入位置（保存在parent）。如果遇到了相同的key，则不操作
        do {
            parent = t;
            cmp = key.compareTo(t.key);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else
                return;
        } while (t != null);

        // 创建需要插入的节点
        Node<K> e = new Node<>(key, parent);
        if (cmp < 0)
            parent.left = e;
        else
            parent.right = e;

        // 插入之后进行平衡修复
        fixAfterInsertion(e);
        size++;
    }

    // 获取，没什么用，可以用来判断元素是否存在
    public K get(K key) {
        Node<K> p = getNode(key);
        return (p == null ? null : p.key);
    }

    final Node<K> getNode(K key) {
        if (key == null)
            throw new NullPointerException();
        Node<K> p = root;
        while (p != null) {
            int cmp = key.compareTo(p.key);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }
        return null;
    }

    public K remove(K key) {
        Node<K> p = getNode(key);
        if (p == null)
            return null;

        K oldKey = p.key;
        deleteNode(p);
        return oldKey;
    }

    /**
     * 删除先看子节点数量：（先看子节点数量、再看兄弟节点的颜色、再看兄弟节点的子节点颜色） <br/>
     * 2个子节点用后继替换，后继转换成1个或0个子节点情况（后继要么没有子节点，要么只有一个右节点） <br/>
     * 1个子节点只会是黑红（红黑、黑黑多1个，红红不存在），删除当前节点后把子节点变黑 <br/>
     * 0个子节点如果是红色删除即可，如果当前节点是黑色： <br/>
     *     兄弟节点为红色，则父节点肯定为黑色且两个孩子节点为黑色，交换兄弟节点和父节点颜色，然后旋转，原兄弟节点的子节点编程新黑兄弟节点 <br/>
     *     兄弟节点为黑色： <br/>
     *         兄弟节点的子节点全黑，则兄弟节点变红，使平衡上升到父节点。父节点为红，变黑即可，父节点为黑，递归处理 <br/>
     *         兄弟节点的子节点不全黑，则左左、右右旋转后变色（左左、右右是黑-红） <br/>
     */
    private void deleteNode(Node<K> p) {
        size--;

        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) { // 2个子节点的情况，用后继替换，就变成了0或者1个子节点的情况
            Node<K> s = successor(p);
            p.key = s.key;
            p = s;
        } // p has 2 children

        // Start fixup at replacement node, if it exists.
        Node<K> replacement = (p.left != null ? p.left : p.right); // 用来替换的子节点，可能是null、可能是左、可能是右

        if (replacement != null) {
            // replacement不为null，说明是1个子节点的情况
            // Link replacement to parent
            // 子节点替换当前节点
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
                p.parent.right = replacement;

            // Null out links so they are OK to use by fixAfterDeletion.
            p.left = p.right = p.parent = null;

            // Fix replacement
            if (p.color == BLACK) // 只有一个节点，只会是黑红，修复方法就是把替换的节点变成黑色。fixAfterDeletion的while条件不会满足
                fixAfterDeletion(replacement);
        } else if (p.parent == null) { // return if we are the only node.
            // 节点是根节点并且没有子节点，直接删除了
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            // 0个子节点的情况
            if (p.color == BLACK)
                fixAfterDeletion(p);

            // 从父节点中删除该节点
            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }

    // 平衡操作

    private static <K> boolean colorOf(Node<K> p) {
        return (p == null ? BLACK : p.color);
    }

    private static <K> Node<K> parentOf(Node<K> p) {
        return (p == null ? null: p.parent);
    }

    private static <K> void setColor(Node<K> p, boolean c) {
        if (p != null)
            p.color = c;
    }

    private static <K> Node<K> leftOf(Node<K> p) {
        return (p == null) ? null: p.left;
    }

    private static <K> Node<K> rightOf(Node<K> p) {
        return (p == null) ? null: p.right;
    }

    /** From CLR */
    private void rotateLeft(Node<K> p) {
        if (p != null) {
            Node<K> r = p.right;
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    /** From CLR */
    private void rotateRight(Node<K> p) {
        if (p != null) {
            Node<K> l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

    /** From CLR */
    /**
     * 插入总是红色并且只会出现在叶子节点（因为总可以找到这样的位置）。<br/>
     * 如果出现双红，看叔节点，叔节点红色则递归变色；叔节点黑色则左左、右右旋转后变色（左左、右右是红-红）
     */
    private void fixAfterInsertion(Node<K> x) {
        // 插入的节点总是红色
        x.color = RED;

        while (x != null && x != root && x.parent.color == RED) {
            // 如果出现双红
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) { // 父节点是左节点
                // 看叔节点的颜色
                Node<K> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) { // 叔节点是红色，这时祖父节点只会是黑色，因为父节点也是红色，让父节点、叔节点变黑，然后祖父节点变红不会影响路径的黑色数量，把问题上升到祖父节点
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    // 叔节点是黑色
                    if (x == rightOf(parentOf(x))) {
                        // 父节点是左，当前节点是右，即左-右，通过左旋变成左-左
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    // 处理左-左情况，父节点和祖父节点交换颜色，然后进行右旋
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else { // 父节点是右节点
                // 看叔节点的颜色
                Node<K> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) { // 叔节点是红色，递归变色
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    // 叔节点是黑色
                    if (x == leftOf(parentOf(x))) {
                        // 父节点是右，当前节点是左，即右-左，通过右旋变成右-右
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    // 处理右-右情况，父节点和祖父节点交换颜色，然后进行左旋
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK; // 上面循环内可能会把跟节点变成红色，因此这里总是把根节点变成黑色
    }

    /** From CLR */
    private void fixAfterDeletion(Node<K> x) {
        while (x != root && colorOf(x) == BLACK) { // 要删除的节点是黑色节点
            if (x == leftOf(parentOf(x))) { // 当前节点是左节点
                Node<K> sib = rightOf(parentOf(x)); // 看兄弟节点

                if (colorOf(sib) == RED) { // 兄弟节点为红色，则父节点肯定为黑色且两个孩子节点为黑色，交换兄弟节点和父节点颜色，然后旋转，原兄弟节点的子节点编程新黑兄弟节点
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                } // 这一步如果执行，会出现新的兄弟节点并且是黑节点

                // 通过上一步，可以确定 当前节点 和 其兄弟节点 都是黑色
                if (colorOf(leftOf(sib))  == BLACK &&
                    colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED); // 兄弟节点的子节点全黑，则兄弟节点变红，父节点以下黑-1，使平衡上升到父节点。父节点为红，变黑即可，父节点为黑，递归处理
                    x = parentOf(x); // 父节点为黑，递归处理。父节点如果为红色，出现双红。但是把父节点变成黑色刚好黑+1。变黑在最后一行代码 setColor(x, BLACK);
                } else { // 兄弟节点的子节点不全黑，因为兄弟节点是右节点，而红色节点是左，即黑-红为右左，需要转变成右-右。交换颜色然后旋转兄弟节点
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    // 变成右-右之后，交换父节点和新兄弟节点的颜色，然后左旋
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK); // 父节点补上删除节点的黑-1
                    setColor(rightOf(sib), BLACK); // 兄弟节点的右子节点补上兄弟节点的黑-1
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // symmetric // 当前节点是右节点，逻辑同上
                Node<K> sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == BLACK &&
                    colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, BLACK);
    }

    // 辅助操作

    // 节点t的后一个节点
    static <K> Node<K> successor(Node<K> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            Node<K> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            Node<K> p = t.parent;
            Node<K> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    // 获取最小的节点
    final Node<K> getFirstNode() {
        Node<K> p = root;
        if (p != null)
            while (p.left != null)
                p = p.left;
        return p;
    }

    @Override
    public String toString() {
        Node<K> e = getFirstNode();
        if (e == null)
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');

        while (true) {
            K key = e.getKey();
            sb.append(key);
            e = successor(e);
            if (e == null)
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }
    }
}
