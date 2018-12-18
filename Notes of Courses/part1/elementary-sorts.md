# 初级排序

## rules of the game

排序是很常见的需求，把数字从小到大排，把字符串按字典序排等等，我们的目标是能对任何类型的数据进行排序，这可以通过回调（callback）实现：

![callback](https://img2018.cnblogs.com/blog/886021/201812/886021-20181213172714862-2006745868.png)

Java 用接口实现回调，具体来说是可比较接口（Comparable），里面有个方法 compareTo()，大于小于等于分别返回 +1，-1 和 0，sort() 即调用这个方法来比较数据大小，不同类型数据的 compareTo() 可能不同，但 sort() 是不管这些的，如下示例。

```java
// Comparable interface(built in to Java)
public interface Comparable<Item> {
    public int compareTo(Item that) {
        // ...
        return -1;    // less
        // ...
        return +1;    // greater
        // ...
        return 0;    // equal
    }
}

// sort implementation
public static void sort(Comparable[] a) {
    int N = a.length;
    for (int i = 0; i < N; i++)
        for (int j = i; j > 0; j--)
            if (a[j].compareTo(a[j - 1]) < 0)
                exch(a, j, j - 1);
            else break;
}
```

上面 sort() 不依赖于数组 a 的实际数据类型，只要 a 实现了可比较接口，就可以对其排序，实现了对不同类型数据排序的目标。Java 内置的类型，像 Integer，Double，String，Data 等，都实现了可比较接口，用户也可以很容易地对自己的数据类型实现可比较接口，举例：

```java
public class Date implements Comparable<Date> {
    private final int month, day, year;

    public Date(int m, int d, int y) {
        month = m;
        day = d;
        year = y;
    }

    public int compareTo(Data that) {
        if (this.year < that.year) return -1;
        if (this.year > that.year) return +1;
        if (this.month < that.month) return -1;
        if (this.month > that.month) return +1;
        if (this.day < that.day) return -1;
        if (this.day > that.day) return +1;
        returnn 0;
    }
}
```

另外，compareTo() 方法实现的应该是全序关系（total order），满足：

- 非对称性（antisymmerty）：若 v $\leqslant$ w 且 w $\leqslant$ v，那么 v = w。
- 传递性（transitivity）：若 v $\leqslant$ w 且 w $\leqslant$ x，那么 v $\leqslant$ x。
- 完全性（totality）：要么 v $\leqslant$ w 要么 w $\leqslant$ v 要么 v = w。

石头剪刀布就不符合传递性，课程还举了个反例：double 的 $\leqslant$。

> violates totality: (Double.NaN <= Double.NaN) is false

最后，课程说把对待排序数组的操作封装成下面两个方法：

```java
// Is item v less than w?
private static boolean less(Comparable v, Compareble w) {
    return v.compareTo(w) < 0;
}

// Swap item in array a[] at index i with the one at index j
private static void exch(Compareble[] a, int i, int j) {
    Comparable swap = a[i];
    a[i] = a[j];
    a[j] = swap;
}
```

交换和比较大小是很常用的操作，而且可以保证能像下面这样检查数组是否有序：

```java
private static boolean isSorted(Comparable[] a) {
    for (int i = 1; i < a.length; i++)
        if (less(a[i], a[i - 1])) return false;
    return true;
}
```

因为你只对数组元素进行交换和比大小操作，只有最后有序了才能通过上面的测试。要是还允许其它操作，比如全部赋值成 1，那也可以通过上面的测试，但显然不算完成排序。总之大概就是封装抽象出这两操作，会比较方便，不管是排序还是测试。

## selection sort

选择排序的思想很简单，第一次选整个数组最小的放在第一位，然后再从第二个位置开始选最小的放在第二位，一直到最后，数组也就排好序了。

例图：

![insertion](https://img2018.cnblogs.com/blog/886021/201812/886021-20181217153636205-1956689716.png)

第一次选择 a[0] ~ a[10] 中最小的 A，和第一个位置交换；第二次选 a[1] ~ a[10] 中最小的 E，和第二个位置交换；第三次选 a[2] ~ a[10] 中最小的 E，和第三个位置交换 ... 。

代码：

```java
public class Selection {
    public static void sort(Comparable[] a) {
        for (int i = 0; i < N; i++) {
            int min = i;
            for (int j = i + 1; j < N; j++)
                if (less(a[j], a[min]))
                    min = j;
            exch(a, i, min);
        }
    }

    private static boolean less(Comparable v, Comparable w) {...}
    private static void exch(Comparable[] a, int i, int j) {...}
}
```

选择排序对一个大小为 N 的数组排序，需要 (N - 1) + (N - 2) + ... + 1 + 0 ~ $N^{2}/2$ 次比较和 N 次交换，不管输入的数组是否有序，都需要这么多次的比较，但它交换的次数是最少的，每个元素交换一次就到了最终的位置。

## insertion sort

## shellsort

## shuffling

## convex hull