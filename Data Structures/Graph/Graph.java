/**
 * Created by LIWENXIN on 4/9/16.
 */

import java.util.Stack;

/** http://algs4.cs.princeton.edu/41graph/Graph.java.html
 *  无向图
 */
public class Graph {
    private final int V;
    private int E;
    private Bag<Integer>[] adj;

    public Graph(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Number of vertices must be non-negative");
        }
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

    // 本来有一个从一个读入(In)读入一个图的constructor 这边不写了

    public Graph(Graph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            Stack<Integer> reverse = new Stack<Integer>();  // 这里的两个循环可以用来按照顺序复制一个队列
            for (int w : G.adj(v)) {                        // 用来把原来图每个vertex的adj复制到新的图的adj[]中
                reverse.push(w);
            }
            for (int w : reverse) {
                adj[v].add(w);
            }
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    /** 好的习惯...每次读入一个vertex的时候都判断一下这个vertex的存在性 */
    private void validateVertex(int v) {
        if (v < 0 || v >= V) {
            throw new IndexOutOfBoundsException();
        }
    }

    /** 新建一条边 */
    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        E++;
        adj[v].add(w);
        adj[w].add(v);
    }

    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    /** 实用性很强的一个method 可以学习一下 */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + "vertices, " + E + "edges" + "\r");
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (int w : adj[v]) {
                s.append(w + " ");
            }
            s.append("\r");
        }
        return s.toString();
    }
}
