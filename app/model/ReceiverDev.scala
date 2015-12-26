package model

import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.{ActorRef, Props, Actor}
import model.ReceiverDev.FoundDevices
import play.api.libs.json._

import scala.util.{Success, Try}

/**
  * Created by darioalessandro on 12/21/15.
  */

case class Device(RSSI: String, identifier : String, timeIntervalSince1970 : Date, name : Option[String])

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

  implicit var deviceReader =  new Reads[Device] {
//    val dateFormatter = C.dateFormatter

    def reads(r: JsValue): JsResult[Device] = {
      val RSSI: String = (r \ "RSSI").as[String]
      val identifier : String = (r \ "identifier").as[String]
      val dateInt = (r \ "timeIntervalSince1970").as[String]
      val timeIntervalSince1970 : Date = new java.util.Date(Math.round(dateInt.toDouble) * 1000)
      val name = (r \ "name").asOpt[String]
      JsSuccess(Device(RSSI, identifier, timeIntervalSince1970, name))
    }
  }

  def devicesFromJson(json : JsValue) : Try[Array[Device]] = {
    json.validate[Array[Device]].map { o =>
      Success(o)
    }.recoverTotal(
      e  => scala.util.Failure(new Throwable("parser broke"))
    )
  }

}
