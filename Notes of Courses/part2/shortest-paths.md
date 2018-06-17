# 最短路径

## APIs

带权有向图中的最短路径，这节讨论从源点（s）到图中其它点的最短路径（single source）。

![shortest-path](https://images2018.cnblogs.com/blog/886021/201806/886021-20180617222508697-1097652278.png)

### Weighted Directed Edge API

需要新的数据类型来表示带权有向边。

![directed-edge-api](https://images2018.cnblogs.com/blog/886021/201806/886021-20180617222530688-1810632250.png)

#### Weighted Directed Edge：implementation

```java
public class DirectedEdge {
    private final int v, w;
    private final double weight;

    public DirectedEdge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    publiv int weight() {
        return weight;
    }
}
```

习惯上处理边 e 的时候先获取端点：int v = e.from(), w = e.to();

### Edge-weighted Digraph API

![edge-weighted-digraph-api](https://images2018.cnblogs.com/blog/886021/201806/886021-20180617222626289-860779925.png)

依然是使用邻接表表示，保存的是指向边对象的引用。

![edge-weighted-digraph-representation](https://images2018.cnblogs.com/blog/886021/201806/886021-20180617222607579-1548783506.png)

#### Edge-weighted Digraph:implementation

```java
public class EdgeWeightedDigraph {
    private final int V;
    private final Bag<DirectedEdge>[] adjl

    public EdgeWeightedDigraph(int V) {
        this.V = V;
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<DirectedEdge>();
    }

    publiv void addEdge(DirectedEdge e) {
        int v = e.from();
        adj[v].add(e);
    }

    public Iterable<DirectedEdge> adj(int V) {
        return adj[v];
    }
}
```

### Single-source Shortest Paths API

![sp-api](https://images2018.cnblogs.com/blog/886021/201806/886021-20180617222700998-1812994653.png)

#### 测试用例

```java
SP sp = new SP(G, s);
for (int v = 0; v <G.V(); v++) {
    StdOut.printf("%d to %d (%.2f): ", s, v, sp.distTo(v));
    for (DirectedEdge e : sp.pathTo(v))
        StdOut.print(e + " ");
    StdOut.println();
}
```

#### 运行示例

![sp-sample](https://images2018.cnblogs.com/blog/886021/201806/886021-20180617225827865-209079891.png)

注：上述内容，详细的可以参考 [booksite-4.4](https://algs4.cs.princeton.edu/44sp/)。

## Shortest-paths Properties

## Dijkstra's Algorithm

## Edge-weighted DAGs

## Negative Weights