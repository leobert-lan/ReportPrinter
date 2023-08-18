package osp.leobert.uml.io

import osp.leobert.android.reporter.diagram.notation.ClassDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.android.reporter.diagram.notation.IgnoreExclude
import osp.leobert.android.reporter.diagram.notation.NameSpace
import osp.leobert.android.reporter.diagram.notation.Visible

/**
 * Classname: Graphs </p>
 * Description: TODO </p>
 * Created by leobert on 2023/4/20.
 */
interface Graphs {
    @ClassDiagram(
        qualifier = "InputStream",
        fieldVisible = [],//[Visible.Public],
        methodVisible = [],//[Visible.Public],
        ignoreExclude = IgnoreExclude(fullNamePatterns = ["java\\.io\\.\\w"])
    )
    annotation class Input

    @ClassDiagram(
        qualifier = "Okio",
        fieldVisible = [],
        methodVisible = [],
    )
    annotation class Okio

    @NameSpace("sink",0xFFe5332c)
//    @ClassDiagram(
//        qualifier = "Okio-sink",
//        fieldVisible = [],
//        methodVisible = [],
//    ) //i don't want to define a new ClassDiagram, just use the namespaced notation
    annotation class NsSink

    @NameSpace("source",0xff3c082c)
//    @ClassDiagram(
//        qualifier = "Okio-source",
//        fieldVisible = [],
//        methodVisible = [],
//    )
    annotation class NsSource

//    @ClassDiagram("RecordDB")
//    annotation class RecordDB
//
//    @ClassDiagram(
//        "Parser",
//        ignore = ["^com.google.common.reflect"],
////        ignoreExclude = IgnoreExclude(exactFullNames = ["com.google.common.reflect.TypeToken"])
//    )
//    annotation class Parser
}