# 初级排序

## rules of the game

排序是很常见的需求，把数字从小到大排，把字符串按字典序排等等，我们的目标是能对对任何类型的数据进行排序，这可以通过回调（callback）实现：

![callback](https://img2018.cnblogs.com/blog/886021/201812/886021-20181213172714862-2006745868.png)

Java 用接口实现回调，具体来说是可比较接口（Comparable），里面有个方法 compareTo()，大于小于等于分别返回 +1，-1 和 0，sort() 即调用这个方法来比较数据大小，如下示例。

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

另外，compareTo() 方法实现的应该是全序关系，满足：

- 非对称性（antisymmerty）：若 v $\leqslant$ w 且 w $\leqslant$ v，那么 v = w。
- 传递性（transitivity）：若 v $\leqslant$ w 且 w $\leqslant$ x，那么 v $\leqslant$ x。
- 完全性（totality）：要么 v $\leqslant$ w 要么 w $\leqslant$ v 要么 v = w。

石头剪刀布就不符合传递性，课程还举了个反例：double 的 $\leqslant$。

> violates totality: (Double.NaN <= Double.NaN) is false

最后，课程说把对待排序数组的操作封装成两个方法：

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

## selection sort

## insertion sort

## shellsort

## shuffling

## convex hull