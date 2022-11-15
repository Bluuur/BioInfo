# 生信原理编程作业 1

> 生信 2001 张子栋 2020317210101
>
> [Bluuur/BioInfo: Bioinformatics code. (github.com)](https://github.com/Bluuur/BioInfo)
> 可在此地址查看代码修改记录

`AlignmentConstructor` 比对构造器类, 用于创建比对构造器对象, 属性包含两条待比对序列, 打分规则, 得分矩阵等.

`ScoreRule` 打分规则类, 用于创建打分规则对象, 构造方法可以从键盘接收打分规则.

`Main` 主程序, `main` 方法中完成比对.

`Ref` 参考代码, [Needleman-Wunsch全局序列比对算法_如果我是海的博客-CSDN博客_needleman wunsch](https://blog.csdn.net/qq_51287641/article/details/125402548), 参考回溯算法.