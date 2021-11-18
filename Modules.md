# Module 说明

演示 `使用方式` & `生成结果` 的内容均置于 samples 目录下，详见：[说明](./samples/README.md)

## ReportNotation

早先的版本中，包含一些注解 & APT插件的必要基础内容，现在仅包含 APT插件的必要基础内容（SPI接口&Bean类）。

## report-anno-compiler

现在仅包含 APT插件的核心逻辑

---

ReportPrinter采用了SPI机制设计，即 ServiceProviderInterface,我们将 **实现了SPI** 的module称为 `功能插件`

report-anno-compiler 的工作是：

* 收集所有的功能插件，即SPI发现
* 得到各个功能插件所关心的注解
* 扫描这些注解，进行一定的预处理
* 将预处理的结果分派给各个功能插件
* 收集各个功能插件的处理结果
* 将结果输出为文件

这样设计的目的在于：关注点分离 *即：SOC* ，功能插件不再需要关注：插件功能的开关、注解对象的预处理、文件的生成等。

需要新功能时，新建功能插件，完成核心处理逻辑即可

---

## DemoReporterExt

一个 `功能插件` 演示module，示范如何自定义一个插件

## m_diagram_uml

"类图生成" 功能插件

## m_review_reporter

code review时做标记的功能插件

## utils_reporter

为助手类生成方法摘要的功能插件