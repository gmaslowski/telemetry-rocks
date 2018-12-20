package rocks.telemetry.core.ws.agent

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ContentTypes.`application/json`
import akka.http.scaladsl.model.HttpMethods.POST
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.util.{ByteString, Timeout}
import rocks.telemetry.core.Akka.AkkaThings
import rocks.telemetry.core.FormulaOneTelemetry
import rocks.telemetry.core.ws.agent.AuthenticationVerifier.{AuthenticationVerificationResult, VerifyAuthentication}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object AuthenticationVerifier {
  def props = Props(classOf[AuthenticationVerifier])

  case class VerifyAuthentication(email: String, apiKey: String)
  case class AuthenticationVerificationResult(email: String, msg: String)
}

class AuthenticationVerifier extends Actor with ActorLogging with AkkaThings {

  implicit val ec: ExecutionContext = context.dispatcher
  implicit val timeout: Timeout = 5 seconds


  val telemetryUsersUrl = FormulaOneTelemetry.config.getString("telemetry.users.url") + "/user/verifyApiKey"

  override def receive = {
    case VerifyAuthentication(email, apiKey) =>
      log.info(s"Verifying key: $apiKey for user: $email")

      // closing over sender
      val respondTo = sender

      Http()
        .singleRequest(
          HttpRequest(uri = s"$telemetryUsersUrl/$email")
            .withMethod(POST)
            .withEntity(`application/json`, s"""{"apiKey": "$apiKey"}""")
        ) onComplete {
        case Success(httpResponse: HttpResponse) => {
          httpResponse.entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
            log.info("Got response, body: " + body.utf8String)
            if (body.utf8String.contains("true")) {
              respondTo ! AuthenticationVerificationResult(email, "OK")
            }
          }
        }
        case Failure(t) => {
          log.warning("Verification failed.", t.getMessage)
          respondTo ! AuthenticationVerificationResult(email, "NOK")
        }
      }
  }
}
