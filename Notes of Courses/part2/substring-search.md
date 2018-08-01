# 查找子字符串

## Introduction

在长度为 N 的文本里寻找长度为 M 的模式（子串），典型情况是 N >> M。

![pattern-text](https://images2018.cnblogs.com/blog/886021/201808/886021-20180801230900919-1725939841.png)

这个应用就很广泛啦，在文本中寻找特定的模式（子串）是很常见的需求。

## Brute Force

我们先来了解一下暴力查找。

![brute-force](https://images2018.cnblogs.com/blog/886021/201808/886021-20180801230913345-679738515.png)

就暴力地两个循环，查找文本的每个位置，最坏情况下需要近似 $MN$ 次字符比较。

![brute-force-worst-case](https://images2018.cnblogs.com/blog/886021/201808/886021-20180801230928182-823845050.png)

稍微贴下代码：

```java
public static int search(String pat, String txt) {
    int M = pat.length();
    int N = txt.length();
    for (int i = 0; i <= N - M; i++) {
        int j;
        for (j = 0; j < M; j++)
            if (txt.charAt(i + j) != pat.charAt(j))
                break;
        if (j == M) return i;    // index in txt where pattern starts
        return N;    // not found
    }
}
```

暴力算法有可能会跑的很慢，而且还存在一个回退（backup）问题：

![brute-force-backup](https://images2018.cnblogs.com/blog/886021/201808/886021-20180801230942284-74790880.png)

这是暴力算法的另一个实现代码（显示回退）：

```java
public static int search(String pat, String txt) {
    int i, N = txt.length();
    int j, M = pat.length();
    for (i = 0, j = 0; i < N && j < M; i++) {
        if (txt.charAt(i) == pat.charAt(j)) j++;
        else {
            i -= j;    // explicit backup
            j = 0;
        }
        if (j == M) return i - M;
        else return N;    // not found
    }
}
```

所以暴力算法并不是总能满足我们的需求，我们希望有线性时间级别的性能保证，希望避免在文本流中回退。

## Knuth-Morris-Pratt

## Boyer-Moore

## Rabin-Karp