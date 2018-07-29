# 单词查找树

和以字符串为键的排序算法类似，以字符串为键的符号表也有更加高效的实现，可以避免检测整个键。于是乎，先贴下我们要实现的 **API:**

![api](https://images2018.cnblogs.com/blog/886021/201807/886021-20180729171733111-1117062077.png)

## R-way Tries

单词查找树（Tires），来自 retrieval，为和‘tree’区分，读作‘try’。

- 节点里存储字符而不是键。
- 每个节点有 R（字母表大小，像拓展 ASCII 是 256）个孩子，分别表示可能的下一个字符。
- 为了方便，我们先不画出空链接。

于是乎，来个例图：
  
![tries](https://images2018.cnblogs.com/blog/886021/201807/886021-20180729171713775-1213701699.png)

### Tries: search

查找自然只有匹配和没有这两种情况：

1. **Search hit:** 查找时的最后一个字符所在节点有非空值，例如：

    ![search-hit](https://images2018.cnblogs.com/blog/886021/201807/886021-20180729171751305-1276872904.png)

2. **Search miss:** 查找时的遇到空链接或是最后一个字符所在节点没有值，如：

    ![search-miss](https://images2018.cnblogs.com/blog/886021/201807/886021-20180729171805785-988481090.png)

### Tries: insertion

往单词查找树里插入新字符串时，顺着和每个字符匹配的链接走下去，然后：

1. 遇到空链接时，创建新的节点。
2. 更新字符串最后一个字符所在节点的值。

    ![insertion](https://images2018.cnblogs.com/blog/886021/201807/886021-20180729171825065-576457960.png)

### Tries: Java implementaion

```java
public class TriesST<Value> {
    private static final int R = 256;    // extended ASCII
    private Node root = new Node();

    private static class Node {
    private Object value;
    private Node[] next = new Node[R];
    }

    public void put(String key, Value val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        // 即用下个字符本身作为数组索引
        x.next[c] = put(x,next[c], key, val, d + 1);
        return x;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x = null) return null;
        return (Value) x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d + 1);
    }
}
```

### Tries: performance

- **Search hit:** 需要检查字符串的每个字符。
- **Search miss:** 有可能只要首个字符就能判断没有，典型案例中只需检查一些字符（亚线性级别）。
- **Space:** 每个叶节点都有 R 个空链接。但如果很多短字符串共享相同前缀，那空间为亚线性级别是可能的。

所以总的来说，可以很快匹配到字符串，判断没有甚至更快，但是浪费空间。

### Tries: deletion

删除单词查找树中的某个字符串时，首先要找到它，然后把最后一个节点的值置空，再递归删除没有非空链接的空值节点。例子：

![deletion](https://images2018.cnblogs.com/blog/886021/201807/886021-20180729171843615-47757674.png)

于是，对于 R 比较小的情况，可以考虑使用 R-way trie。但是，当 R 比较大时，它所需要的空间就太大了，16 位的 Unicode 就会是 65536-way trie。

## Ternary Search Tries

## Character-based Operations