package model

import akka.actor.{ActorRef, Props, Actor}
import model.ReceiverDev.FoundDevices
import play.api.libs.json.{JsError, Json, JsValue}

import scala.util.{Success, Try}

/**
  * Created by darioalessandro on 12/21/15.
  */

case class Device(RSSI: String, identifier : String)

object ReceiverDev {
  def props(out: ActorRef, receiverId : String, monitor : ActorRef) = Props(new ReceiverDev(out, receiverId, monitor))
  case class FoundDevices(devices : Array[Device], receiverId : String)
}


class ReceiverDev(out: ActorRef, receiverId : String, monitor : ActorRef) extends Actor {

  def receive = {
    case msg: JsValue =>
      devicesFromJson(msg).map { devices =>
        monitor ! FoundDevices(devices, receiverId)
      }
  }

  override def postStop() = {
    println("post stop")
  }

  override def preStart() = {
    println(s"pre start $receiverId")
  }

  implicit var deviceParser = Json.format[Device]

  def devicesFromJson(json : JsValue) : Try[Array[Device]] = {
    json.validate[Array[Device]].map { o =>
      Success(o)
    }.recoverTotal(
      e  => scala.util.Failure(new Throwable("parser broke"))
    )
  }

}
