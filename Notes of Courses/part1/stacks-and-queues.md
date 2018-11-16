# 栈和队列

大型填坑现场，以上。

栈和队列是很基础的数据结构，前者后进先出，后者先进先出，如下图：

![stack-queue-demo](https://img2018.cnblogs.com/blog/886021/201811/886021-20181116204940450-615637641.png)

下面开始将客户端和具体实现分开，这样有两个好处：一是客户端不知道实现的细节，但同时也会有很多不同实现来选择，二是实现方面也不知道客户端需求的细节，但同时很多客户端可以也重用一样的实现。接口就像把二者连接起来的桥梁。

![client-implementation-interface](https://img2018.cnblogs.com/blog/886021/201811/886021-20181116205002716-232225823.png)

## stacks

栈的操作主要就出栈入栈，热身来一个放字符串的栈。

### stack API

![stack-api](https://img2018.cnblogs.com/blog/886021/201811/886021-20181116210418375-277921632.png)

### 栈的测试代码

```java
public static void main(String[] args) {
    StackOfStrings stack = new StackOfString();
    while (!StdIn.isEmpty()) {
        String s = StdIn.readString();
        if (s.equals("-")) StdOut.print(stack.pop());
        else stack.push(s);
    }
}
```

### stack linked-list

用链表来实现栈，出栈入栈的示意图和代码：

![stack-linklist-pushpop](https://img2018.cnblogs.com/blog/886021/201811/886021-20181116205041418-49129113.png)

```java
public class LinkedStackOfStrings {
    private Node first = null;

    private class Node {
        String item;
        Node next;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public void push(String item) {
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
    }

    public String pop() {
        String item = first.item;
        first = first.next;
        return item;
    }
}
```

链表实现的栈每个操作都只要常数的时间，一直都很快，相对的会需要较多额外的空间。

![stack-linklist-space](https://img2018.cnblogs.com/blog/886021/201811/886021-20181116211703790-750458906.png)

上面算的是每个栈节点需要的空间，不包括其中的字符串，它们在客户端上。

### stack array

很常见的，能用链表实现，一般还有用数组实现的版本。

![stack-array](https://img2018.cnblogs.com/blog/886021/201811/886021-20181116212744127-859476472.png)

详细代码在下节的变长数组里，这里说下数组游离（loitering）问题。

![loitering](https://img2018.cnblogs.com/blog/886021/201811/886021-20181116205132760-1360831098.png)

出栈时把不要的元素置空，垃圾回收机制才能回收不用的内存。

## resizing arrays

## queues

## generics

## iterators

## applications