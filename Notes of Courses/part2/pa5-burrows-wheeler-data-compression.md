# 编程作业五

作业链接：[Burrows-Wheeler Data Compression](http://coursera.cs.princeton.edu/algs4/assignments/burrows.html) & [Checklist](http://coursera.cs.princeton.edu/algs4/checklists/burrows.html)

我的代码：TODO

## 问题简介

Burrows-Wheeler 数据压缩算法包括三个部分：Burrows-Wheeler transform，Move-to-front encoding 和 Huffman compression，前面两个部分把文本转换成易于用哈夫曼压缩的形式，或者说更适合，然后展开时再逆着变回原来的文本。哈夫曼压缩部分可以直接调用课程写好的，作业要求我们实现前面两个部分。

### Move-to-front

这是一种对字符串编码方式，输入字符串，输出编码。做法不难说明：先初始个有序字母表序列，然后开始读入字符，输出其在字母表序列中的位置，并将字母表中的该字符移动到首位，再继续读下一个字符。例子：

```txt
move-to-front    in   out
-------------    ---  ---
 A B C D E F      C    2
 C A B D E F      A    1
 A C B D E F      A    0
 A C B D E F      A    0
 A C B D E F      B    2
 B A C D E F      C    2
 C B A D E F      C    0
 C B A D E F      C    0
 C B A D E F      A    2
 A C B D E F      C    1
 C A B D E F      C    0
 C A B D E F      F    5
 F C A B D E  
```

输入字符串为 CAAABCCCACCF，则初始有序字母表序列为 ABCDEF。读入字符 C，输出位置索引 2，字母表序列更新为 CABDEF；读入 A，输出位置索引 1，序列更新为 ACBDEF ... 解码的时候类似，辅助序列初始为 ABCDEF，编码 2 输出 C，更新序列为 CABDEF；编码 1 输出 A，更新序列为 ACBDEF；编码 0 输出 A ...

不难发现，要是输入的字符串中存在很多相邻相同字符，那输出的编码就会有很多小整数，像 0，1 和 2。因为每次都会把读入的字符移到首位，这个字符老是出现，那它的位置也就总是很靠前。编码中小整数出现频率很高，这样的编码就很适合再用哈夫曼算法来压缩。而 Burrows-Wheeler 就是为 Move-to-front 准备这种字符串的，它可以把文本进行一定的转换，让一些相同的字符彼此相邻。

### Burrows–Wheeler transform

> Burrows-Wheeler 变换只改变字符串中字符的顺序而并不改变其字符，如果原字符串有几个出现多次的子串，那么转换过的字符串上就会有一些连续重复的字符。

摘自维基百科：[链接](https://zh.wikipedia.org/zh-hans/Burrows-Wheeler%E5%8F%98%E6%8D%A2)。

这个关键的转换需要用到一个基础的数据结构：循环后缀数组（circular suffix array），姑且这么写吧。例子：

```txt
 i     Original Suffixes          Sorted Suffixes       t    index[i]
--    -----------------------     -----------------------    --------
 0    A B R A C A D A B R A !     ! A B R A C A D A B R A    11
 1    B R A C A D A B R A ! A     A ! A B R A C A D A B R    10
 2    R A C A D A B R A ! A B     A B R A ! A B R A C A D    7
*3    A C A D A B R A ! A B R     A B R A C A D A B R A !   *0
 4    C A D A B R A ! A B R A     A C A D A B R A ! A B R    3
 5    A D A B R A ! A B R A C     A D A B R A ! A B R A C    5
 6    D A B R A ! A B R A C A     B R A ! A B R A C A D A    8
 7    A B R A ! A B R A C A D     B R A C A D A B R A ! A    1
 8    B R A ! A B R A C A D A     C A D A B R A ! A B R A    4
 9    R A ! A B R A C A D A B     D A B R A ! A B R A C A    6
10    A ! A B R A C A D A B R     R A ! A B R A C A D A B    9
11    ! A B R A C A D A B R A     R A C A D A B R A ! A B    2
```

原始字符串是 ABRACADABRA!，循环后缀数组就是每次循环左移得到的 12 个字符串，然后我们再对这 12 个字符串排序，最终输出的变换字符串即排序后的最后一列 t[]。此外，为了后续的还原，输出变换字符串前还要输出原始字符串在排序后的行号 3。

Burrows-Wheeler 变换的目的是把字符串中一些相同字符放在一起，这是一种比较适于压缩的形式，那选排序后的第一列不是更合适吗我想。后来觉得可能是因为考虑到还原的问题，你对 t[] 排下序也就得到了第一列，只有第一列大概不好还原。那不要最后一列，其它列也行吗，排序后多少也会比原字符串的重复字符多些。实际上，变换选最后一列是因为：

>It relies on the following intuition: if you see the letters hen in English text, then most of the time the letter preceding it is t or w. If you could somehow group all such preceding letters together (mostly t’s and some w’s), then you would have an easy opportunity for data compression.

也就是维基百科中说的有相同子串，变换后才会有重复字符。排序后 hen 开头的都排在一起，前缀也就是最后一列啦，很大几率是重复的 t 或 w。

现在来说还原的问题，怎么从原字符串排序后的行号和变换字符串得到原字符串。

```txt
 i      Sorted Suffixes     t      next[i]
--    -----------------------      -------
 0    ! ? ? ? ? ? ? ? ? ? ? A        3
 1    A ? ? ? ? ? ? ? ? ? ? R        0
 2    A ? ? ? ? ? ? ? ? ? ? D        6
*3    A ? ? ? ? ? ? ? ? ? ? !        7
 4    A ? ? ? ? ? ? ? ? ? ? R        8
 5    A ? ? ? ? ? ? ? ? ? ? C        9
 6    B ? ? ? ? ? ? ? ? ? ? A       10
 7    B ? ? ? ? ? ? ? ? ? ? A       11
 8    C ? ? ? ? ? ? ? ? ? ? A        5
 9    D ? ? ? ? ? ? ? ? ? ? A        2
10    R ? ? ? ? ? ? ? ? ? ? B        1
11    R ? ? ? ? ? ? ? ? ? ? B        4
```

我们对 t[] 进行排序，也就得到了第一列数据，加上原字符串排序后的行号 3，马上知道原字符串第一个字符是 A。然后查看 next[3] 是 7，说明第二个字符是第七行的 B，就这样一个字符一个字符的还原。于是乎，关键就是这个 next 数组要怎么生成啦。

next[i] 表示循环后缀数组第 i 个字符串的下一个字符串在排序后的行号，对于在原字符串中只出现一次的字符来说，next 值很好算：

```txt
 i      Sorted Suffixes     t      next[i]
--    -----------------------      -------
 0    ! ? ? ? ? ? ? ? ? ? ? A        3
 1    A ? ? ? ? ? ? ? ? ? ? R
 2    A ? ? ? ? ? ? ? ? ? ? D
*3    A ? ? ? ? ? ? ? ? ? ? !
 4    A ? ? ? ? ? ? ? ? ? ? R
 5    A ? ? ? ? ? ? ? ? ? ? C
 6    B ? ? ? ? ? ? ? ? ? ? A
 7    B ? ? ? ? ? ? ? ? ? ? A
 8    C ? ? ? ? ? ? ? ? ? ? A        5
 9    D ? ? ? ? ? ? ? ? ? ? A        2
10    R ? ? ? ? ? ? ? ? ? ? B
11    R ? ? ? ? ? ? ? ? ? ? B
```

考虑字符 C，它只出现了一次。根据循环后缀数组定义，在以 C 开头的字符串下面是以 C 结尾的字符串，也就是排序后第五行字符串，所以 next[8] = 5。类似的，next[0] = 3，next[9] = 2。

不止出现一次的字符，没法一下一次对应，但实际上也不难区分。像上面字符 R，出现了两次，以 R 结尾的字符串有两个，要怎么和 next[10]，next[11] 对应起来呢。正确的答案是：next[10] = 1，next[11] = 4。因为第 10 和第 11 行都以 R 开头，而这个又是排好序的，那这两字符串的后 11 位肯定是前者小于后者，用这两 11 位开头的字符串自然也是前者排在前面。

## 任务摘要

>### MoveToFront.java
>
>Name your program MoveToFront.java and organize it using the following API:
>
>```java
>public class MoveToFront {
>    // apply move-to-front encoding, reading from standard input and writing to standard output
>    public static void encode()
>
>    // apply move-to-front decoding, reading from standard input and writing to standard output
>    public static void decode()
>
>    // if args[0] is '-', apply move-to-front encoding
>    // if args[0] is '+', apply move-to-front decoding
>    public static void main(String[] args)
>}
>```
>
>### CircularSuffixArray.java
>
>Your job is to implement the following circular suffix array API, which provides the client access to the index[] values:
>```java
>public class CircularSuffixArray {
>   public CircularSuffixArray(String s)    // circular suffix array of s
>   public int length()                     // length of s
>   public int index(int i)                 // returns index of ith sorted suffix
>   public static void main(String[] args)  // unit testing (required)
>}
>```
>
>### BurrowsWheeler.java
>
>Name your program BurrowsWheeler.java and organize it using the following API:
>```java
>public class BurrowsWheeler {
>    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
>    public static void transform()
>
>    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
>    public static void inverseTransform()
>
>    // if args[0] is '-', apply Burrows-Wheeler transform
>    // if args[0] is '+', apply Burrows-Wheeler inverse transform
>    public static void main(String[] args)
>}
>```

## 问题分析

## 测试结果