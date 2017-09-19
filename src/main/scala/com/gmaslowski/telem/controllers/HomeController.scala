package com.gmaslowski.telem.controllers

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.gmaslowski.telem.{FormulaOneTelemetry, WebSocketClient}
import play.api.Logger
import play.api.libs.streams.ActorFlow
import play.api.mvc._

@Singleton
class HomeController @Inject()(cc: ControllerComponents)
                              (implicit system: ActorSystem, mat: Materializer)
  extends AbstractController(cc) with SameOriginCheck {

  val logger = play.api.Logger(getClass)

  def index = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def ws = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      WebSocketClient.props(FormulaOneTelemetry.webSocketHandler.get, out)
    }
  }
}

trait SameOriginCheck {

  def logger: Logger

  def sameOriginCheck(rh: RequestHeader): Boolean = {
    rh.headers.get("Origin") match {
      case Some(originValue) if originMatches(originValue) =>
        logger.debug(s"originCheck: originValue = $originValue")
        true

      case Some(badOrigin) =>
        logger.error(s"originCheck: rejecting request because Origin header value ${badOrigin} is not in the same origin")
        false

      case None =>
        logger.error("originCheck: rejecting request because no Origin header found")
        false
    }
  }

  def originMatches(origin: String): Boolean = {
    origin.contains("localhost:9000") || origin.contains("localhost:19001")
  }

}
