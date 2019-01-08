# 编程作业四

作业链接：[8 Puzzle](http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html) & [Checklist](http://coursera.cs.princeton.edu/algs4/checklists/8puzzle.html)

我的代码：[Board.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part1/pa4-8puzzle/Board.java) & [Solver.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part1/pa4-8puzzle/Solver.java)

## 问题简介

借助优先队列，实现解决八数码（8puzzle）问题的 A* 算法。

八数码问题就是下面这样，移动方块重排数字，最后使其有序，希望找到最少的移动次数。

```txt
    1  3        1     3        1  2  3        1  2  3        1  2  3
 4  2  5   =>   4  2  5   =>   4     5   =>   4  5      =>   4  5  6
 7  8  6        7  8  6        7  8  6        7  8  6        7  8

 initial        1 left          2 up          5 left          goal
```

A* 算法是一种最优搜索（Best-first search），评价（或启发或优先级，反正就那意思）函数依实际问题而设计，搜索拓展节点时选最优，这里介绍了两个：

- Hamming priority function. 定义为：不在正确位置上的方块数目加上目前移动的次数。直观上来说，在错误位置上的方块数目少，也就越接近目标，而且我们也希望移动次数少。
  
- Manhattan priority function. 定义为：错误方块到正确位置需要的曼哈顿距离（水平加垂直格子数）之和加上目前移动的次数。

举例来说：

```txt
 8  1  3        1  2  3     1  2  3  4  5  6  7  8    1  2  3  4  5  6  7  8
 4     2        4  5  6     ----------------------    ----------------------
 7  6  5        7  8        1  1  0  0  1  1  0  1    1  2  0  0  2  2  0  3

 initial          goal         Hamming = 5 + 0          Manhattan = 10 + 0
```

### TODO

启发式应该满足...

一个重要的优化的是避免往优先队列里放入太多重复的情况，具体来说就是拓展节点时丢弃先前转换来的情况：

```txt
  8  1  3       8  1  3       8  1          8  1  3       8  1  3
  4     2       4  2          4  2  3       4     2       4  2  5
  7  6  5       7  6  5       7  6  5       7  6  5       7  6

predecessor   search node    neighbor       neighbor      neighbor
                                           (disallow)
```

这样做不能保证完全不出现重复的情况，不过也已经好很多了，再用 SET 什么的反而可能会带来更大的开销（[Checklist](http://coursera.cs.princeton.edu/algs4/checklists/8puzzle.html) 如是说）。

第二个优化是把汉明距离和曼哈顿距离存下来，不用需要的时候再重新算一遍。另外，存在没有解的情况，课程说这种情况随便交换两个数字后就是可解的（证明不清楚，感兴趣的或许可以参考：[proof](http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.19.1491)）。所以，为了处理无解的情况，我们还要同步进行另一个 A* 搜索，二者有一对数字相反，后者找到了解，那前者就是无解的。

## 任务摘要

>**Board and Solver data types.** Organize your program by creating an immutable data type Board with the following API:
>
>```java
>public class Board {
    >public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
    >                                       // (where blocks[i][j] = block in row i, column j)
    >public int dimension()                 // board dimension n
    >public int hamming()                   // number of blocks out of place
    >public int manhattan()                 // sum of Manhattan distances between blocks and goal
    >public boolean isGoal()                // is this board the goal board?
    >public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    >public boolean equals(Object y)        // does this board equal y?
    >public Iterable<Board> neighbors()     // all neighboring boards
    >public String toString()               // string representation of this board (in the output format specified below)
    >
    >public static void main(String[] args) // unit tests (not graded)
>}
>```
>
>Also, create an immutable data type Solver with the following API:
>
>```java
>public class Solver {
    >public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    >public boolean isSolvable()            // is the initial board solvable?
    >public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    >public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    >public static void main(String[] args) // solve a slider puzzle (given below)
>}
>```

详细参见：[specification](http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html)。

## 问题分析

## 测试结果