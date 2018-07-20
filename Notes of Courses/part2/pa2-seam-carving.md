# 编程作业二

作业链接：[Seam Carving](http://coursera.cs.princeton.edu/algs4/assignments/seam.html) & [Checklist](http://coursera.cs.princeton.edu/algs4/checklists/seam.html)

我的代码：[SeamCarver.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part2/pa2-seam/SeamCarver.java)

## 问题简介

>接缝裁剪（Seam carving），是一个可以针对照片内容做正确缩放的算法（由 Shai Avidan 和 Ariel Shamir 所发表）。概念上，这个算法会找出好几条 seams，而这些 seams 是在照片中最不重要的一连串像素，接着再利用这些 seams，对照片做缩放。如果是要缩小照片，则移除这些 seams，若是放大，则在这些 seams 的位置上，插入一些像素。
>
>这样的技术可以用在 image retargeting，将照片正确且没有扭曲得放在各种大小的屏幕或位置上，比如说，手机、投影幕等等。

摘自[维基百科](https://zh.wikipedia.org/wiki/%E6%8E%A5%E7%B8%AB%E8%A3%81%E5%89%AA)。

举个例子，这是一张需要变窄的照片。

![Broadway_tower_edit](https://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Broadway_tower_edit.jpg/800px-Broadway_tower_edit.jpg)

传统的收缩（左）会改变塔的形状，裁剪（右）会让塔变得不完整。

![800px-Broadway_tower_edit_scale&cropped.png](https://images2018.cnblogs.com/blog/886021/201806/886021-20180623113212758-796750501.png)

Seam Carving 则可以达到下图的效果。

![Broadway_tower_edit_Seam_Carving](https://upload.wikimedia.org/wikipedia/commons/thumb/e/ed/Broadway_tower_edit_Seam_Carving.png/800px-Broadway_tower_edit_Seam_Carving.png)

为了评价像素点的重要性，我们需要个函数计算能量，作业这里使用双梯度（dual-gradient）能量函数。图片里颜色变化区域的像素能量高，像塔和天空间的边界区域，seam carving 就会避免移除这些区域的像素。具体的计算方法和一些约定，参见：[Seam Carving](http://coursera.cs.princeton.edu/algs4/assignments/seam.html)，不提。

## 任务摘要

>**The SeamCarver API.** Your task is to implement the following mutable data type:
>```java
>public class SeamCarver {
>    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
>    public Picture picture()                          // current picture
>    public int width()                            // width of current picture
>    public int height()                           // height of current picture
>    public double energy(int x, int y)               // energy of pixel at column x and row y
>    public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
>    public int[] findVerticalSeam()                 // sequence of indices for vertical seam
>    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
>    public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
>}
>```

摘自 [Seam Carving](http://coursera.cs.princeton.edu/algs4/assignments/seam.html)，这里略去很多细节。

**PS:**

原本注释较长会跑到下一行，看着很难受，而 Github 默认会有横向滚动条，于是为实现这个滚动条折腾了好久，一度跑偏，未果。最终是直接查看博客页面的审查元素，在博客园设置里的”页面定制 CSS 代码“添加一行代码 `pre {white-space: pre;}` 即可。。。

## 问题分析

按 [Checklist](http://coursera.cs.princeton.edu/algs4/checklists/seam.html) 里建议的编程步骤，先实现构造器，以及 picture()，width() 和 height() 这些简单的方法，了解下 [Picture.java](https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Picture.html) 就好。接着建议我们实现能量函数，有 [Checklist](http://coursera.cs.princeton.edu/algs4/checklists/seam.html) 里的提示和测试程序，也没有什么大问题，不用创建 Color 对象的优化也可以有。

到了计算 seams 的方法，[Checklist](http://coursera.cs.princeton.edu/algs4/checklists/seam.html) 提示说不要显式地用图片创建图，然后再去跑最短路径算法，而是直接在像素点上进行操作。想了下，没什么头绪，去题目讨论页面寻找灵感。这里的图片像素点和下一行相邻的三个像素点相连，可以看做无环的带权有向图（权重在点上），于是我们按拓扑排序的顺序来放松各个像素点就可以算出 seams。拓扑排序不用特地去算，直接一行一行从左到右遍历下来就好，这里这就是一种拓扑顺序其实。

模仿课程里的实现，来两个辅助数组 distTo[][] 和 edgeTo[][]，findVerticalSeams() 不久也本地测试成功。到了实现 findHorizontal() 方法，[Checklist](http://coursera.cs.princeton.edu/algs4/checklists/seam.html) 叫我们把图片转置（transpose）一下，就是第一行变第一列，第二行变第二列这样，然后直接用 findVerticalSeams() 找，跑完再把图片转回来。转置的实现没有想到啥好办法，new 一个新的长宽相反的 Picture 对象，再一个个像素点用 Picture.setRGB()。这边的行列和常见的有点不一样，老是报错说数组越界，但最后还是成功跑出来了。反正先跑起来，交上去性能不好的话，再来优化吧。

删除 seams 也没有想到什么好方法，同样地 new 一个长或宽少一个像素点的 Picture 对象，再用 Picture.setRGB() 配置。实现了删除垂直的 seams，水平的一样是将图片转置后跑删除垂直 seams 的方法，然后再把图片转置回来。

本地测试成功，虽然 [Checklist](http://coursera.cs.princeton.edu/algs4/checklists/seam.html) 里还有些优化看不懂，但也先交上去看看，不行也能有针对性地进行优化。幸运的是第一次提交就有 99 分，稍作修改就拿到了满分，那还有些优化就不管啦，哈哈哈。

## 测试结果

![part2-pa2-result](https://images2018.cnblogs.com/blog/886021/201806/886021-20180627175436352-444363395.png)