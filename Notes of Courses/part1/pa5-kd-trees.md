# 编程作业五

作业链接：[Kd-Trees](http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html) & [Checklist](http://coursera.cs.princeton.edu/algs4/checklists/kdtree.html)

我的代码：[PointSET.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part1/pa5-kdtree/PointSET.java) & [KdTree.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part1/pa5-kdtree/KdTree.java)

## 问题简介

是平衡树的几何应用中介绍的 2d 树，不再过多描述，要求实现范围查找和最近邻居这两个功能。

## 任务摘要

>**Brute-force implementation.** Write a mutable data type PointSET.java that represents a set of points in the unit square. Implement the following API by using a red–black BST:
>
>```java
>public class PointSET {
   >public         PointSET()                               // construct an empty set of points
   >public           boolean isEmpty()                      // is the set empty?
   >public               int size()                         // number of points in the set
   >public              void insert(Point2D p)              // add the point to the set (if it is not already in the set)
   >public           boolean contains(Point2D p)            // does the set contain point p?
   >public              void draw()                         // draw all points to standard draw
   >public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
   >public           Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
>
   >public static void main(String[] args)                  // unit testing of the methods (optional)
>}
>```
>
>**2d-tree implementation.** Write a mutable data type KdTree.java that uses a 2d-tree to implement the same API (but replace PointSET with KdTree). A 2d-tree is a generalization of a BST to two-dimensional keys. The idea is to build a BST with points in the nodes, using the x- and y-coordinates of the points as keys in strictly alternating sequence.

详细参见：[specification](http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html)。

## 问题分析

实现 2d 树之前，还要我们先来一个暴力算法，也就是范围查找和最近邻需要访问所有的点。作业给我们提供了二维点和矩形的数据类型：[Point2D](https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Point2D.html) 和 [RectHV](https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/RectHV.html)，还说暴力算法必须使用 [SET](https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/SET.html) 或 [java.util.TreeSet](https://docs.oracle.com/javase/8/docs/api/java/util/TreeSet.html)。因为 [Point2D](https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Point2D.html) 里有 [compareTo()](https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Point2D.html#compareTo(edu.princeton.cs.algs4.Point2D)) 方法，所以可以直接塞到平衡树里。那插入和查找的复杂度都是对数级别，而范围查找和最近邻的性能要求是所需时间和点的数量成正比，于是遍历就好。综上，暴力算法完全没有技术含量，直接看代码 [PointSET.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part1/pa5-kdtree/PointSET.java)。

接着是正题，API 没变，实现改成 2d 树，第一步来设计节点，我的长这样：

```java
public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

    private Node root;    // the root of KdTree
    private int size;     // the number of points in the KdTree

    // KdTree helper node data type
    private static class Node {
        private Point2D p;            // the point
        private Node lb;              // the left/bottom subtree
        private Node rt;              // the right/top subtree
        private boolean divide;       // true->vertical, false->horizontal

        public Node(Point2D p) {
            this.p = p;
        }
    }
}
```

[Checklist](http://coursera.cs.princeton.edu/algs4/checklists/kdtree.html) 里建议的编程步骤有给出样例，每个节点里有对应的矩形，像根节点对应 1*1 的最大矩形，但是后面又说实际上可以不用存来节省空间，所以我这个应该是后面自己思索的吧，时间太长不大记得。

方法 isEmpty() 和 size() 按道理要很简单，方法 insert() 需要注意些：

>**What should I do if a point has the same x-coordinate as the point in a node when inserting / searching in a 2d-tree?** Go the right subtree as specified.
>
>**What should I do if a point is inserted twice in the data structure?** The data structure represents a set of points, so you should keep only one copy.

摘自 [Checklist](http://coursera.cs.princeton.edu/algs4/checklists/kdtree.html) 的常见问题，x 节点碰到相等的往右，那 y 节点就往上，然后二者都相等就啥也不干。模仿课程代码，借助私有的方法来递归地插入，每次改变节点的划分方向，问题不大。

contains() 方法注意相同值一样地处理，x 节点相同 x 值找右边这样就可以。接着是 draw() 方法，[Checklist](http://coursera.cs.princeton.edu/algs4/checklists/kdtree.html) 里为我们提供了参数设置：

>**How should I set the size and color of the points and rectangles when drawing?** Use StdDraw.setPenColor(StdDraw.BLACK) and StdDraw.setPenRadius(0.01) before before drawing the points; use StdDraw.setPenColor(StdDraw.RED) or StdDraw.setPenColor(StdDraw.BLUE) and StdDraw.setPenRadius() before drawing the splitting lines.

红色铅垂线和蓝色水平线直接借助矩形数据类型的 draw() 方法来画其实。

然后实现 range() 方法，当前搜索点代表的矩形可以通过一些信息就地构造出来，如果和查询矩形没有相交，那么也就没有继续搜索下去的必要。nearest() 方法需要尽快找到一个比较近的点，这样才有可能剪枝，提高效率，所以倾向于先搜索和查询点一个方向的子树，因为一般来说较近点在一个方向。另外，[Checklist](http://coursera.cs.princeton.edu/algs4/checklists/kdtree.html) 最后提示的改进里提醒说，直接用距离的平方就好，平方根的消耗是可以省去的。这两直接也直接看吧：[KdTree.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part1/pa5-kdtree/KdTree.java)。

## 测试结果

![part1-pa5-result](https://img2018.cnblogs.com/blog/886021/201902/886021-20190201102706920-903459761.png)