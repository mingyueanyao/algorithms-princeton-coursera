# 编程作业一

作业链接：[specification](http://coursera.cs.princeton.edu/algs4/assignments/collinear.html) & [Checklist](http://coursera.cs.princeton.edu/algs4/checklists/collinear.html)

我的代码：[BruteCollinearPoints.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part1/pa3-collinear/BruteCollinearPoints.java) & [FastCollinearPoints.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part1/pa2-queues/FastCollinearPoints.java) & [Point.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part1/pa3-collinear/Point.java)

## 问题简介

计算机视觉涉及分析视觉图像中的模式并重建产生它们的现实世界对象。该过程通常分为两个阶段：特征检测和模式识别。特征检测涉及选择图像的重要特征；模式识别涉及发现特征中的模式。我们将研究一个涉及点和线段的特别简单的模式识别问题。这种模式识别出现在许多其他应用中，例如统计数据分析。

>**The problem.** Given a set of n distinct points in the plane, find every(maximal) line segment that connects a subset of 4 or more of the points.
>
>![lines2](https://img2018.cnblogs.com/blog/886021/201812/886021-20181228151759274-1666771878.png)

给定一些点，要求找到所有至少包含四个点的线段。

## 任务摘要

>**Point data type.** Create an immutable data type Point that represents a point in the plane by implementing the following API:
>
>```java
>public class Point implements Comparable<Point> {
>   public Point(int x, int y)                         // constructs the point (x, y)
>
>   public   void draw()                               // draws this point
>   public   void drawTo(Point that)                   // draws the line segment from this point to that point
>   public String toString()                           // string representation
>
>   public               int compareTo(Point that)     // compare two points by y-coordinates, breaking ties by x-coordinates
>   public            double slopeTo(Point that)       // the slope between this point and that point
>   public Comparator<Point> slopeOrder()              // compare two points by slopes they make with this point
>}
>```

实现表示点的数据类型，要求完成后三个方法，详细参见：[specification](http://coursera.cs.princeton.edu/algs4/assignments/collinear.html)。

>**Brute force.** Write a program *BruteCollinearPoints.java* that examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments. To check whether the 4 points p, q, r, and s are collinear, check whether the three slopes between p and q, between p and r, and between p and s are all equal.
>
>```java
>public class BruteCollinearPoints {
   >public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
   >public           int numberOfSegments()        // the number of line segments
   >public LineSegment[] segments()                // the line segments
>}
>```

除了暴力检测每四个点是否共线（$n^{4}$），还有更快的做法，对点 P：

- 把 P 当做原点。

- 其它点按和点 P 的斜率（slope）排序。

- 检查是否有三个点（或者更多）有同样的斜率。如果有，那这些点和点 P 就构成目标线段。

![lines1](https://img2018.cnblogs.com/blog/886021/201812/886021-20181228154849673-1432428956.png)

对其它的点，重复上述过程，就能找到所有目标线段，因为排序把斜率一样的点聚在一起。而这算法更快是因为瓶颈操作是排序，排序是 nlgn，最后是 $n^{2}lgn$，也比 $n^{4}$ 好得多。

>Write a program *FastCollinearPoints.java* that implements this algorithm.
>
>```java
>public class FastCollinearPoints {
   >public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
   >public           int numberOfSegments()        // the number of line segments
   >public LineSegment[] segments()                // the line segments
>}
>```

## 问题分析

## 测试结果