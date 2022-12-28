package osp.leobert.android.reporter.diagram.core

import javax.annotation.processing.ProcessingEnvironment

/**
 * Classname: DrawerContext </p>
 * Description: uml类图解析和绘制过程中的上下文 </p>
 * Created by leobert on 2022/12/28.
 */
class DrawerContext(val env: ProcessingEnvironment?, val umlElementCache: MutableSet<UmlElement>) {
}