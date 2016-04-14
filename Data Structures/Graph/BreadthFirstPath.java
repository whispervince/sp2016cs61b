import edu.princeton.cs.algs4.Queue;

import java.util.Stack;

/**
 * Created by LIWENXIN on 4/10/16.
 */
public class BreadthFirstPath {
    private static final int INFINITY = Integer.MAX_VALUE;
    private int[] edgeTo;
    private boolean[] marked;
    private int[] distTo;

    /** bfs from a single source point */
    public BreadthFirstPath(Graph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        bfs(G, s);

        assert check(G, s);
    }

    /** bfs from multiple source points */
    public BreadthFirstPath(Graph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            distTo[v] = INFINITY;
        }
        bfs(G, sources);
    }

    /** 复杂度是O(V + E) */
    private void bfs(Graph G, int s) {
        Queue<Integer> fringe = new Queue<Integer>();
        for (int v = 0; v < G.V(); v++) {
            distTo[v] = INFINITY;
        }
        marked[s] = true;
        distTo[s] = 0;
        fringe.enqueue(s);
        while (!fringe.isEmpty()) {
            int v = fringe.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    fringe.enqueue(w);
                }
            }
        }
    }

    private void bfs(Graph G, Iterable<Integer> sources) {
        Queue<Integer> fringe = new Queue<Integer>();
        for (int w : sources) {
            distTo[w] = 0;
            marked[w] = true;
            fringe.enqueue(w);
        }
        while (!fringe.isEmpty()) {
            int v = fringe.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    fringe.enqueue(w);
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public int distTo(int v) {
        return distTo[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }
        Stack<Integer> path = new Stack<>();
        int x;                                          // 这边把x独立出来是因为要处理有多个resource point的情况
        for (x = v; distTo[x] != 0; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(x);
        return path;
    }

    /** 检查single source point有没有问题...
     *  假设v is reachable from s
     */
    private boolean check(Graph G, int s) {
        if (distTo[s] != 0) {
            System.out.println("Distance to source " + s + "to itself is " + distTo[s]);
            return false;
        }
        // 判断相邻的两个点是否联通 距离是否小于等于1
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                if (hasPathTo(v) != hasPathTo(w)) {
                    System.out.println("Edge " + v + "-" + w);
                    System.out.println("hasPathTo(" + v + ") = " + hasPathTo(v));
                    System.out.println("hasPathTo(" + w + ") = " + hasPathTo(w));
                    return false;
                }
                if (hasPathTo(v) && distTo[w] > distTo[v] + 1) {
                    System.out.println("Edge " + v + "-" + w);
                    System.out.println("distTo[" + v + "] = " + hasPathTo(v));
                    System.out.println("distTo[" + w + "] = " + hasPathTo(w));
                    return false;
                }
            }
        }
        for (int w = 0; w < G.V(); w++) {
            if (!hasPathTo(w) || w == s) {
                continue;
            }
            int v = edgeTo[w];
            if (distTo[w] != distTo[v] + 1) {
                System.out.println("Shortest path edge " + v + "-" + w);
                System.out.println("distTo[" + v + "] = " + hasPathTo(v));
                System.out.println("distTo[" + w + "] = " + hasPathTo(w));
                return false;
            }
        }
        return true;
    }
}
