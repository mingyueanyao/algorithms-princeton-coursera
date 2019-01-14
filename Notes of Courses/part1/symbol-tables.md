# 初级符号表

符号表是键值对的集合，支持给定键查找值的操作，有很多应用：

![symbol-table-application](https://img2018.cnblogs.com/blog/886021/201901/886021-20190114110457267-842028877.png)

## API

![symbol-table-API](https://img2018.cnblogs.com/blog/886021/201901/886021-20190114110514527-284581748.png)

put() 和 get() 是最基础的两个操作，为了保证代码的一致性，简洁性和实用性，先说下具体实现中的几个设计选择。

- **泛型** 我们在考虑方法时不指定在处理的键和值的类型，而是使用泛型。
  
- **重复的键** 每个键只对应一个值（表里没有重复的键），插入键值对的时候如果发现表里已经有这个键，那就更新该键值对的值。这些约定定义了关联数组（associative array）抽象，你可以把符号表想象成一个数组，其中键是索引而值是数组元素。

- **空值** 不允许键对应的值为空（null）。直接原因是 get() 方法在表里没有相应传入的键时返回 null。这样约定，给定键我们还可以看符号表是否给它定义了值，看 get() 方法是否返回 null 就好。另外，方法 delete() 可以通过调用 put() 方法时在第二个参数传 null 来实现。

- **删除** 符号表的删除操作一般有两种策略。一是懒删除（lazy deletion），即上面提到的直接用 put() 更新为 null，然后可能过会儿再移除这样的键。二是即时删除（eage deletion），马上把键从符号表中移除。在我们的符号表实现中不会使用默认方案，即不用 put(key, null)。

- **迭代** key() 返回一个迭代器给客户端遍历所有键。

- **键的等价性** Java 要求为每个对象实现一个 equals() 方法，本身也为标准类型如 Integer，Double 和 String 以及更复杂的类型如 Date，File 和 URL 实现了这个方法，我们以这个方法来确定一个给定的键是否在符号表中。在实际中，对自定义的键需要重写 equals() 方法，这在 part1-pa4 中有提过，可以参考 [Date.java](https://algs4.cs.princeton.edu/12oop/Date.java.html) 和 [Transaction.java](https://algs4.cs.princeton.edu/12oop/Transaction.java.html)，提下原来没讲到的 equals() 应实现一个等价关系，满足:

  - 自反性（Reflexive）：x.equals(x) 返回真。

  - 对称性（Symmetric）：若 x.equals(y) 为真，则 y.equals(x) 也为真。

  - 传递性（Transitive）：若 x.equals(y) 为真且 y.equals(z) 为真，那么 x.equals(z) 为真。

另外，equals() 的参数必须是一个对象，还要满足：

- 一致性（Consistency）：当两个对象都没有没修改时，多次调用 x.equals() 返回相同的值。

- 非空：x.equals(null) 返回 false。

最后，最好使用不可变的数据类型作为键，以此来保证符号表的一致性。

再蛮打一下符号表的测试用例，一个统计字符最近出现的位置，一个输出出现频率最高的字符串：

```java
public static void mian(String[] args) {
    ST<String, Integer> st = new ST<String, Integer>();
    for (int i = 0; !StdIn.isEmpty(); i++) {
        String key = StdIn.readString();
        st.put(key, i);
    }

    for (String s : st.keys()) {
        StdOut.println(s + " " + st.get(s));
    }
}
```

```java
public class FrequencyCounter {
    public static void main(String[] args) {
        int minlen = Integer.parseInt(args[0]);
        ST<String, Integer> st = new ST<String, Integer>();
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            if (word.length() < minlen) continue;    // ignore short strings
            if (!st.contains(word)) st.put(word, 1);
            else st.put(word, st.get(word) + 1);
        }

        String max = "";
        st.put(max, 0);
        for (String word : st.keys()) {
            if (st.get(word) > st.get(max))
                max = word;
        }
        StdOut.println(max + " " + st.get(max));
    }
}
```

## elementary implementations

符号表的一个简单实现是使用链表（无序），每个节点存储一个键值对。get() 方法遍历链表，用 equals() 方法匹配查找的键，成功则返回对应的值，失败则返回 null。put() 方法同样遍历链表，用 equals() 方法看表中是否已存在该键，存在则更新对应的值，不存在则新生成一个节点存在表头。这种方法，我们称为顺序查找（sequential search）。示例图：

![sequential-search](https://img2018.cnblogs.com/blog/886021/201901/886021-20190114110557543-1081193822.png)

无序链表的顺序查找在最坏情况下，查找和插入的时间复杂度都是 N 级别的，平均情况下查找是 N/2 级别，但插入还是 N 级别。我们希望能找到更高效的实现，不管是查找还是插入，于是来了解下有序数组的二分查找。

实现的关键是 rank() 方法，它会返回符号表中比给定键值小的键的数目。对于 get() 来说，rank() 准确地告诉我们去哪找（找不到就不在表中）。对于 put() 来说，rank() 准确地告诉我们去哪更新表中存在的键的值，或是要把新的键值对放在哪。插入新键值对时，我们把较大的键都后移一位来腾出位置，以此来维护有序性。

图例：

![binary-search](https://img2018.cnblogs.com/blog/886021/201901/886021-20190114110609970-701338083.png)

代码：

```java
public Value get(Key key) {
    if (isEmpty()) return null;
    int i = rank(key);
    if (i < N && keys[i].compareTo(key) == 0) return vals[i];
    else return null;
}

private int rank(Key key) {
    int lo = 0, hi = N - 1;
    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        int cmp = key.compareTo(keys[mid]);
        if (cmp < 0) hi = mid - 1;
        else if (cmp > 0) lo = mid + 1;
        else return mid;
    }
    return lo;
}
```

维护数组有序，就可以使用二分查找来大大减少比较的次数，和数组中间比完看在哪边再递归地处理，查找的复杂度最坏情况下也是 lgN 级别的。最坏情况下，插入也是 N 级别，因为大的键都要后移，但平均是 N/2 级别，比原来链表都是 N 高效。

![rank](https://img2018.cnblogs.com/blog/886021/201901/886021-20190114110626281-993360519.png)

## ordered operations

有序数组里的键自然是可比较的，还有一些其它和顺序有关的操作，API 如下：

![ordered-symbol-table-api](https://img2018.cnblogs.com/blog/886021/201901/886021-20190114110651573-664206626.png)

这些操作还是很实用的，像下面的例子：

![ordered-symbol-table-ops](https://img2018.cnblogs.com/blog/886021/201901/886021-20190114110822278-1842393996.png)

借助 rank() 方法，实现也很简洁明了，如：

```java
public Key ceiling(Key key) {
    int i = rank(key);
    return keys[i];
}
```

完整的参见：[BinarySearchST.java](https://algs4.cs.princeton.edu/31elementary/BinarySearchST.java.html)。

最后，再来总结比较一下：

![sequential-vs-binary](https://img2018.cnblogs.com/blog/886021/201901/886021-20190114110837552-123880333.png)

二分查找的插入和删除还是有改进空间的。

## BSTs

## bst ordered operations

## deletion