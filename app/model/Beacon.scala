package model

import java.util.Date

import akka.actor.{Cancellable, Actor}
import model.Beacon.{WatchdogTimeout, DidGetUpdate, Update}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This is the Device that the child carries.
  *
  * The device is responsible for tracking the connection status and send the appropiate notifications to the email server.
  */

object Beacon {
  case class Update(RSSI: String, identifier : String, timeIntervalSince1970 : Date, name : Option[String])
  case class DidGetUpdate(update : Update, receiverId : String, receiverUsername : String)
  case class WatchdogTimeout(receiverId : String, receiverUsername : String)
}



class Beacon(uniqueIdentifier : String, deviceName : Option[String]) extends Actor {

  object BeaconStatus extends Enumeration {
    type BeaconStatus = Value
    val Connected,Disconnected = Value
  }

  var status = BeaconStatus.Disconnected

  val emailSender = context.actorSelection("/user/emailSender")

  var timer : Option[Cancellable] = None

  override def receive = {

    case WatchdogTimeout(receiverId, username) =>
      if(status == BeaconStatus.Connected) {
        status = BeaconStatus.Disconnected
        val content = s"$deviceName disconnected from $receiverId @ ${new Date().toString} owned by $username"
        println(content)
        emailSender ! EmailSender.Email(content, List("dario.lencina@compositetech.com", "cadams@compositetech.com", "carmen.waite@compositetech.com"), content)
      }

    case DidGetUpdate(Beacon.Update(rssi , identifier, timeIntervalSince1970 : Date, name), receiverId, username) =>

      val rssiFloat = rssi.toInt

      if(rssiFloat != -127) {

        timer.foreach { t => t.cancel() }

        val updateMilis = timeIntervalSince1970.getTime
        val systemMilis = new Date().getTime

        val timeDelta = Math.abs(updateMilis - systemMilis)

        timer = Some(context.system.scheduler.scheduleOnce(10 seconds, self, WatchdogTimeout(receiverId, username)))

        val emailContent : Option[String] = status match {
          case BeaconStatus.Disconnected if timeDelta < 10000 =>
            status = BeaconStatus.Connected
            Some(s"$name connected to $receiverId owned by $username")

          case BeaconStatus.Connected if timeDelta > 10000 =>
            status = BeaconStatus.Disconnected
            Some(s"$name disconnected")

          case _ =>
            None
        }

        emailContent foreach { content =>
          println(content)
          emailSender ! EmailSender.Email(content + s" @ ${new Date().toString}", List("dario.lencina@compositetech.com", "cadams@compositetech.com", "carmen.waite@compositetech.com"), s"$content @ ${new Date().toString}")
        }

      }

  }

}
