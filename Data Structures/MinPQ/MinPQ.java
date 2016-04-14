/** lab中的做法是用自带的arraylist 这样就不要写resize 会方便很多 */

/** http://algs4.cs.princeton.edu/24pq/MinPQ.java.html
 *  用array写的MinPQ Heap结构
 *
 *  注意一下各个位置加的throw exception 良好的习惯是啥来着
 */

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by LIWENXIN on 3/29/16.
 */
public class MinPQ<Key> implements Iterable<Key> {
    private Key[] pq;                               // 存储所有的key 从1开始
    private int N;                                  // pq中元素的个数
    private Comparator<Key> comparator;

    public MinPQ(int initCapacity) {
        pq = (Key[]) new Object[initCapacity + 1];  // 多开一位 因为第一位不存东西
        N = 0;
    }

    public MinPQ() {
        this(1);
    }

    public MinPQ(int initiCapacity, Comparator<Key> comparator) {
        this(initiCapacity);
        this.comparator = comparator;
    }

    public MinPQ(Comparator<Key> comparator) {
        this(1, comparator);
    }

    public MinPQ(Key[] keys) {
        N = keys.length;
        pq = (Key[]) new Object[keys.length + 1];
        for (int i = 0; i < N; i++) {
            pq[i + 1] = keys[i];
        }
        for (int k = N / 2; k >= 1; k--) {          // 最后一层不用管 从倒数第二层sink一遍
            sink(k);
        }
        assert isMinHeap();
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    /** 返回最小值(根) 不删除 */
    public Key min() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return pq[1];
    }

    private void resize(int capacity) {
        assert  capacity > N;
        Key[] temp = (Key[]) new Object[capacity];
        for (int i = 1; i <= N; i++) {
            temp[i] = pq[i];
        }
        pq = temp;
    }

    /** 插入 插在最后然后往上维护 */
    public void insert(Key x) {
        if (N == pq.length - 1) {
            resize(pq.length * 2);
        }
        pq[++N] = x;                                // 并不知道++N是干嘛的 感觉是N += 1然后直接用N
        swim(N);                                    // 最后一位 往上swim
        assert isMinHeap();
    }

    /** 取出最小值(根节点) */
    public Key delMin() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        exch(1, N);                                 // 把最后一位和第一位换一下
        Key min = pq[N--];                          // 讲道理的话 先取N位置上的值 然后N--
        sink(1);                                    //
        pq[N + 1] = null;                           // 然后把删掉的这个节点变成null
        if ((N > 0) && (N == (pq.length - 1) / 4)) {    // 变小的resize的判断
            resize(pq.length / 2);
        }
        assert isMinHeap();
        return min;                                 // 返回最后一位
    }

    /** 向上交换 确定位置 */
    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            exch(k, k / 2);
            k /= 2;
        }
    }

    /** 向下交换 稍微烦一点因为每次要找到三个点中最小的一个 */
    private void sink(int k) {                      // 我觉得这个东西写的不优美啊
        while (2 * k <= N) {
            int j = k * 2;
            if (j < N && greater(j, j + 1)) {       // 判断是左儿子小还是右儿子小 确定新的位置
                j++;
            }
            if (!greater(k, j)) {                   // 如果k不比j大 就不要换
                break;
            }
            exch(k, j);
            k = j;
        }
    }

    /** 判断i是不是比j大 */
    private boolean greater(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0;
        } else {
            return comparator.compare(pq[i], pq[j]) > 0;
        }
    }

    /** 交换两个node */
    private void exch(int i, int j) {
        Key temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
    }

    private boolean isMinHeap() {
        return isMinHeap(1);
    }

    /** 判断每个节点和他左右儿子的大小关系 */
    private boolean isMinHeap(int k) {
        if (k > N) {
            return true;
        }
        int left = k * 2;
        int right = k * 2 + 1;
        if (left <= N && greater(k, left)) {
            return false;
        }
        if (right <= N && greater(k, right)) {
            return false;
        }
        return isMinHeap(left) && isMinHeap(right);
    }

    public Iterator<Key> iterator() {
        return new HeapIterator();
    }

    /** 一个简单的iterator 把pq复制一遍 每次删一个root */
    private class HeapIterator implements Iterator<Key> {
        private MinPQ<Key> copy;

        public HeapIterator() {
            if (comparator == null) {
                copy = new MinPQ<Key>(size());
            } else {
                copy = new MinPQ<Key>(size(), comparator);
            }
            for (int i = 1; i <= N; i++) {
                copy.insert(pq[i]);
            }
        }

        public boolean hasNext() {
            return !copy.isEmpty();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Key next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return copy.delMin();
        }
    }
}

