# ReportPrinter

根据注解生成报告，可以用于标识版本变更、Code Review、在代码中标注重要信息、 **生成类图** 等

完全自定义注解以及注解的处理，框架部分仅负责：

* 注解的发现
* 将注解和被注解的信息给到自定义部分处理
* 将自定义处理的结果生成文档

本项目有两种使用方式：

* 使用核心注解处理器+已经存在的功能扩展，例如UML类图扩展
* 使用核心注解处理器+自定义功能扩展

## 可以快速获得的支持

* [核心注解处理器及其插件扩展的设计思路](https://blog.csdn.net/a774057695/article/details/106603455)
* [类图工具设计介绍和使用](https://juejin.cn/post/7025235961801867278)
* [使用中的一些问题](https://github.com/leobert-lan/ReportPrinter/issues?q=is%3Aissue+is%3Aclosed) ，检索相关内容
* [实在没辙了可以这样](https://github.com/leobert-lan/ReportPrinter/issues/new)

[项目内容说明](./Modules.md)

[演示如何使用的相关内容说明](./samples/README.md)

## 可直接获取的功能

核心注解处理器：
<img src="https://img.shields.io/static/v1?label=MavenCentray&message=report-anno-compiler"/>
[<img src="https://img.shields.io/maven-central/v/io.github.leobert-lan/report-anno-compiler.svg?label=latest%20release"/>](https://search.maven.org/search?q=g:io.github.leobert-lan%20And%20a:report-anno-compiler)

类图生成器，注解&注解处理器插件
<img src="https://img.shields.io/static/v1?label=MavenCentray&message=class-diagram-reporter"/>
[<img src="https://img.shields.io/maven-central/v/io.github.leobert-lan/class-diagram-reporter.svg?label=latest%20release"/>](https://search.maven.org/search?q=g:io.github.leobert-lan%20And%20a:class-diagram-reporter)

review文档生成器，"TODO/BUG/DONE"，注解&注解处理器插件
<img src="https://img.shields.io/static/v1?label=MavenCentray&message=reporter-review"/>
[<img src="https://img.shields.io/maven-central/v/io.github.leobert-lan/reporter-review.svg?label=latest%20release"/>](https://search.maven.org/search?q=g:io.github.leobert-lan%20And%20a:reporter-review)


## 如何自定义功能扩展并使用：

[参见此文档](./custom.md) 

## License
<details>
<summray>
MIT License Copyright (c) 2018 leobert-lan, see details
</summary>

```
MIT License
 
Copyright (c) 2018 leobert-lan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
</details>




