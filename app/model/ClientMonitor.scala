package model

import akka.actor.{Props, ActorRef, Actor}
import model.Monitor.ReceiversListChanged
import play.api.libs.json.{Json, JsValue}

import scala.util.Try

/**
  * Created by darioalessandro on 12/25/15.
  */

object ClientMonitor {
  def props(out: ActorRef, monitor: ActorRef) = Props(new ClientMonitor(out, monitor))
}

class ClientMonitor(out : ActorRef, monitor : ActorRef) extends Actor {

  implicit var deviceParser = Json.format[Device]

  override def preStart = {
    println("Client monitor prestart")
    monitor ! new Monitor.SubscribeForReceiverUpdates()
  }

  override def receive = {
    //TODO: Add type safety
    case ReceiversListChanged(receivers) =>
      out ! Json.toJson(receivers)
  }

}
