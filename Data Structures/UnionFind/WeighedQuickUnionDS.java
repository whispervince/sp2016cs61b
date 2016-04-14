import java.util.ArrayList;

/**
 * Created by LIWENXIN on 3/9/16.
 */
public class WeighedQuickUnionDS {
    private int[] parent;
    private int[] size;

    public WeighedQuickUnionDS(int n) {     // 两个array 一个存当前节点的父亲节点 一个存当前节点下面的元素个数
        parent = new int[n];
        size  = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    private int find(int p) {               // 当找到当前节点等于他的父亲节点时(表示已经到根) 返回这个值
        if (p == parent[p]) {
            return p;
        } else {
            parent[p] = find(parent[p]);    // 优化 在找这个节点的位置时 是从下往上找的 将经过的所有的节点都直接放到根节点下
            return parent[p];               // 压缩树的高度 提升效率
        }
    }

    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    } // 从下往上查一遍树

    public void connect(int p, int q) {     // 将两个节点连起来
        int i = find(p);
        int j = find(q);

        if (i == j) {return; }
        if (size[i] < size[j]) {
            parent[i] = j;
            size[j] += size[i];
        } else {
            parent[j] = i;
            size[i] += size[j];
        }
    }
}

/** 复杂度是logN */