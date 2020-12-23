[TOC]





# Marelles_期末大程报告

## 3180102943 常博宇

### 背景描述

Marelles，中文名九子棋、直棋，是一款双人对战的益智棋盘类游戏，最早可追溯至罗马帝国时期。因其棋盘、棋子布置简单、可玩性高，是一款比较流行的游戏。本次期末大程以这款游戏为核心元素，结合socket编程、GUI、数据库编程等知识，实现了双人在线对战游戏。

### 游戏规则

游戏棋盘由24个格点组成，棋盘如下图所示。两名玩家分别执黑、白，最初各有九个棋子，称为”士兵“。玩家通过移动”士兵“形成”军队“（即连续的三个棋子连成一条水平或竖直的直线）时，可以拿掉对方的一个棋子。当某一方玩家只剩两名“士兵”，或者玩家剩余的士兵不能移动时，则失败。



![img](Marelles_期末大程报告.assets/220px-Nine_Men's_Morris_board_with_coordinates,_modified.svg.png)

游戏共分为三个阶段：

1. 棋子放置阶段：棋盘起初为空。两名玩家决定谁先开始，轮流在空闲的格点处放置棋子。在放置过程中，如果有一方形成了连续的水平或竖直的三个棋子（称为“三连”），则可以从对方已放置的棋子中移除一个到游戏外。移除时，必须先移除没有形成一行或一列的棋子。双方轮流放置直到都放置过了九枚棋子。
2. 棋子移动阶段：玩家交替进行棋子移动。棋子移动时只能将自己一方的一枚棋子移动到相邻的空闲的格点处。如果移动棋子使得自己一方形成了一个“三连”，则该玩家需要立即选择对方玩家任意一枚棋子移出游戏。当某方玩家只剩三枚棋子时，进入第三阶段。
3. 棋子跳跃阶段：当某方玩家只剩三枚棋子时，他可以不受“每次移动只能选择相邻空闲位置”的限制，而可以将自己的任意一枚棋子移动到任意空闲格点。当某方玩家只剩两枚棋子时，该玩家失败。

### 实验整体思路



### 设计要点、创新点

* 

### 实验详细思路



### 运行实例



### 编译环境

* IDE

  ```
  IntelliJ IDEA 2020.2.3 (Ultimate Edition)
  Build #IU-202.7660.26, built on October 6, 2020
  Licensed to https://zhile.io
  You have a perpetual fallback license for this version
  Subscription is active until July 8, 2089
  Runtime version: 11.0.8+10-b944.34 amd64
  VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
  Windows 10 10.0
  GC: ParNew, ConcurrentMarkSweep
  Memory: 1965M
  Cores: 8
  Registry: ide.suppress.double.click.handler=true
  Non-Bundled Plugins: Statistic, org.jetbrains.kotlin, com.intellij.plugins.html.instantEditing, com.jetbrains.php, Pythonid
  ```

* JAVA version 

  ```
  java version "1.8.0_271"
  Java(TM) SE Runtime Environment (build 1.8.0_271-b09)
  Java HotSpot(TM) 64-Bit Server VM (build 25.271-b09, mixed mode)
  ```

* OS

  ```
  Microsoft Window 10 家庭中文版
  基于x64的电脑
  ```

  

### 主要文件及目录说明

```python
./
|-resources/ #服务器资源目录，【需要拷贝到D:/下】
|          |-html/
|          |     |-img/
|          |     |    |-logo.jpg
|          |     |-noimg.html
|          |     |-test.html
|          |-txt/
|          |    |-test.txt
|          |-test.zup
|          |-test.jzup
|          |-downloadTest
|-socket/ #工程目录，此目录需直接在IDEA打开
|       |-.idea/*
|       |-out/
|       |    |-production/*
|       |    |-artifacts/
|       |               |-socket_jar/
|       |                           |-socket.jar #每次进行Build jar时，产生的jar将保存在此处
|       |-src/
|            |-META-INF/*
|            |-ClientConnection.java #程序源代码ClientConnection类，用于处理请求并进行响应
|            |-LogPrinter.java #程序源代码LogPrinter类，用于输出调试信息
|            |-MySocket.java #程序源代码Mysocket类 （main），用于建立socket
|            |-RequestParser.java #程序源代码RequestParser类，用于解析请求
|            |-MyClassLoader.java #程序源代码MyClassLoader类，用于加载指定路径的class
|            |-socket.iml
|-Java_Web服务器实验报告.md #本README文件(pdf若出现错位请阅读md)
|-Java_Web服务器实验报告.pdf #本README文件
```

### 运行说明

1. 

### 实验心得
