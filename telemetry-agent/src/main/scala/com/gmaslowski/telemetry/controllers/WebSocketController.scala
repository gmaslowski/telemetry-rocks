package com.gmaslowski.telemetry.controllers

import akka.NotUsed
import akka.actor.PoisonPill
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.gmaslowski.telemetry.{Akka, FormulaOneTelemetry}
import com.gmaslowski.telemetry.controllers.SingleWebSocketConnection.OutgoingMessage
import com.gmaslowski.telemetry.ws.api.JsonSupport
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi.JsonApi

object WebSocketController extends Akka.AkkaThings with JsonSupport {

  val route =
    get {
      path("ws") {
        handleWebSocketMessages(websocket)
      }
    }

  def websocket: Flow[Message, Message, NotUsed] = {
    val wsConnection = system.actorOf(SingleWebSocketConnection.props(FormulaOneTelemetry.wsHandler))

    val incomingMessages: Sink[Message, NotUsed] = Flow[Message]
      .map {
        case TextMessage.Strict(text) => SingleWebSocketConnection.IncomingMessage(new JsonApi {})
      }
      .to(Sink.actorRef[SingleWebSocketConnection.IncomingMessage](wsConnection, PoisonPill))

    val outgoingMessages: Source[Message, NotUsed] = Source.actorRef[OutgoingMessage](10, OverflowStrategy.fail)
      .mapMaterializedValue { outActor =>
        // give the user actor a way to send messages out
        wsConnection ! SingleWebSocketConnection.Connected(outActor)
        NotUsed
      }
      .map((msg: OutgoingMessage) => TextMessage(write(msg.json).compactPrint))

    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
  }
}