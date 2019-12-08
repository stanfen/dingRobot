package cn.stanfen.client

import cn.stanfen.Config
import cn.stanfen.formatter.TextMessage
import org.scalatest.{FlatSpec, Matchers}

class RobotTest extends FlatSpec with Matchers {
  it should("test post to dingtalk") in {
    val msg = new TextMessage("[desktop]send onway").toJsonString
    val ob = new Robot(Config.webHook, Config.accessToken)

    val t = ob.sendMsg(msg)
    println(t)
  }
}
