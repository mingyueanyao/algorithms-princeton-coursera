# 快排

快排是另一个经典的排序算法，在实际中也被广泛地应用。

## quicksort

快排的基本思想：

- 混洗（shuffle）打乱待排数组。
  
- 这样划分（partition）数组：
  - 元素 a[j] 在排好的位置上。
  - j 左边元素都不大于 a[j]。
  - j 右边元素都不小于 a[j]。

- 递归地排好 j 的左边和右边。

![quicksort-overview](https://img2018.cnblogs.com/blog/886021/201812/886021-20181229112337309-1407301953.png)

混洗是为了保证算法性能，下面分析性能时再说。随机打乱了数组，这里划分时直接选第一个元素为基准，再来 i，j 两个指针分别从首尾遍历，交换不符合的元素，最后再把基准值放到指针相遇的位置。

划分示意：

![partition](https://img2018.cnblogs.com/blog/886021/201812/886021-20181229112358701-218419254.png)

代码：

```java
private static int partition(Comparable[] a, int lo, int hi) {
    int i = lo, j = hi+1;
    while (true) {
        // find item on left to swap
        while (less(a[++i], a[lo]))
            if (i == hi) break;

        // find item on right to swap
        while (less(a[lo], a[--j]))
            if (j == lo) break;

        if (i >= j) break;    // check if pointers cross
        exch(a, i, j);        // swap
    }

    exch(a, lo, j);    // swap with partitioning item
    return j;          // return index of item now known to be in place
}
```

划分例子：

![partition-trace](https://img2018.cnblogs.com/blog/886021/201812/886021-20181229112555373-807921662.png)

再递归地划分左右，得到基础的快排代码：

```java
public class Quick {
    private static int partition(Comparable[] a, int lo, int hi) {
        /* as before */
    }

    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) return;
        int j = partition(a, lo, hi);
        sort(a, lo, j-1);
        sort(a, j+1, hi);
    }
}
```

排序过程轨迹示例：

![trace](https://img2018.cnblogs.com/blog/886021/201812/886021-20181229112611859-1947934885.png)

第一次划分选了 K，然后排左边，左边排好了再排 K 右边，小的数组也是这么个过程。

### 性能分析

最好情况下，每次划分选的元素都刚好把数组分成两半，这就有点像归并排序了，需要的比较次数是 NlgN 级别。最坏情况下，每次划分选的元素都是最小的，那需要的比较次数就是 $N^{2}$ 级的。平均情况下，也就是排序之前随机打乱无重复元素的数组，快排需要 ~2NlnN 次比较，证明：

$C_{N}$ = (N + 1) + $(\frac{C_{0} + C_{N-1}}{N})$ + $(\frac{C_{1} + C_{N-2}}{N})$ + ... + $(\frac{C_{N-1} + C_{0}}{N})$

N + 1 是每次划分时需要的比较次数，基准值和其它 N - 1 个比较，i 和 j 相遇多比了两次，我觉得大概是这样理解。后面是递归排序的比较次数，而且划分基准选取每个位置的可能性是一样的，所以都带 $\frac{1}{N}$。等式两边乘 N，整理各项有：

$NC_{N}$ = N(N + 1) + 2($C_{0}$ + $C_{1}$ + ... + $C_{N-1}$)

上式减去 N-1 时的相同等式可得：

$NC_{N}$ - (N - 1)$C_{N-1}$ = 2N + $2C_{N-1}$

整理后再两边同时除以 N(N + 1) 得：

$\frac{C_{N}}{N + 1}$ = $\frac{C_{N-1}}{N}$ + $\frac{2}{N + 1}$

然后再看这张 PPT：

![pf](https://img2018.cnblogs.com/blog/886021/201812/886021-20181229121747678-263544494.png)

所以，平均情况下，快排需要的比较次数比归并排序多 39%，但实际上快排一般会比归并排序快，因为快排移动数据的次数更少。排序之前随机混洗，就是为了尽可能避免最坏情况，达到平均情况的表现，保证性能。

另外，快排是就地（in-place）排序，不需要额外空间。还有，快排不是稳定排序，交换可能改变相等元素的相对位置。

![not-stable](https://img2018.cnblogs.com/blog/886021/201812/886021-20181229155728934-806634897.png)

### 算法改进

#### 小数组用插入排序

和归并排序一样，快排也是递归的，处理小问题时的方法调用过于频繁，而且插入排序在小数组上很可能会更快。

```java
private static void sort(Comparable[] a, int lo, int hi) {
    if (hi <= lo + CUTOFF - 1) {
        Insertion.sort(a, lo, hi);
        return;
    }
    int j = partition(a, lo, hi);
    sort(a, lo, j-1);
    sort(a, j+1, hi);
}
```

#### 三抽样取中

人们发现将取样大小设为 3 并用大小居中的元素划分的效果最好。

```java
private static void sort(Comparable[] a, int lo, int hi) {
    if (hi <= lo) return;

    int m = medianOf3(a, lo, lo + (hi - lo)/2, hi);
    swap(a, lo, m);

    int j = partition(a, lo, hi);
    sort(a, lo, j-1);
    sort(a, j+1, hi);
}
```

## selection

## duplicate keys

## system sorts