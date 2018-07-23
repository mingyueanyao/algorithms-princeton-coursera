# 基数排序

## Strings In Java

### Char Data Type

C 语言中的字符数据类型占一个字节（8 比特），最多只能表示 256 个字符。支持 7 位的标准 ASCII(American Standard Code for Information Interchange，美国标准信息交换编码)，最高位用于奇偶校验。或是拓展的 ASCII，最高位用来确定附加的 128 个特殊的字符。

#### 标准 ASCII 的十六进制转换表

![ASCII-hex](https://images2018.cnblogs.com/blog/886021/201807/886021-20180721161952185-820074282.png)

Java 中的字符数据类型占两个字节，支持 16 位的 Unicode 编码。

### String Data Type

字符串就是一些字符组成的序列，不是原生的数据类型，在 Java 中是不可变（immutable）的对象，实现了一些方便的方法：

![Java-string](https://images2018.cnblogs.com/blog/886021/201807/886021-20180721162521004-905655523.png)

表示字符串的字符数组是不可变的，获取的子字符串其实也保存在这个字符数组里，所以在常数时间内就能完成。但是连接两个字符串的时候，就要新建字符串对象，需要正比于字符串长度的时间。

### StringBuilder Data Type

StringBuilder 的字符序列是可变的（mutable），内部实现用的是可变长的字符数组，所以连接字符串的操作几乎能在常数时间完成。但是相应的，获取子字符串的操作需要正比于字符串长度的时间。

所以在实际的使用中，我们要根据不同的需求，选择合适的对象来表示字符串。

## Key-indexed counting

基于比较的排序至少需要 $NlgN$ 次的比较，先介绍一种不是基于比较的，适用于排序的键是小整数的情况，它也是下面其它字符串排序算法的基础，称作：**键索引计数法**。

设想有个数组 a = {d, a, c, f, f, b, d, b, f, b, e, a} 要排序，知道总共有 6 个不同的字母，要先统计它们出现的频数。

```java
int R = 6;
int N = a.length;
int[] count = new int[R + 1];

for (int i = 0; i < N; i++)
    count[a[i] - 'a' + 1]++;
```

字母 a 的键为 0 （a[i] - 'a'），出现的次数在 count 数组中的索引为键值加一。

![key-index-1](https://images2018.cnblogs.com/blog/886021/201807/886021-20180721174333877-1881107237.png)

遍历一遍 count 数组：

```java
for (int i = 0; i < R; i++)
    count[i + 1] += count[i];
```

现在 count 数组中保存的即对应字母在排好序的数组中开始的索引值，像两个 d 应该放在 a[6] 和 a[7]。

```java
char[] aux = new char[N];
for (int i = 0; i < N; i++)
    aux[count[a[i] - 'a']++] = a[i];
```

辅助数组 aux 借助 count 数组找到了每个 a[i] 的位置。注意 count 数组在这一步中还会改变，每次要加一，下次相同的 a[i] 就会放在下一个位置。所以这个算法也是稳定的（stable），相同元素间的相对顺序不会改变。

![key-index-2](https://images2018.cnblogs.com/blog/886021/201807/886021-20180721174344508-415087789.png)

最后把 aux 数组一个个赋值回原数组，即完成了排序。

键索引计数法排序只需要几个一重循环，不需要比较，只要 R 在 N 的一个常数因子范围内，它就是一个线性时间级别的排序方法。

## LSD Radix Sort

## MSD Radix Sort

## 3-way Radix Quicksort

## Suffix Arrays