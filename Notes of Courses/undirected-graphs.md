# 无向图

## Introduction

图是由边连接的点的集合，有着广泛的应用空间。

![graph-application](https://images2018.cnblogs.com/blog/886021/201806/886021-20180604153832516-1301151106.png)

一些图的术语，点，边，路径，环（圈），连通分量（子图）。

![graph-terminology](https://images2018.cnblogs.com/blog/886021/201806/886021-20180604153950204-1127408589.png)

## Graph-API

对于有 V 个节点的图，我们使用整数 0 到 V-1 来简化表示，反正可以用符号表把实际名称和数字对应起来。实际中可能有自环和平行边，但示例中一般不画出来。

![anomalies](https://images2018.cnblogs.com/blog/886021/201806/886021-20180604170225502-1577370842.png)

惯例先给出 API 和示例程序，然后再谈具体实现。

### API

![api](https://images2018.cnblogs.com/blog/886021/201806/886021-20180604162942787-1506140236.png)

### Sample Client

![sample-client](https://images2018.cnblogs.com/blog/886021/201806/886021-20180604163001109-1537608465.png)

### 运行示例

![result](https://images2018.cnblogs.com/blog/886021/201806/886021-20180604163017911-870034841.png)

在这些简单的基础操作之上，我们才能实现一些常用的图处理代码，像是计算 v 的度（有几条边），图的最大度等等，现在的问题是用哪种方式（数据结构）来表示图并实现这份 API。

### Set-of-edges

维护一个包含图中所有边的集合，用数组或是链表实现。但是，这样的话实现 adj() （查询某个节点的所有边）需要检查图中所有的边，显然太慢了。

### Adjacency-matrix

邻接矩阵。维护一个 v 乘 v 的布尔矩阵 adj[][]，若节点 v 和节点 w 之间有条边，则 adj[v][w] 和 adj[w][v] 为 true 。但是，这样的话，显然当 v 很大的时候会需要很大的内存，也是不合适的，而且邻接矩阵也不支持平行边。

### Adjacency-list

邻接表。维护一个链表的数组，每个链表对应一个节点所有与它相连的点。

![adjacency-list](https://images2018.cnblogs.com/blog/886021/201806/886021-20180604165613424-1939003304.png)

在实际中，我们使用邻接表来表示图，因为算法基于迭代点的相邻点，且真实世界中的往往是稀疏图（点相对边来说多得多）。邻接表需要的内存正比于 E+V，添加边需要的时间是常数，判断两点间是否有边的时间和点的度成正比，遍历点的所有相邻点的时间和点的度成正比，对这些操作来说已经是最优的了。

#### Java Implementation

```java
public class Graph {
    private final int V;
    private Bag<Integer>[] adj;

    public Graph(int V) {
        this.V = V;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Integer>();
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
    }

    public Iterable<Integer> adj(int v) {
        return adj[v];
    }
}
```

## Depth First Search

## Breadth First Search

## Connected Components

## Challenges