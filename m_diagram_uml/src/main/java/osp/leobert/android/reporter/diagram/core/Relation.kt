package osp.leobert.android.reporter.diagram.core

/**
 * <p><b>Package:</b> osp.leobert.android.reporter.diagram.core </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Relation </p>
 * Created by leobert on 2021/9/18.
 */
enum class Relation(val type: Int) {
    //@startuml
    //ClassA <-- ClassB:关联
    //ClassA <.. ClassB : 依赖
    //ClassA o-- ClassB:聚集
    //ClassA <|-- ClassB:泛化
    //ClassA <|.. ClassB:实现
    //@enduml

    Stub(1) {
        override fun <T> format(first: T, second: T, nameGetter: (T) -> String): String? {
            return null
        }
    },

    //泛化（Generalization）,
    Generalization(2) {
        override fun <T> format(first: T, second: T, nameGetter: (T) -> String): String {
            return "${nameGetter(second)} <|-- ${nameGetter(first)}"
        }
    },

    // 实现（Realization），
    Realization(3) {
        override fun <T> format(first: T, second: T, nameGetter: (T) -> String): String {
            return "${nameGetter(second)} <|.. ${nameGetter(first)}"
        }
    },

    // 依赖(Dependency)
    Dependency(4) {
        override fun <T> format(first: T, second: T, nameGetter: (T) -> String): String {
            return "${nameGetter(first)} ..> ${nameGetter(second)}"
        }
    },

    // 关联（Association)，
    Association(5) {
        override fun <T> format(first: T, second: T, nameGetter: (T) -> String): String {
            return "${nameGetter(first)} --> ${nameGetter(second)}"
        }
    },

    // 聚合（Aggregation），
    Aggregation(6) {
        override fun <T> format(first: T, second: T, nameGetter: (T) -> String): String {
            return "${nameGetter(first)} --o ${nameGetter(second)}"
        }
    },

    // 组合(Composition)，
    Composition(7) {
        override fun <T> format(first: T, second: T, nameGetter: (T) -> String): String {
            //先使用关联的
            return "${nameGetter(first)} --> ${nameGetter(second)}"
        }
    };


    abstract fun <T> format(first: T, second: T, nameGetter: (T) -> String): String?


    companion object {
        val enum = arrayListOf(
            Generalization, Realization, Dependency, Association, Aggregation, Composition, Stub
        )

        fun of(type: Int): Relation? {
            return enum.find { it.type == type }
        }
    }
}
