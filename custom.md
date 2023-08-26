## 如何自定义功能扩展并使用：


### 自定义注解和处理逻辑

参考DemoReporterExt：

声明依赖并将编译版本指定到 Java8

```
dependencies {
    implementation "io.github.leobert-lan:ReportNotation:1.1.5"

    annotationProcessor 'com.google.auto.service:auto-service:1.0-rc4'
    implementation 'com.google.auto.service:auto-service:1.0-rc4'
}

sourceCompatibility = "1.8"
targetCompatibility = "1.8"
```

因为使用了SPI机制做服务发现，所以用到了AutoService, 但也可以自行添加 Meta中的SPI注册

定义注解：

```
public @interface Demo {
    String foo();
}
```

定义注解处理逻辑：

```
@AutoService(ReporterExtension.class)
public class FooReporter implements ReporterExtension {
    @Override
    public Set<String> applicableAnnotations() {
        return Collections.singleton(Demo.class.getName());
    }

    @Override
    public Result generateReport(Map<String, List<Model>> previousData) {
        if (previousData == null)
            return null;

        List<Model> demoModels = previousData.get(Demo.class.getName());
        if (demoModels == null || demoModels.isEmpty())
            return Result.newBuilder().handled(false).build();
        StringBuilder stringBuilder = new StringBuilder();

        for (Model model : demoModels) {
            Demo demo = model.getElement().getAnnotation(Demo.class);
            String foo = demo.foo();
            stringBuilder.append(model.getElementKind().toString())
                    .append(" :")
                    .append(model.getName())
                    .append("\r\n")
                    .append("foo:")
                    .append(foo)
                    .append("\r\n");
        }

        return Result.newBuilder()
                .handled(true)
                .reportFileNamePrefix("Demo")
                .fileExt("md")
                .reportContent(stringBuilder.toString())
                .build();
    }
}
```

实现：Set<String> applicableAnnotations() 返回需要处理的注解全类名。

实现：Result generateReport(Map<String, List<Model>> previousData) 进行数据处理。

* 框架已经将感兴趣的注解 *（注解处理器能够发现的注解）*，全部收集并分组返回，利用注解全类名可取出这些信息；
* 利用Model中的元信息获得注解信息：Demo demo = model.getElement().getAnnotation(Demo.class);
* 根据注解信息和 Model中的元信息组织文档内容
* 返回结果 Result

### 使用它们

以Kotlin_sample 为例：

```
apply plugin: 'com.android.library'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'
apply plugin: 'kotlin-kapt'

//注意此处配置
kapt {
    arguments {
        arg("module", "ktsample") //将作为生成文件的前缀
        arg("mode", "mode_file") //不要修改它
        arg("active_reporter", "on")//开启或关闭
    }
}

dependencies {

    api project(':utils_reporter')
    implementation project(':DemoReporterExt')

    kapt "io.github.leobert-lan:report-anno-compiler:1.1.3"
    kapt project(":utils_reporter")
    kapt project(":DemoReporterExt")
}
```

需要配置注解处理器，并声明使用

声明 DemoReporterExt 的依赖以使用注解
声明 注解处理器：

* kapt "io.github.leobert-lan:report-anno-compiler:1.1.5"
* kapt project(":DemoReporterExt")

配置：

* module：文件前缀，一般和Module对应即可
* mode：输出形式， 取值范围：
    * mode_file 以实际内容输出
    * ~~mode_class 目前不建议使用，是一个尚未成熟的设计~~
* active_reporter on|off ,是否输出
* ~~reporter_writer 同样不建议使用，配合 "model为mode_class" 时输出到特定目录作为源码编译的~~

以此为例：

```
@Demo(foo = "foo of demo notated at clz")
class KtFoo {
    @Demo(foo = "foo of demo notated at function")
    private fun foo(bar: Any) {
    }

    @Demo(foo = "foo of demo notated at field")
    private val i = 0
}
```    

将得到： {module_root}/Reports/{配置中的module值}DemoReport.md

内容为：

```
CLASS :osp.leobert.android.kotlin_sample.KtFoo
foo:foo of demo notated at clz

METHOD :osp.leobert.android.kotlin_sample.KtFoo#foo(java.lang.Object)
foo:foo of demo notated at function

METHOD :osp.leobert.android.kotlin_sample.KtFoo#getI$annotations()
foo:foo of demo notated at field
```

当然，这还只是一个非常简单的例子，使用者可以在注解处理时、将内容编辑为MarkDown或者Html，甚至利用更加复杂的注解完成有趣的事情（例如：基于plantuml，生成流程图、类图）

现在，我们已经拥有类图插件了！    