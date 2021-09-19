package osp.leobert.android.reporter.diagram.core

/**
 * <p><b>Package:</b> osp.leobert.android.reporter.diagram.core </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Relation </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/9/18.
 */
sealed class Relation(val type: Int) {

    companion object {
        val enum = arrayListOf(
            Stub, Generalization, Realization, Dependency, Association, Aggregation, Composition
        )

        fun of(type: Int): Relation {
            return requireNotNull(enum.find { it.type == type })
        }
    }

    object Stub : Relation(0) {

    }

    //泛化（Generalization）,
    object Generalization : Relation(1) {

    }

    // 实现（Realization），
    object Realization : Relation(2) {

    }

    // 依赖(Dependency)
    object Dependency : Relation(3) {

    }

    // 关联（Association)，
    object Association : Relation(4) {

    }

    // 聚合（Aggregation），
    object Aggregation : Relation(5) {

    }

    // 组合(Composition)，
    object Composition : Relation(6) {

    }
}
