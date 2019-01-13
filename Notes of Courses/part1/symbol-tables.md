# 符号表

符号表是键值对的集合，支持给定键查找值的操作，有很多应用：

![symbol-table-application]()

## API

![symbol-table-API]()

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

## ordered operations