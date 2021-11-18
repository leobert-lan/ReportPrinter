package osp.leobert.android.kotlin_sample.diagram

import osp.leobert.android.reporter.diagram.notation.ClassDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram


class BridgePattern {

    @ClassDiagram(qualifier = "BridgePattern")
    annotation class BridgePatternDiagram

    interface MessageImplementor {
        fun send(message: String, toUser: String)
    }

    abstract class AbstractMessage(private val impl: MessageImplementor) {
        open fun sendMessage(message: String, toUser: String) {
            impl.send(message, toUser)
        }
    }

    @BridgePatternDiagram
    @GenerateClassDiagram
    class CommonMessage(impl: MessageImplementor) : AbstractMessage(impl)

    @BridgePatternDiagram
    @GenerateClassDiagram
    class UrgencyMessage(impl: MessageImplementor) : AbstractMessage(impl) {
        override fun sendMessage(message: String, toUser: String) {
            super.sendMessage("加急：$message", toUser)
        }
    }

    @BridgePatternDiagram
    @GenerateClassDiagram
    class MessageSMS : MessageImplementor {
        override fun send(message: String, toUser: String) {
            println("使用系统内短消息的方法，发送消息'$message'给$toUser")
        }
    }

    @BridgePatternDiagram
    @GenerateClassDiagram
    class MessageEmail : MessageImplementor {
        override fun send(message: String, toUser: String) {
            println("使用邮件短消息的方法，发送消息'$message'给$toUser")
        }
    }
}
