# 优先队列

集合性质的数据类型离不开插入删除这两操作，主要区别就在于删除的时候删哪个，像栈删最晚插入的，队列删最早插入的，随机队列就随便删，而优先队列删除当前集合里最大（或最小）的元素。优先队列有很多应用，举几个见过的像：数据压缩的哈夫曼编码、图搜索中的 Dijkstra 算法和 Prim 算法、人工智能里的 A* 算法等，优先队列是这些算法的重要组成部分。

## API and elementary implementations

![priority-queue-API](https://img2018.cnblogs.com/blog/886021/201901/886021-20190103174052707-2131246550.png)

先来个简单的 API，应用例子是从 N 个输入里找到前 M 个大的元素，其中 N 的数目很大。因为 N 很大，没有足够的空间来存储，所以不能全部排序后输出前 M 个。得用优先队列，保留 M 个，插入超过 M 就删除最小的。

像栈和队列那样的初级优先队列实现，内部存储又可以分为有序和无序两种，有序的插入操作是 N 级别，删除是常数级别，而无序相反，所以在时间上也不尽人意。下节介绍的二叉堆实现都是 NlgM 级别，这里先蛮贴一个初级实现。

```java
public class UnorderedMaxPQ<Key extends Comparable<Key>> {
    private Key[] pq;    // pq[i] = ith element on pq
    private int N;       // number of elements on pq

    public UnorderedMaxPQ(int capacity) {
        pq = (Key[]) new Comparable[capacity];
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public void insert(Key x) {
        pq[N++] = x;
    }

    public Key delMax() {
        int max = 0;
        for (int i = 0; i < N; i++)
            if (less(max, i)) max = i;
        exch(max, N-1);
        return pq[--N];
    }
}
```

## binary heaps

二叉树就是节点最多有两个孩子的树，其中完全二叉树除了底层可能右边空外都是满的，二叉堆是用数组表示的符合堆顺序（heap-ordered）的完全二叉树，例：

![heap-representations](https://img2018.cnblogs.com/blog/886021/201901/886021-20190103174028571-2139035422.png)

堆顺序大概就是指父母节点的值不比孩子小，然后一层一层从左到右放数组里。另外，数组从下标 1 开始，这样就可以很方便的在堆里上下移动：下标为 k 的节点的父母下标为 k/2，孩子是 2k 和 2k+1，完全不需要其它显式的连接。

二叉堆实现的优先队列，内部存储用的是数组，插入和删除都和数组最后一个元素打交道，比较方便：插入先直接插入末尾，删除也是把 a[1] 先和末尾交换再直接删除末尾。于是乎，我们需要上浮（swim）和下沉（sink）操作，来恢复插入删除中被破坏的堆顺序。

### swim

```java
private void swim(int k) {
    while (k > 1 && less(k/2, k)) {
        exch(k, k/2);
        k = k/2;
    }
}
```

当孩子节点的值比父母大时，为了维护堆顺序，这个值该上浮，于是和父母的值交换，继续这一过程浮到合适的位置。所以，把元素插入到末尾之后，就给它来个上浮，安排到合适位置。

```java
public void insert(Key x) {
    pq[++N] = xl
    swim(N);
}
```

### sink

```java
private void sink(int k) {
    while (2*k <= N) {
        int j = 2*k;
        if (j < N && less(j, j+1)) j++;
        if (!less(k, j)) break;
        exch(k, j);
        k = j;
    }
}
```

当父母节点的值比孩子小时，这个节点该下沉来维护堆顺序，选两个孩子（如果有两）中较大的交换值，继续这一过程直到沉到合适的位置。于是，当删除元素时，把 a[1] 和末尾交换后删除末尾，现在的 a[1] 用下沉来找到该有的位置。

```java
public Key delMax() {
    Key max = pq[1];
    exch(1, N--);
    sink(1);
    pq[N+1] = null;
    return max;
}
```

因为有 N 个点的完全二叉树的高度位 lgN 取下整（高度只有在节点数为 2 的幂时才会加一），所以上浮和下沉的复杂度都是 lgN 级别。

图例：

![heap-operations](https://img2018.cnblogs.com/blog/886021/201901/886021-20190103174122174-555358349.png)

代码：

```java
public class MaxPQ<Key extends Comparable<Key>> {
    private Key[] pq;
    private int N;

    public MaxPQ(int capacity) {
        pq = (key[]) new Comparable[capacity+1];
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public void insert(Key key)
    public Key delMax() {
        /* see previous code */
    }

    private void swim(int k)
    private void sink(int k) {
        /* see previous code */
    }

    private boolean less(int i, int j) {
        return pq[i].compareTo(pq[j]) < 0;
    }
    private void exch(int i, int j) {
        Key t = pq[i];
        pq[i] = pq[j];
        pq[j] = t;
    }
}
```

此外，还有一些可以考虑的，如把键设为不可变的（immutable），当它们在优先队列里的时候，客户端程序不能改变它们；内部使用变长数组；改成删除最小元素；支持随机删除和改变优先级（这个后面好像会有）等。

关于不可变多说了点，Java 里用关键字 final 就好，像 String、Integer、Double、Vector 等都是不可变的，而 StringBUilder、Stack、Java array 等是可变的。不可变数据创建之后就不能改变，这有很多好处：方便 debug，利于防范恶意代码，可以放心地作为优先队列或符号表的键等，虽然每个值都要新建，但还是利大于弊。反正，了解下啦。

## heapsort

## event-driven simulation