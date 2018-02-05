package org.enmichuk.apache

import java.io.ByteArrayInputStream
import java.security.cert.CertificateFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.{KeyFactory, KeyStore}
import java.util.concurrent.TimeUnit
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.IOUtils
import org.apache.http.HttpHost
import org.apache.http.impl.bootstrap.ServerBootstrap
import org.apache.http.localserver.{LocalServerTestBase, RequestBasicAuth, ResponseBasicUnauthorized}
import org.apache.http.protocol._
import org.apache.http.ssl.SSLContexts

class LocalTestServer(
  scheme: LocalServerTestBase.ProtocolScheme,
  pathHandlers: Map[String, HttpRequestHandler],
  port: Int)
  extends LocalServerTestBase(scheme) {

  override def setUp(): Unit = {
    super.setUp()
    val httpproc = HttpProcessorBuilder.create()
      .add(new RequestBasicAuth())
      .add(new ResponseBasicUnauthorized())
      .build()

    val bs = ServerBootstrap.bootstrap().setHttpProcessor(httpproc).setListenerPort(port)
    val bootstrap =
      if (scheme == LocalServerTestBase.ProtocolScheme.https) {
        bs.setSslContext(getSslContext)
      } else {
        bs
      }
    pathHandlers.foreach {
      case (path, handler) => bootstrap.registerHandler(path, handler)
    }

    this.serverBootstrap = bootstrap
  }

  override def start(): HttpHost = {
    setUp()
    super.start()
  }

  def awaitTermination(): Unit = {
    server.awaitTermination(10000000, TimeUnit.SECONDS)
  }

  private def getSslContext: SSLContext = {
    val sslcontext = SSLContexts.custom().build()
    val password = "changeit"
    val pwd = password.toCharArray

    val pemPrivateKeyStream = classOf[LocalTestServer].getResource("/server.pem").openStream()
    val privateKeyBytes = try
      IOUtils.toString(pemPrivateKeyStream, "UTF-8")
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
    finally pemPrivateKeyStream.close()

    val privateKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyBytes))
    val keyFactory = KeyFactory.getInstance("RSA")
    val pk = keyFactory.generatePrivate(privateKeySpec)

    val ks = KeyStore.getInstance("JKS")
    ks.load(null, null)

    val instream = classOf[LocalTestServer].getResource("/server.crt").openStream()
    val certBytes = try IOUtils.toByteArray(instream) finally instream.close()

    val cf = CertificateFactory.getInstance("X.509")
    val in1 = new ByteArrayInputStream(certBytes)
    val chain = Array(
      cf.generateCertificate(in1)
    )

    ks.setKeyEntry("RSA_KEY", pk, pwd, chain)
    ks.setCertificateEntry("CERT", chain(0))

    val kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
    kmfactory.init(ks, pwd)
    val keymanagers = kmfactory.getKeyManagers
    val tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    tmfactory.init(ks)
    val trustmanagers = tmfactory.getTrustManagers
    sslcontext.init(keymanagers, trustmanagers, null)
    sslcontext
  }

}
