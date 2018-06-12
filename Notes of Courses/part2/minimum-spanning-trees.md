# 最小生成树

## Introduction

图的生成树是它的一棵含有其所有顶点的无环连通子图。一幅加权无向图的最小生成树（MST）是它的一棵权值（树中所有边的权值之和）最小的生成树。

![MST](https://images2018.cnblogs.com/blog/886021/201806/886021-20180612153054189-895207847.png)

## Greedy Algorithm

假定图是连通的，且各个边有不同的权值，这样图就会存在唯一一棵最小生成树。

### Cut Property

切分将图的所有点分为两个非空且不重复的集合，横切边指连接两个集合的边。

**切分定理**：在一幅加权图中，给定任意的切分，它的横切边中权重最小者必然属于图的最小生成树。

![cut-property](https://images2018.cnblogs.com/blog/886021/201806/886021-20180612155536372-57077706.png)

#### 证明

用反证法，假设图的最小生成树 T 不包含权重最小的横切边 e 。现将 e 加入 T ，则会产生一个包含 e 的环，而这个环必然至少含有另一条横切边 f 。又有 e < f ，此时我们删掉 f ，则可以得到一棵更小的生成树。矛盾。

![cut-proper-pf](https://images2018.cnblogs.com/blog/886021/201806/886021-20180612172504776-607558724.png)

切分定理是解决最小生成树问题的所有算法的基础，这些算法都是一种贪心算法，每次选择一条权重最小的横切边，不断重复直到找到最小生成树的所有边，不同之处在于如何切分和判定权重。

## Edge-Weighted Graph API

惯例先给出 API 再说具体要怎么实现。现在是加权图，需要给边新的表示。

### Weighted Edge API

![weighted-edge-api](https://images2018.cnblogs.com/blog/886021/201806/886021-20180612165350328-663593995.png)

获取边 e 的端点：int v = e.either(), w = e.other(v)，看下面的实现就能理解。

#### Weighted Edge: Java Implementation

```java
public class Edge implements Comparable<Edge> {
    private final int v, w;
    private final double weight;

    private Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int either() {
        return v;
    }

    public int other(int vertex) {
        if (vertex == v) {
            return w;
        } else {
            return v;
        }
    }

    public int compareTo(Edge that) {
        if (this.weight < that.weight) {
            return -1;
        }
        else if (this.weight > that.weight) {
            return +1;
        } else {
            return 0;
        }
    }
}
```

### Edge-weighted Graph API

![edge-weighted-graph-api](https://images2018.cnblogs.com/blog/886021/201806/886021-20180612165449679-1141971280.png)

仍然使用邻接表来表示加权图，边的两个端点都有存指向边的引用，实际上表示边的对象只有一个。

![weighted-edge-graph-adjacency-list](https://images2018.cnblogs.com/blog/886021/201806/886021-20180612165716324-1873284478.png)

#### Adjacency-lists Implementation

```java
public class EdgeWeightedGraph {
    private final int V;
    private final Bag<Edge>[] adj;

    public EdgeWeightedGraph(int V) {
        this.V = V;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Edge>();
        }

        public void addEdge(Edge e) {
            int v = e.either();
            int w = e.other(v);
            adj[v].add(e);
            adj[w].add(e);
        }

        public Iterable<Edge> adj(int v) {
            return adj[v];
        }
    }
}
```

### MST-API

![mst-api](https://images2018.cnblogs.com/blog/886021/201806/886021-20180612165745854-918143522.png)

#### Sample Client

```java
public static void main(String[] args) {
    In in = new In(Args[0]);
    EdgeWeightedGraph G = new EdgeWeightedGraph(in);
    MST mst = new MST(G);
    for (Edge e : mst.edges()) {
        StdOut.println(e);
    }
    Stdout.println("%.2f\n", mst.weight());
}
```

#### 运行示例

![sample](https://images2018.cnblogs.com/blog/886021/201806/886021-20180612165808252-1966436108.png)

注：上面贴的大都不完整，完整的可以在 [booksite-4.3](https://algs4.cs.princeton.edu/43mst/) 找到。

## Kruskal's Algorithm

## Prim's Algorithm

## Context