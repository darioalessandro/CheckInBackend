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
  def props(out: ActorRef, username : String, receiverId : String, monitor : ActorRef) = Props(new Receiver(out, username,receiverId, monitor))
  case class FoundDevices(devices : Array[Beacon.Update], receiverId : String, username : String)
}


class Receiver(out: ActorRef, username : String, receiverId : String, monitor : ActorRef) extends Actor {

  def receive = {
    case msg: JsValue =>
      devicesFromJson(msg).map { devices =>
        monitor ! FoundDevices(devices, receiverId, username)
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
      val uuid: String = (r \ "uuid").as[String]
      val major : String = (r \ "major").as[String]
      val minor = (r \ "minor").as[String]
      val power = (r \ "power").as[String]

      val timestamp : Date = new Date()//new java.util.Date(Math.round((r \ "timestamp").as[String].toDouble) * 1000)
      val name = (r \ "name").asOpt[String]
      JsSuccess(Beacon.Update(uuid, major,  minor, power, timestamp))
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
