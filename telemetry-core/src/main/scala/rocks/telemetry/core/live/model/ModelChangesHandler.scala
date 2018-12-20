package rocks.telemetry.core.live.model

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import rocks.telemetry.core.live.model.ModelChangesHandler.RegisterHandler

object ModelChangesHandler {
  def props = Props(classOf[ModelChangesHandler])

  // api
  case class RegisterHandler(handler: ActorRef)
}

class ModelChangesHandler extends Actor with ActorLogging {

  var handlers: Seq[ActorRef] = Seq.empty[ActorRef]

  override def receive = {
    case RegisterHandler(handler) =>
      handlers = handlers :+ handler

    case msg: Any =>
      handlers.foreach(handler => handler ! msg)
  }
}
