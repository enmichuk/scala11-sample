package org.enmichuk.apache

import org.apache.http.entity.StringEntity
import org.apache.http.localserver.LocalServerTestBase
import org.apache.http.protocol.{HttpContext, HttpRequestHandler}
import org.apache.http.{HttpRequest, HttpResponse, HttpStatus}

import scala.util.Try

object PostReceiver {
  private val consumerUser = "test"
  private val consumerPassword = "test"
  private val requestPath = "/test"
  private val scheme = LocalServerTestBase.ProtocolScheme.https

  def main(args: Array[String]): Unit = {
    val port = Try(args(0).toInt).toOption.getOrElse(8074)
    val server = new LocalTestServer(
      scheme = scheme,
      pathHandlers = Map(
        requestPath -> new HttpRequestHandler() {
          def handle(request: HttpRequest, response: HttpResponse, context: HttpContext) {
            val creds = context.getAttribute("creds").asInstanceOf[String]
            val validCredentials = s"$consumerUser:$consumerPassword"
            creds match {
              case credentials if credentials == validCredentials =>
                println(request)
                response.setStatusCode(HttpStatus.SC_OK)
                val entityResponse = new StringEntity("success")
                response.setEntity(entityResponse)
              case _ =>
                response.setStatusCode(HttpStatus.SC_UNAUTHORIZED)
            }
          }
        }
      ),
      port = port
    )
    server.start()
    server.awaitTermination()
  }
}