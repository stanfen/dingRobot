package cn.stanfen.formatter

import org.scalatest.{FlatSpec, Matchers}

class TextMessageTest extends FlatSpec with Matchers  {

  "TextEn" should("tojson funciton with english string") in {
    val en = "english test"
    val o = new TextMessage(en)
//    println(o.toJsonString)
    o.toJsonString shouldBe(s"""{"text":{"content":"english test"},"msgtype":"text"}""")
  }

  "TextZh" should("tojson funciton with english string") in {
    val zh = "中文编码测试"
    val o = new TextMessage(zh)
//    println(o.toJsonString)
    o.toJsonString shouldBe(s"""{"text":{"content":"中文编码测试"},"msgtype":"text"}""")
  }
}
