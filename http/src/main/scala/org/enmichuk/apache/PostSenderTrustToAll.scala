package org.enmichuk.apache

import java.security.cert.X509Certificate
import javax.net.ssl.{HostnameVerifier, SSLSession}

import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.impl.client.{BasicCredentialsProvider, HttpClients}
import org.apache.http.ssl.{SSLContexts, TrustStrategy}

object PostSenderTrustToAll extends App {

  val provider = new BasicCredentialsProvider()
  val credentials = new UsernamePasswordCredentials("test", "test")
  provider.setCredentials(AuthScope.ANY, credentials)


  val sslcontext = SSLContexts.custom()
    .loadTrustMaterial(new TrustStrategy() {
      def isTrusted(chain: Array[X509Certificate], authType: String) = true
    }).build()

  val httpClient =
    HttpClients
      .custom()
      .setDefaultCredentialsProvider(provider)
      .setSSLContext(sslcontext)
      .setSSLHostnameVerifier(new TestX509HostnameVerifier())
      .build()

  for(i <- 0 to 10) {
    val entity = new ByteArrayEntity("123".getBytes)

    val httpPost = new HttpPost("https://localhost:8073/test")
    //    val httpPost = new HttpPost("https://mail.ru")
    httpPost.setEntity(entity)
    val response = httpClient.execute(httpPost)
    response.close()
  }

  httpClient.close()

}

class TestX509HostnameVerifier extends HostnameVerifier {

  def verify(host: String, session: SSLSession) = true

}