/**
 * Created by LIWENXIN on 3/9/16.
 */
public class WeighedQuickUnionDS {
    private int[] parent;
    private int[] size;

    public WeighedQuickUnionDS(int n) {
        parent = new int[n];
        size  = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    private int find(int p) {
        if (p == parent[p]) {
            return p;
        } else {
            parent[p] = find(parent[p]);
            return parent[p];
        }
    }

    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    public void connect(int p, int q) {
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
