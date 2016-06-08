package model

import java.text.SimpleDateFormat

import akka.actor.{Props, ActorRef, Actor}
import model.Monitor.ReceiversListChanged
import play.api.libs.json.{Writes, Json, JsValue}

import scala.util.Try

/**
  * This Actor communicates the backend with the WebSocket from the monitor website
  */

object ClientMonitor {
  def props(out: ActorRef, monitor: ActorRef) = Props(new ClientMonitor(out, monitor))
}

class ClientMonitor(out : ActorRef, monitor : ActorRef) extends Actor {

  override def preStart = {
    println("Client monitor prestart")
    monitor ! new Monitor.SubscribeForReceiverUpdates()
  }

  val df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")


  implicit val deviceWriter = new Writes[Beacon.Update] {
    def writes(c: Beacon.Update): JsValue = {
      val cJson = Json.obj(
        "rssi" -> c.power,
        "uuid" -> c.uuid,
        "major" -> c.major,
        "minor" -> c.minor,
        "timestamp" -> df.format(c.timestamp)
      )
      cJson
    }
  }

  override def receive = {
    //TODO: Add type safety
    case ReceiversListChanged(receivers) =>
      out ! Json.toJson(receivers)
  }

}
