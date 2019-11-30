package cn.stanfen

import org.scalatest.{FlatSpec, Matchers}

class ConfigTest extends FlatSpec with Matchers {
    it should "test config file value" in {
      println(Config.webHook)
      println(Config.accessToken)
      println(Config.port)
    }
}
