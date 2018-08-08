# 编程作业四

作业链接：[Boggle](http://coursera.cs.princeton.edu/algs4/assignments/boggle.html) & [Checklist](http://coursera.cs.princeton.edu/algs4/checklists/boggle.html)

我的代码：[BoggleSolver.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part2/pa4-boggle/BoggleSolver.java)

## 问题简介

Boggle 是一个文字游戏，有 16 个每面都有字母的骰子，开始随机将它们放在 4 * 4 的板子上，如图：

![boggle](https://images2018.cnblogs.com/blog/886021/201808/886021-20180808164145026-1389415701.jpg)

图片来自：[wiki](https://en.wikipedia.org/wiki/Boggle)。

游戏双方就在上面找有效的单词，单词越长分数越高，有效的单词具体指：

- 有效单词相邻字符的骰子也相邻，上下左右，对角线也行。
- 有效单词不能重复使用某个骰子。
- 有效单词的长度至少为 3。
- 有效单词要能在字典中找到，专有名词不算。

举例：

![valid-word-sample](https://images2018.cnblogs.com/blog/886021/201808/886021-20180808164205342-1121403689.png)

左边的单词 PINES 是有效的，右边的 DATES 是无效的（显然不相邻）。

不同长度的单词计分如下：

| word length | points |
|:-----------:|:------:|
| 0 - 2 | 0 |
| 3 - 4 | 1 |
| 5 | 2 |
| 6 | 3 |
| 7 | 5 |
| 8+ | 11 |

最后，因为英语单词中字母 Q 后面几乎总是会跟着 U，所以直接在骰子上写了 QU，计分的时候按两个字母算。

## 任务摘要

>**Your task.** Your challenge is to write a Boggle solver that finds all valid words in a given Boggle board, using a given dictionary. Implement an immutable data type BoggleSolver with the following API:
>
>```java
>public class BoggleSolver
>{
>    // Initializes the data structure using the given array of strings as the dictionary.
>    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
>    public BoggleSolver(String[] dictionary)
>
>    // Returns the set of all valid words in the given Boggle board, as an Iterable.
>    public Iterable<String> getAllValidWords(BoggleBoard board)
>
>    // Returns the score of the given word if it is in the dictionary, zero otherwise.
>    // (You can assume the word contains only the uppercase letters A through Z.)
>    public int scoreOf(String word)
>}

## 问题分析

## 测试结果