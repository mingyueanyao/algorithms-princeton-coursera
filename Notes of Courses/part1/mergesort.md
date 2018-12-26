# 归并排序

归并排序和快速排序是两个经典的排序算法，是计算机的基础设施的重要组成部分，完整科学地理解它们的特性有助于我们将其用于实际的系统排序，快排也是二十世纪科学和工程领域的十大算法之一。

## mergesort

归并排序基本思想：把数组分成两半，递归地排好每一半，合并有序的两半。另外，冯诺依曼被公认为“归并排序之父”。

![mergesort-overview](https://img2018.cnblogs.com/blog/886021/201812/886021-20181225174650843-989622633.png)

合并操作并不复杂，需要先拷贝到一个辅助数组，然后子数组从头开始对应比较，看先放哪个。

代码：

```java
private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
    assert isSorted(a, lo, mid);        // precondition: a[lo..mid] sorted
    assert isSorted(a, mid + 1, hi);    // precondition: a[mid + 1..hi] sorted

    for (int k = lo; k <= hi; k++)
        aux[k] = a[k];

    int i = lo, j = mid + 1;
    for (int k = lo; k <= hi; k++) {
        if (i > mid) a[k] = aux[j++];
        else if (j > hi) a[k] = aux[i++];
        else if (less(aux[j], aux[i])) a[k] = aux[j++];
        else a[k] = aux[i++];
    }

    assert isSorted(a, lo, hi);    // postcondition: a[lo..hi] sorted
}
```

上面用到了 Java 中的 assert，会在后面的布尔值为假时抛出异常，isSorted() 在 [Elementary Sorts](https://www.cnblogs.com/mingyueanyao/p/10115335.html) 第一部分的最后有。这有助于发现程序的错误，而且你可以禁用它，不会在产品中产生额外的代码。

![assert](https://img2018.cnblogs.com/blog/886021/201812/886021-20181225174705604-426303676.png)

再加上递归，归并排序的完整代码如下：

```java
public class Merge {
    private static void merge(...) {
        /* as before */
    }

    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    public static void sort(Comparable[] a) {
        aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1);
    }
}
```

注意不要递归地创建辅助数组，这会让代码的效率变差，上面只在递归外创建一次辅助数组。

归并排序的轨迹示例：

![trace](https://img2018.cnblogs.com/blog/886021/201812/886021-20181225174808766-1241367421.png)

排好最前两，再排后面两，并起来排好前四，类似排好后面四个，并起来排好前八，一样排好后八，并起来排好整个数组。这样的轨迹图，有助于我们理解递归发生了什么。

**对于长度为 N 的任意数组**，**归并排序至多需要 NlgN 次的比较和 6NlgN 次的数组访问**。

![recurrences](https://img2018.cnblogs.com/blog/886021/201812/886021-20181226155543129-1732019249.png)

合并的时候最多需要比较 N 次，访问数组包括复制到辅助数组的 2N，比较完放回去的 2N，以及最多 N 次比较时的 2N 次访问。

当 N 为 2 的幂时，比较次数最多为 NlgN 比较好证明，课程甚至列了三：

![pf1](https://img2018.cnblogs.com/blog/886021/201812/886021-20181226155558222-1413451482.png)

![pf2](https://img2018.cnblogs.com/blog/886021/201812/886021-20181226155610095-1049882220.png)

![pf3](https://img2018.cnblogs.com/blog/886021/201812/886021-20181226155620640-189599053.png)

对于一般的 N，最后的准确值会更复杂些，反正还是 NlgN 级别的。总之，归并排序的时间复杂度是 NlgN 级别，但是因为有辅助数组，需要的空间和 N 成正比。也有不用辅助数组改成就地排序的，但太复杂不实用。另外，我们还可以对上面的代码做点改进。

### 对小规模数组用插入排序

递归会使小规模问题中方法的调用过于频繁，而且插入排序很可能在小数组上比归并更快。

```java
private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
    if (hi <= lo + CUTOFF - 1) {
        Insertion.sort(a, lo, hi);
        return;
    }
    int mid = lo + (hi - lo) / 2;
    sort(a, aux, lo, mid);
    sort(a, aux, mid + 1, hi);
    merge(a, aux, lo, mid, hi);
}
```

CUTOFF 可设为 7 或 15 这些比较小的数。

### 测试数组是否有序

如果左边最大小于右边最小，就可以跳过合并，对于子数组有序的数组有帮助，而且只要一行。

```java
private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
    ...
    if (!less(a[mid + 1], a[mid])) return;
    merge(a, aux, lo, mid, hi);
}
```

### 不要复制到辅助数组

每次合并直接合到辅助数组上，下次再交换角色让原数组当辅助，省去了原来复制数据的时间。

![save-copy](https://img2018.cnblogs.com/blog/886021/201812/886021-20181226155642692-14459425.png)

[MergeX.java](https://algs4.cs.princeton.edu/22mergesort/MergeX.java.html) 实现了上述改进版归并排序，里面的 sort():

```java
public static void sort(Comparable[] a) {
        Comparable[] aux = a.clone();
        sort(aux, a, 0, a.length-1);  
        assert isSorted(a);
    }
```

输入的待排序数组在第二个参数。

## bottom-up mergesort

## sorting complexity

## comparators

## stability