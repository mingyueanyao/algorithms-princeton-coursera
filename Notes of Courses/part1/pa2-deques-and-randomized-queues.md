# 编程作业一

作业链接：[specification](http://coursera.cs.princeton.edu/algs4/assignments/queues.html) & [Checklist](http://coursera.cs.princeton.edu/algs4/checklists/queues.html)

我的代码：

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

详细的要求参见：[specification](http://coursera.cs.princeton.edu/algs4/assignments/queues.html)。

## 问题分析

## 测试结果
