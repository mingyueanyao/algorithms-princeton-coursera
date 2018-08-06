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

**KMP** 算法一下子解决了上面两个问题，既不用回退，最多也只要访问 N 次字符，于是我们先来了解下 **DFA**。

### Deterministic Finite State Automaton

确定型有穷（状态）自动机（**DFA**），是一个抽象的字符串查找机器。

- 状态数目是有穷的（包括初始状态和终结状态）。

- 每个状态对每个字符有且仅有一个转移。

- 转移到终结状态则接受这个字符串，即含有我们寻找的子串（模式）。

比如说，现在我们在一个由字母 A, B 和 C 组成的文本中寻找子串：ABABAC，DFA 长这样：

![dfa-graph](https://images2018.cnblogs.com/blog/886021/201808/886021-20180806171418350-437178693.png)

具体实现时用二维数组表示就好：

```txt
           j    0    1    2    3    4    5
                --------------------------
pat.charAt(j)   A    B    A    B    A    C    if in state j reading char c:
           |A   1    1    3    1    5    1        if j is 6 halt and accept
   dfa[][j]|B   0    2    0    4    0    4        else move to state dfa[c][j]
           |C   0    0    0    0    0    6
```

其中 dfa[i][j] 表示状态 j 遇到字符 i 会转移到下一个状态，并不包括终结状态。

现在查找子串就很简单啦，一开始在初始状态，文本流读到哪个字符就往哪条路走，要是走到了终结状态，也就表示找到了子串。像我们这样构造的 DFA，走到状态几，就说明已经匹配了多少个字符其实，所以走到终结状态就表示全部匹配。例子，状态三：

![dfa-state](https://images2018.cnblogs.com/blog/886021/201808/886021-20180806171432655-228466052.png)

### KMP Substring Search: Java Implementation

```java
public int search(String txt) {
    int i, j, N = txt.length();
    for (i = 0, j = 0; i < N && j < M; i++)
        j = dfa[txt.chaAt(i)][j];    // no backup
    if (j == M) return i - M;
    else return N;    // not found
}
```

在二维数组 dfa 的指导下，查找子串的代码很简单，即没有回退，最多也只要 N 次字符访问。于是乎，**关键**就在于如何从要查找的子串构造相应的 dfa[][] 啦。

### Construct DFA

1. **Match Transition**

   匹配时的转移很好办，直接到下一个状态即可。

   ![match-transition](https://images2018.cnblogs.com/blog/886021/201808/886021-20180806171446315-1202770884.png)

2. **Mismatch Transition**

    **关键**在不匹配时该如何转移。

    假设在状态 j 时读到的下一个字符 c 不等于要找的子串的第 j + 1 个字符（pat.charAt(j)，从 0 标号），那么这个时候，我们从文本流中最近读出的 j - 1 个字符即为 pat[1..j - 1] + c，就是暴力算法要重新扫描的部分。

    当前首字母到状态 j 出现了不匹配，按暴力算法该丢弃它从下一个字母再开始，即 pat[1]，再一路重新扫描到 c。所以，现在状态 j 遇到 c 该怎么转移，实际上和字符串 pat[1.. j - 1] + c 在 DFA 中所到状态碰到 c 的转移目标一样才对。于是我们这么计算 dfa[c][j]：**在 DFA 上模拟 pat[1.. j - 1]，然后直接取字符 c 的转移**。

    举个例子来看，如何计算状态 5 碰到字符 A 和 B 的转移：

   ![mismatch-transition](https://images2018.cnblogs.com/blog/886021/201808/886021-20180806171501965-420417384.png)

   这样，乍一看需要 j 步来模拟暴力算法中回退部分字符输入到 DFA，从而找到不匹配时的重启状态（像上图的状态 3）。但其实，我们可以花很小的成本来维护重启状态 X，不匹配时就能一下知道该转移到哪。其实也就是从 pat[1] 开始匹配而已，具体可以看下面代码。

#### Construcing DFA For KMP: Java Implementation

```java
public KMP(String pat) {
    this.pat = pat;
    M = pat.length();
    dfa = new int[R][M];
    dfa[pat.charAt(0)][0] = 1;            // 初始状态只要这一个转移，其它还是自己（0）
    // 构造 dfa[][1..M - 1] 没有终结状态的转移
    for (int X = 0, j = 1; j < M; j++) {
        for (int c = 0; c < R; c++)
            dfa[c][j] = dfa[c][X];        // copy mismatch cases
        // 匹配时，状态和重启状态没关系，以当前状态为主，直接下一状态
        dfa[pat.charAt(j)][j] = j + 1;    // set match case
        // 从 pat[1] 开始匹配
        X = dfa[pat.charAt(j)][X];        // update restart state
    }
}
```

## Boyer-Moore

## Rabin-Karp