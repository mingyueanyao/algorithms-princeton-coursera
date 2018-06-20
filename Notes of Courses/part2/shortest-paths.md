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

### Data Stuctures

这里讨论单点最短路径，我们用两个以点为索引的数组来表示最短路径树（SPT）。

![spt](https://images2018.cnblogs.com/blog/886021/201806/886021-20180619144638192-620853787.png)

edgeTo[i] 表示从点 0 到点 i 的最短路径上的最后一条边，用来还原最短路径，edgeTo[0] 记为 null。distTo[i] 即表示从点 0 到点 i 的最短路径长度，distTo[0] 为 0。

```java
public double distTo(int v) {
    return distTo[v];
}

public Iterable<DirectedEdge> pathTo(int V) {
    Stack<DirectedEdge> path = new Stack<DirectedEdge>();
    for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from])
        path.push(e);
    return path;
}
```

### Relaxation

我们的最短路径 API 的实现都基于一个被称为**松弛**（relaxation）的简单操作。想象把一根橡皮筋沿最短路径拉长，找到更短的路径，也就“松弛”了这根橡皮筋。

#### Edge

放松边 v->w 就是要检查源点 s 到点 w 经过这条边是否会更短。

```java
private void relax(DirectedEdge e) {
    int v = e.from(), w = e.to();
    if (distTo[w] > distTo[v] + e.weight()) {
        distTo[w] = distTo[v] = e.weight();
        edgeTo[w] = e;
    }
}
```

下图中，左边例子称为边失效，右边则说放松是成功的。

![relaxation-edge](https://images2018.cnblogs.com/blog/886021/201806/886021-20180619144738132-1473930107.png)

#### Vertex

点的松弛即放松由其发出的所有边。

```java
private void relax(EdgeWeightedDigraph G, int v) {
    for (DirectedEdge e : G.adj(v)) {
        int w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }
    }
}
```

### Shortest-paths Optimality Conditions

**最优性条件**：当且仅当对于任意从点 v 到点 w 的边 e，满足 distTo[w]$\leqslant$distTo[v]+e.weight()（没有可以放松的有效边），那么 distTo[w] 是从 s 到 w 的最短路径长度。

#### 证明

- **必要性**

    假设 distTo[w] 是 s 到 w 的最短路径。若存在边 e(v->w) 有 distTo[v]+e.weight()<distTo[w]，显然从 s 到 v 再经 e 到 w 更短，矛盾。

- **充分性**

    假设 s=$v_{0}$->$v_{1}$->$v_{2}$->...->$v_{k}$=w 是 s 到 w 的最短路径，其权重记为 $OPT_{sw}$，$e_{i}$ 表示路径上的第 i 条边，有：

    distTo[$v_{1}$] $\leqslant$ distTo[$v_{0}$] + $e_{1}$.weight()

    distTo[$v_{2}$] $\leqslant$ distTo[$v_{1}$] + $e_{2}$.weight()

    ...

    distTo[$v_{k}$] $\leqslant$ distTo[$v_{k-1}$] + $e_{k}$.weight()

    综合这些不等式并去掉 distTo[$v_{0}$] = distTo[s] = 0:

    distTo[w] = distTo[$v_{k}$] $\leqslant$ $e_{1}$.weight() + $e_{2}$.weight() + ... + $e_{k}$.weight() = $OPT_{sw}$

    又因为 distTo[w] 是从 s 到 w 的某条路径的长度，不会比最短路径更短，所以下列式子成立。

    $OPT_{sw} \leqslant$ distTo[w] $\leqslant OPT_{sw}$

### Generic Shortest-paths Algorithm

由上述最优性条件马上可以得到一个计算单点最短路径问题的 SPT 的通用算法：

- 将 distTo[s] 初始化为 0，其它 distTo[] 元素初始化为无穷大。
- 重复放松图 G 中的任意边，直到不存在有效边为止（满足最优性条件）。

#### Pf

- 算法会把 distTo[v] 赋值成某条从 s 到 v 的路径长，且 edgeTo[v] 是该路径的最后一条边。
- 对于 s 可到达的点 v，distTo[v]初始为无穷大，肯定存在有效边。
- 每次成功的放松都会减少某些 distTo[v]，distTo[v] 减少的次数是有限的。

**注**：暂时不考虑负权重。

通用算法没有指定边放松的顺序，它为我们提供了证明算法可以计算 SPT 的方式：证明算法会放松所有边直到没有有效边。

## Dijkstra's Algorithm

Dijkstra 算法采用和 Prim 算法构建 MST 类似的策略来构建 SPT：每次添加的都是离起点最近的非树顶点。

![dijkstra-sample-graph](https://images2018.cnblogs.com/blog/886021/201806/886021-20180620162058537-2144493005.png)

从起点 s(0) 开始，维护两个数组 distTo 和 edgeTo 来表示 SPT，先把 distTo[0] 置为 0.0，其它 distTo 元素置为无穷大，edgeTo[0] 置为 null。

最初 distTo 数组中最小的非树点（离 SPT 最近的非树顶点）即是 distTo[0]，把点 0 加入 SPT 并进行放松操作。其它 distTo 被初始化为无穷大，点 0 发出的都是有效边，更新对应的 distTo 和 edgeTo 元素。

现在 distTo 数组中最小的是非树顶点是 distTo[1]（显然还需要索引优先队列来帮我们快速获取离 SPT 最近的非树顶点），加入 SPT 并放松点 1。distTo[2] 和 distTo[3] 初始更新，边 1-7 无效。

![dijkstra-sample1](https://images2018.cnblogs.com/blog/886021/201806/886021-20180620162112922-1873018993.png)

现在点 7 是离 SPT 最近的非树点，加入 SPT 并放松，边 2-7 有效，更新 distTo[2]，edgeTo[2] 变为 7->2。

![dijkstra-sample2](https://images2018.cnblogs.com/blog/886021/201806/886021-20180620162124756-1699453755.png)

每次挑离 SPT 最近的非树点加入 SPT 并进行放松操作，直到可到达的点都被加入 SPT，也就完成了计算。

![dijkstra-sample-result](https://images2018.cnblogs.com/blog/886021/201806/886021-20180620164946867-1643933501.png)

s 可到达的点都只会被放松一次，当 v 被放松时，有 distTo[w]$\leqslant$distTo[v]+e.weight()，而且该不等式在算法结束前都会成立，因为：

- distTo[w] 不会增加。因为放松操作只有可能减少 distTo[w]。
- distTo[v] 不会改变。边的权重非负，我们每次选择的又都是最小的 distTo[] 值，后面的放松操作不可能使任何 distTo[] 的值小于 distTo[v]。

满足最优性条件，所以 Dijkstra 算法可以解决边权重非负的加权有向图的单点最短路径问题。

### Dijkstra: Java Implementation

```java
public class DijkstraSP {
    private DirectedEdge[] edgeTo;
    private double[] distTo;
    private IndexMinPQ<Double> pq;

    public DijkstraSP(EdgeWeightedDigraph G, int s) {
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];
        pq = new IndexMinPQ<Double>(G.V());

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        pq.insert(s, 0.0);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }
    }

    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) {
                pq.decreaseKey(w, distTo[w]);
            } else {
                pq.insert(w, distTo[w]);
            }
        }
    }
}
```

时间复杂度取决于优先队列的实现，二叉堆的话是 $ElogV$ 级别。

## Edge-weighted DAGs

## Negative Weights