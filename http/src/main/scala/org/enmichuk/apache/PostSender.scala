package org.enmichuk.apache

import java.io.ByteArrayInputStream
import java.security.KeyStore
import java.security.cert.{CertificateFactory, X509Certificate}
import javax.net.ssl.SSLContext

import org.apache.commons.io.IOUtils
import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.impl.client.{BasicCredentialsProvider, HttpClients}
import org.apache.http.ssl.{SSLContexts, TrustStrategy}

object PostSender extends App {

  def getSSLContext: Option[SSLContext] = {
    val instream = classOf[LocalTestServer].getResource("/server.crt").openStream()
    val certBytes = try IOUtils.toByteArray(instream) finally instream.close()

    val cf = CertificateFactory.getInstance("X.509")
    val in1 = new ByteArrayInputStream(certBytes)
    val chain = Array(
      cf.generateCertificate(in1)
    )
    val ks = KeyStore.getInstance("JKS")
    ks.load(null, null)
    ks.setCertificateEntry("CERT", chain(0))

    val sslcontext = SSLContexts.custom()
      .loadTrustMaterial(ks, new TrustStrategy() {
        def isTrusted(ch: Array[X509Certificate], authType: String) = ch.sameElements(chain)
      }).build()
    Option(sslcontext)
  }

  val provider = new BasicCredentialsProvider()
  val credentials = new UsernamePasswordCredentials("test", "test")
  provider.setCredentials(AuthScope.ANY, credentials)

  val httpClient =
    HttpClients
      .custom()
      .setDefaultCredentialsProvider(provider)
      .setSSLContext(getSSLContext.getOrElse(SSLContexts.createSystemDefault()))
      .build()

  for(i <- 0 to 10) {
    val entity = new ByteArrayEntity("123".getBytes)

    val httpPost = new HttpPost("https://localhost:8074/test")
    httpPost.setEntity(entity)
    val response = httpClient.execute(httpPost)
    response.close()
  }

  httpClient.close()
}