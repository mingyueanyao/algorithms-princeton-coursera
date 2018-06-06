# 有向图

## Introduction

就是边是有方向的，像单行道那样，也有很多典型的应用。

![digraph-applications](https://images2018.cnblogs.com/blog/886021/201806/886021-20180606233630897-1391024623.png)

点的出度指从这个点发出的边的数目，入度是指向点的边数。当存在一条从点 v 到点 w 的路径时，称点 v 能够到达点 w ，但要注意这并不意味着点 w 可以到达点 v 。

![digraph-terminology](https://images2018.cnblogs.com/blog/886021/201806/886021-20180606233613732-254568405.png)

## Digraph API

先给出表示有向图的 API 以及简单的测试用例，[booksite-4.2](https://algs4.cs.princeton.edu/42digraph/) 上可以找到完整的。

### API

![digraph-api](https://images2018.cnblogs.com/blog/886021/201806/886021-20180606234853162-389340235.png)

### Sample Client

![digraph-client](https://images2018.cnblogs.com/blog/886021/201806/886021-20180606234917241-1456277967.png)

### 运行示例

![digraph-result](https://images2018.cnblogs.com/blog/886021/201806/886021-20180606234938510-854990674.png)

仍然使用邻接表来实现有向图，比无向图还简单其实。

### Adjacency-list

![digraph-adjacency-list](https://images2018.cnblogs.com/blog/886021/201806/886021-20180606235255627-1575855040.png)

#### Java Implementation

```java
public class Digraph {
    private final int V;
    private final Bag<Integer>[] adj;    // adjacency lists

    public Digraph(int V) {
        this.V = V;
        // create empty graph with V vertices
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

    // add edge v->w
    public void addEdge(int v, int w) {
        adj[v].add(w);
    }

    // iterator for vertices pointing from v
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }
}
```

用邻接表来表示有向图，类似的内存使用正比于 E+V ，常数时间就能加入新边，判断点 v 到 w 是否有条边需要正比于点 v 出度的时间，遍历从点 v 发出的边也是。

## Digraph Search

同样的，可以直接用 [Undirected Graphs](https://www.cnblogs.com/mingyueanyao/p/9133805.html) 提到的 DFS 和 BFS 这两种搜索策略。

### DFS

```java
public class DirectedDFS {
    private boolean[] marked;    // true if path from s

    // constructor marks vertices reachable from s
    public DirectedDFS(Digraph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    // recursive DFS does the work
    private void dfs(Digraph G, int v) {
        marked[v] = true;
        for (int w : G.adl(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    // client can ask whether any vertex is reachable from s
    public boolean visited(int v) {
        return marked[v];
    }
}
```

#### 示例

![digraph-dfs](https://images2018.cnblogs.com/blog/886021/201806/886021-20180607001953505-502681596.png)

### BFS

随便来张图感受一下。

![digraph-bfs](https://images2018.cnblogs.com/blog/886021/201806/886021-20180607002015725-2005429335.png)

## Topological Sort

## Strong Components
