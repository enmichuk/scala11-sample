package org.enmichuk.apache

import java.io.ByteArrayInputStream
import java.math.BigInteger
import java.security.{KeyFactory, KeyStore}
import java.security.cert.CertificateFactory
import java.security.spec.RSAPrivateCrtKeySpec
import java.util.concurrent.TimeUnit
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

import org.apache.http.HttpHost
import org.apache.http.conn.ssl.CertificatesToPlayWith
import org.apache.http.impl.bootstrap.ServerBootstrap
import org.apache.http.localserver.{LocalServerTestBase, RequestBasicAuth, ResponseBasicUnauthorized}
import org.apache.http.protocol.{HttpProcessorBuilder, HttpRequestHandler}

class LocalTestServerWithTestCert(
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
        val sslcontext = SSLContext.getInstance("TLSv1")
        initWithSsl(sslcontext)
        bs.setSslContext(sslcontext)
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

  private def initWithSsl(sslcontext: SSLContext) = {
    val password = "changeit"
    val pwd = password.toCharArray
    val k = new RSAPrivateCrtKeySpec(
      new BigInteger(CertificatesToPlayWith.RSA_PUBLIC_MODULUS, 16),
      new BigInteger(CertificatesToPlayWith.RSA_PUBLIC_EXPONENT, 10),
      new BigInteger(CertificatesToPlayWith.RSA_PRIVATE_EXPONENT, 16),
      new BigInteger(CertificatesToPlayWith.RSA_PRIME1, 16),
      new BigInteger(CertificatesToPlayWith.RSA_PRIME2, 16),
      new BigInteger(CertificatesToPlayWith.RSA_EXPONENT1, 16),
      new BigInteger(CertificatesToPlayWith.RSA_EXPONENT2, 16),
      new BigInteger(CertificatesToPlayWith.RSA_COEFFICIENT, 16)
    )

    val pk = KeyFactory.getInstance("RSA").generatePrivate(k)
    val ks = KeyStore.getInstance("JKS")
    ks.load(null, null)

    val cf = CertificateFactory.getInstance("X.509")
    val in1 = new ByteArrayInputStream(CertificatesToPlayWith.X509_FOO)
    val in2 = new ByteArrayInputStream(CertificatesToPlayWith.X509_INTERMEDIATE_CA)
    val in3 = new ByteArrayInputStream(CertificatesToPlayWith.X509_ROOT_CA)
    val chain = Array(
      cf.generateCertificate(in1),
      cf.generateCertificate(in2),
      cf.generateCertificate(in3)
    )

    ks.setKeyEntry("RSA_KEY", pk, pwd, chain)
    ks.setCertificateEntry("CERT", chain(2))

    val kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
    kmfactory.init(ks, pwd)
    val keymanagers = kmfactory.getKeyManagers
    val tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    tmfactory.init(ks)
    val trustmanagers = tmfactory.getTrustManagers
    sslcontext.init(keymanagers, trustmanagers, null)
  }

}