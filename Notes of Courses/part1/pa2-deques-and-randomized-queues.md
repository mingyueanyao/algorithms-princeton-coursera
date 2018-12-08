# 编程作业一

作业链接：[specification](http://coursera.cs.princeton.edu/algs4/assignments/queues.html) & [Checklist](http://coursera.cs.princeton.edu/algs4/checklists/queues.html)

我的代码：[Deque.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part1/pa2-queues/Deque.java) & [RandomizedQueue.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part1/pa2-queues/RandomizedQueue.java) & [Permutation.java](https://github.com/mingyueanyao/algorithms-princeton-coursera/blob/master/Codes%20of%20Programming%20Assignments/part1/pa2-queues/Permutation.java)

## 问题简介

>Write a generic data type for a deque and a randomized queue. The goal of this assignment is to implement elementary data structures using arrays and linked lists, and to introduce you to generics and iterators.

## 任务摘要

>**Dequeue**. A double-ended queue or deque (pronounced “deck”) is a generalization of a stack and a queue that supports adding and removing items from either the front or the back of the data structure. Create a generic data type Deque that implements the following API:
>
>```java
>public class Deque<Item> implements Iterable<Item> {
>   public Deque()                           // construct an empty deque
>   public boolean isEmpty()                 // is the deque empty?
>   public int size()                        // return the number of items on the deque
>   public void addFirst(Item item)          // add the item to the front
>   public void addLast(Item item)           // add the item to the end
>   public Item removeFirst()                // remove and return the item from the front
>   public Item removeLast()                 // remove and return the item from the end
>   public Iterator<Item> iterator()         // return an iterator over items in order from front to end
>   public static void main(String[] args)   // unit testing (optional)
>}
>```
>**Randomized queue**. A randomized queue is similar to a stack or queue, except that the item removed is chosen uniformly at random from items in the data structure. Create a generic data type RandomizedQueue that implements the following API:
>
>```java
>public class RandomizedQueue<Item> implements Iterable<Item> {
>   public RandomizedQueue()                 // construct an empty randomized queue
>   public boolean isEmpty()                 // is the randomized queue empty?
>   public int size()                        // return the number of items on the randomized queue
>   public void enqueue(Item item)           // add the item
>   public Item dequeue()                    // remove and return a random item
>   public Item sample()                     // return a random item (but do not remove it)
>   public Iterator<Item> iterator()         // return an independent iterator over items in random order
>   public static void main(String[] args)   // unit testing (optional)
>}
>```
>Iterator.  Each iterator must return the items in uniformly random order. The order of two or more iterators to the same randomized queue must be mutually independent; each iterator must maintain its own random order.
>**Client**. Write a client program Permutation.java that takes an integer k as a command-line argument; reads in a sequence of strings from standard input using StdIn.readString(); and prints exactly k of them, uniformly at random. Print each item from the sequence at most once.

详细的要求参见：[specification](http://coursera.cs.princeton.edu/algs4/assignments/queues.html)。

## 问题分析

第二次作业比较简单，主要想让我们用数组和链表实现基础的数据结构，介绍 Java 的泛型和迭代器，还可以参考 [ResizingArrayStack.java](https://algs4.cs.princeton.edu/13stacks/ResizingArrayStack.java.html) 和 [LinkedStack.java](https://algs4.cs.princeton.edu/13stacks/LinkedStack.java.html)。

任务一要求实现双端队列，即在队列的首尾都可以进行插入和删除操作。因为性能要求里要每次操作都保证在常数时间内完成，所以选择链表实现。确定了实现方式，不行再照着课程样例，建议加的哨兵节点没什么问题。

任务二要求实现随机队列，随机出队，每个位置出去的概率一样，迭代器返回的顺序也要是随机的。[Checklist](http://coursera.cs.princeton.edu/algs4/checklists/queues.html) 里的提示降低了很多难度：

>**What is meant by uniformly at random?** If there are n items in the randomized queue, then you should choose each one with probability 1/n, up to the randomness of StdRandom.uniform(), independent of past decisions. You can generate a pseudo-random integer between 0 and n−1 using StdRandom.uniform(n) from [StdRandom](https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/StdRandom.html).
>
>**Given an array, how can I rearrange the entries in random order?** Use StdRandom.shuffle()—it implements the Knuth shuffle discussed in lecture and runs in linear time. Note that depending on your implementation, you may not need to call this method.

所以这个随机队列用数组实现就比较方便，随机数组下标好操作，也符合作业的目的，一个用链表，一个用数组。而且，任务二性能要求空间部分，也保证我们用变长数组才能拿到满分，总之，课程设计是很棒的。然后，任务二也没什么问题。

任务三的测试程序，要求从文件里读入 N 个字符串，然后随机输出 K 个。直接把 N 个字符串压入任务二的随机队列，然后输出 K 个就行。但是，还有个额外的挑战：

>For an extra challenge, use only one Deque or RandomizedQueue object of maximum size at most k.

所以不能直接压入 N 个，队列里最多只能放 K 个字符串，这大概是这次作业最难的部分了。当初是没有想出来，找到这篇[博客](http://www.cnblogs.com/lidunot-fear/p/8025840.html)才拿到了额外的分数。

## 测试结果

![part1-pa2](https://img2018.cnblogs.com/blog/886021/201812/886021-20181208173957245-88322132.png)

填坑，继续小号提交原来通过的代码。