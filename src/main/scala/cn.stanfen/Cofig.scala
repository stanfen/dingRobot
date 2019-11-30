package cn.stanfen

import org.apache.commons.configuration.PropertiesConfiguration

import scala.util.Try

object Config {
  private val config = new PropertiesConfiguration("./application.properties")

  val webHook = config.getString("webhook")
  val accessToken = config.getString("accesstoken")
  val keyWord = config.getString("keyword")
  val robotMode = config.getString("robotmode")
  val username = config.getString("username")
  val password = config.getString("password")
  val proxy = config.getString("proxy")
  val port = Try(config.getInt("port")).getOrElse(0)
}
