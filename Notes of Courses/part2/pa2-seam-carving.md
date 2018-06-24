# 编程作业二

作业链接：[Seam Carving](http://coursera.cs.princeton.edu/algs4/assignments/seam.html) & [Checklist](http://coursera.cs.princeton.edu/algs4/checklists/seam.html)

我的代码：

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
>    public     int width()                            // width of current picture
>    public     int height()                           // height of current picture
>    public  double energy(int x, int y)               // energy of pixel at column x and row y
>    public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
>    public   int[] findVerticalSeam()                 // sequence of indices for vertical seam
>    public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
>    public    void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
>}
>```

摘自 [Seam Carving](http://coursera.cs.princeton.edu/algs4/assignments/seam.html)，这里略去很多细节。

**PS:**

原本注释较长会跑到下一行，看着很难受，而 Github 默认会有横向滚动条，于是为实现这个滚动条折腾了好久，一度跑偏，未果。最终是直接查看 Github 页面的审查元素，在博客园设置里的”页面定制 CSS 代码“添加一行代码 `pre {white-space: pre;}`即可。。。

## 问题分析

## 测试结果
