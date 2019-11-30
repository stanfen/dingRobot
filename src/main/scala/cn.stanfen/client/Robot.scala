package cn.stanfen.client

import java.io.IOException
import java.nio.charset.StandardCharsets

import cn.stanfen.Config._
import cn.stanfen.webhook.WebHook
import com.alibaba.fastjson.JSONObject
import org.apache.http.client.ClientProtocolException
import org.apache.http.{HttpHost, HttpStatus}
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.{BasicCredentialsProvider, CloseableHttpClient, HttpClients}
import org.apache.http.util.EntityUtils

class Robot(webHook: String, accessToken: String) {
  lazy val jsonObj = new JSONObject()

  def isProxyEnable: Boolean = proxy.nonEmpty && port != 0

  def setProxy(proxy: String, port: Int, username: String, psswd: String): (RequestConfig, BasicCredentialsProvider) = {
    import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
    val host = new HttpHost(proxy, port)
    val provider = new BasicCredentialsProvider()
    provider.setCredentials(new AuthScope(host), new UsernamePasswordCredentials(username, psswd))

    (RequestConfig.custom.setProxy(host).build, provider)
  }

  def constructHttpPost(msg: String): HttpPost = {
    val post = new HttpPost(webHook + accessToken)
    val entity = new StringEntity(msg, StandardCharsets.UTF_8)
    post.setEntity(entity)
    post.addHeader("Content-Type", "application/json; charset=utf-8")

    post
  }

  def sendMsg(msg: String): DingTalkResponse = {
    val res = DingTalkResponse(HttpStatus.SC_NOT_FOUND)
    val post = constructHttpPost(msg)
    val client: CloseableHttpClient = if (isProxyEnable) {
      val (config, provider) = setProxy(proxy, port, username, password)
      post.setConfig(config)
      HttpClients.custom().setDefaultCredentialsProvider(provider).build()
    } else {
      HttpClients.custom().build()
    }

    try {
      val rsp = client.execute(post)
      try {
        if (rsp.getStatusLine().getStatusCode == HttpStatus.SC_OK) {
          val result: String = EntityUtils.toString(rsp.getEntity)
          val toJson = jsonObj.getJSONObject(result)
          println(result)
          val errCode = toJson.getInteger("errcode")
          val msg = toJson.getString("errmsg")
          res.copy(status =  HttpStatus.SC_OK, errorCode = errCode, errMsg = msg)
        }
      }
      catch {
        case io: IOException => println(s"http response IOException ${io}")
        case cpe: ClientProtocolException => println(s"http ClientProtocolException ${cpe}")
        case ex: Exception => throw ex
      } finally if (rsp != null) rsp.close()
    } catch {
      case ex: Exception => println(s"http response exception ${ex}")
    } finally {
      if (client != null) client.close()
    }

    res
  }



}

case class DingTalkResponse(status: Int, errorCode: Int = 0, errMsg: String = "")

