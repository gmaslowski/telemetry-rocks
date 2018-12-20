package rocks.telemetry.core.ws.agent

import akka.actor.{Actor, ActorRef, Props}
import akka.util.ByteString
import rocks.telemetry.core.FormulaOneTelemetry.changesHandler
import rocks.telemetry.core.live.model.TelemetryReceiver
import rocks.telemetry.core.ws.agent.AgentWebSocketConnection.{AuthenticationRequest, Connected, PacketMessage}
import rocks.telemetry.core.ws.agent.AuthenticationVerifier.{AuthenticationVerificationResult, VerifyAuthentication}

object AgentWebSocketConnection {

  def props(authenticationVerifier: ActorRef) = Props(classOf[AgentWebSocketConnection], authenticationVerifier)

  // api
  case class Connected(outgoing: ActorRef)

  trait IncomingMessage
  case class PacketMessage(byteString: ByteString) extends IncomingMessage
  case class AuthenticationRequest(email: String, apiToken: String) extends IncomingMessage

}

class AgentWebSocketConnection(val authenticationVerifier: ActorRef) extends Actor {

  var agent: ActorRef = _
  var telemetryReceiver: ActorRef = _

  override def receive = {
    case PacketMessage(packet) =>
      telemetryReceiver ! packet

    case AuthenticationRequest(email, apiToken) =>
      authenticationVerifier ! VerifyAuthentication(email, apiToken)

    case AuthenticationVerificationResult(email, "OK") =>
      telemetryReceiver = context.actorOf(TelemetryReceiver.props(changesHandler, email), email)
      agent ! "OK"

    case AuthenticationVerificationResult(_, "NOK") =>
      agent ! "NOK"
      context.stop(self)

    case Connected(outgoing) =>
      agent = outgoing
  }
}
