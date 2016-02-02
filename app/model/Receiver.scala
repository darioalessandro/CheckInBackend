package model

import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.{ActorRef, Props, Actor}
import model.Receiver.FoundDevices
import play.api.libs.json._

import scala.util.{Success, Try}

/**
  * Communicates the backend and the Receivers.
  */


object Receiver {
  def props(out: ActorRef, receiverId : String, monitor : ActorRef) = Props(new Receiver(out, receiverId, monitor))
  case class FoundDevices(devices : Array[Beacon.Update], receiverId : String)
}


class Receiver(out: ActorRef, receiverId : String, monitor : ActorRef) extends Actor {

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

  implicit var deviceReader =  new Reads[Beacon.Update] {
//    val dateFormatter = C.dateFormatter

    def reads(r: JsValue): JsResult[Beacon.Update] = {
      val RSSI: String = (r \ "RSSI").as[String]
      val identifier : String = (r \ "identifier").as[String]
      val dateInt = (r \ "timeIntervalSince1970").as[String]
      val timeIntervalSince1970 : Date = new java.util.Date(Math.round(dateInt.toDouble) * 1000)
      val name = (r \ "name").asOpt[String]
      JsSuccess(Beacon.Update(RSSI, identifier, timeIntervalSince1970, name))
    }
  }

  def devicesFromJson(json : JsValue) : Try[Array[Beacon.Update]] = {
    json.validate[Array[Beacon.Update]].map { o =>
      Success(o)
    }.recoverTotal(
      e  => scala.util.Failure(new Throwable("parser broke"))
    )
  }

}
