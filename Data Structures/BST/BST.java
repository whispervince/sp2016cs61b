import java.util.*;

/**
 * Created by LIWENXIN on 3/10/16.
 */

/** 引用 http://algs4.cs.princeton.edu/32bst/BST.java.html
 *  非平衡树 存储key-value pair 存储顺序按左-中-右对应小-中-大的顺序
 *  1.对于K类型的Key 要求extends Comparable (有compareTo()method来进行比较)
 *  2.对于已经存在的key put()method会覆盖原先value的值 value不能为null
 *  3.由于不能保证树结构的平衡 put(), contains(), remove() 在最坏情况下都是O(N)的复杂度
 *  4.size()和constructor是O(1)的复杂度
 *  5.剩余的注释标注在每个method的开始位置
 */
public class BST<K extends Comparable<K>, V>{
    /** 树的每个节点
     *  存储了该点的key-value pair
     *  同时也指向左,右儿子
     */
    private class Node {
        private K key;
        private V value;
        private int size;
        private Node left, right;

        Node(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }

    /** 根节点 */
    private Node root;
    private Set<K> kSet = new HashSet<>();

    /** constructor并没有需要初始化的instance variable */
    public BST() {
    }

    /** Helper method
     *  当前元素为null时 返回0 保证不会出现NullPointer
     */
    private int size(Node x) {
        if (x == null) {
            return 0;
        }
        return x.size;
    }

    /** 返回当前树中一共有多少个节点 */
    public int size() {
        return size(root);
    }

    public void clear() {
        root = null;
    }

    /** Helper method 通过递归在x节点下找key对应的value 每次下一层
     *  不断判断key和当前节点的大小关系选择左还是右
     */
    private V get(Node x, K key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        } else if (cmp > 0) {
            return get(x.right, key);
        } else {
            return x.value;
        }
    }

    /** 默认value不能为null 在根节点及以下找有没有key */
    public boolean containsKey(K key) {
        if (key == null) {
            throw  new NullPointerException();
        }
        return get(root, key) != null;
    }

    public V get(K key) {
        return get(root, key);
    }

    /** Helper method 通过递归确定key应该在的位置 然后覆盖存在的节点/创建新的节点
     *  新加入的节点必然在最后一层 所以在x == null的时候表示到达对应的位置 创建新的节点并返回给上一级的.left或.right
     *  每层都要重新计算一次size
     */
    private Node put(Node x, K key, V value) {
        if (x == null) {
            return new Node(key, value, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, value);
        } else if (cmp > 0) {
            x.right = put(x.right, key, value);
        } else {
            x.value = value;
        }
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (value == null) {                  //如果读入的value是null 需要把这个节点删除 暂时还没有写
            delete(key);
            return;
        }
        root = put(root, key, value);
    }

    /** 在当前节点下找一个最小的key 返回这个节点 只要不断沿着左边向下找即可 */
    private Node min(Node x) {
        if (x.left == null) {
            return x;
        }
        return min(x.left);
    }

    public K min() {
        return min(root).key;
    }

    /** 找最小的key 用于keySet和iterator */

    /** 在当前节点下找一个最大的key 返回这个节点 只要不断沿着右边向下即可 */
    private Node max(Node x) {
        if (x.right == null) {
            return x;
        }
        return max(x.right);
    }

    /** 找最大的key并返回这个key */
    private K max() {
        return max(root).key;
    }

    /** 删除当前节点下的最大值 */
    private Node deleteMax(Node x) {
        if (x.right == null) {                 // 找到最大值(没有右儿子)后返回这个节点的左儿子 在上一层里赋值(左儿子是null也不影响)
            return x.left;
        }
        x.right = deleteMax(x.right);
        x.size = size(x.left) + size(x.right) + 1; // 维护经过的每一个节点的size
        return x;
    }

    /** 同上 */
    private Node deleteMin(Node x) {
        if (x.left == null) {
            return x.right;
        }
        x.left = deleteMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    /** 删除当前节点下的key对应的一个节点
     *  每一层返回的是"当前位置应该是什么"
     */
    private Node delete(Node x, K key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = delete(x.left, key);
        } else if (cmp > 0) {
            x.right = delete(x.right, key);
        } else {
            if (x.right == null) {      // 不会影响下层结构
                return x.left;
            } else if (x.left == null) {
                return x.right;
            } else {                    // 右侧肯定有元素
                Node temp = x;
//                if (size(temp.right) > size(temp.left)) {     // 自己加的 根据当前节点的左子树和右子树的大小确定是取哪一边 不知道对不对
//                    x = min(temp.right);
//                    x.right = deleteMin(temp.right);
//                    x.left = temp.left;
//                } else {
//                    x = max(temp.left);
//                    x.left = deleteMax(temp.left);
//                    x.right = temp.right;
//                }
                x = min(temp.right);
                x.right = deleteMin(temp.right);
                x.left = temp.left;
            }
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    /** 先记录当前节点对应的值 然后删除 */
    public V delete(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        V toDelete = get(key);
        root = delete(root, key);
        return toDelete;
    }

    /** 返回从小到大的所有的key */
    public Iterable<K> keys() {
        return keys(min(), max());
    }

    /** 返回从low到high的所有的key */
    public Iterable<K> keys(K low, K high) {
        if (low == null || high == null) {
            throw new NullPointerException();
        }
        Queue<K> queue = new ArrayDeque<>();
        keys(root, queue, low, high);
        return queue;
    }

    /** 递归到最小的key的位置 按照从小到大的顺序把key存进deque */
    private void keys(Node x, Queue<K> queue, K low, K high) {
        if (x == null) {
            return;
        }
        int cmpLow = low.compareTo(x.key);
        int cmpHigh = high.compareTo(x.key);
        if (cmpLow < 0) {
            keys(x.left, queue, low, high);
        }
        if (cmpLow <= 0 && cmpHigh >= 0) {
            queue.add(x.key);
        }
        if (cmpHigh > 0) {
            keys(x.right, queue, low, high);
        }
    }

    /** 中序遍历 按照key的大小从小到大打印 */
    private void printInOrder(Node x) {
        if (x == null) {
            return;
        }
        printInOrder(x.left);
        System.out.print(x.key + ", ");
        printInOrder(x.right);
    }

    public void printInOrder() {
        printInOrder(root);
        System.out.println();
    }
}

/** 由于在插入和删除操作时不能够保证树结构的平衡 数据结构可能退化成链表 所以最坏情况下的复杂度是O(N)\
 *  优化的方法在红黑树种会有解释
 */

