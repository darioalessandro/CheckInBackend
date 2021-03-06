package model

import java.util.Date

import akka.actor.{Cancellable, Actor}
import anorm._
import model.Beacon.{WatchdogTimeout, DidGetUpdate, Update}
import play.api.db.DB
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try
import play.api.Play.current

/**
  * This is the Device that the child carries.
  *
  * The device is responsible for tracking the connection status and send the appropiate notifications to the email server.
  */

object Beacon {
  case class Update(uuid:String, major:String, minor:String,power:String,timestamp: Date) {
    def identifier = s"$uuid-$major-$minor"
  }

  case class DidGetUpdate(update : Update, receiverId : String, receiverUsername : String)
  case class WatchdogTimeout(receiverId : String, receiverUsername : String)
  case class OnStatusChanged(uniqueIdentifier : String, beaconId: String, deviceName : Option[String], status:String, receiverId:String, timestamp:Date)
  case class Owner(username : String, name : String)
}

class Beacon(uuid : String, major : String, minor : String, deviceName : Option[String]) extends Actor {

  object BeaconStatus extends Enumeration {
    type BeaconStatus = Value
    val Connected,Disconnected = Value
  }

  var status = BeaconStatus.Disconnected

  val emailSender = context.actorSelection("/user/emailSender")

  var timer : Option[Cancellable] = None

  val parser: RowParser[Beacon.Owner] = new RowParser[Beacon.Owner] {
      override def apply(v1: Row): SqlResult[Beacon.Owner] = {
            Success(new Beacon.Owner(v1[String]("username"),v1[String]("name")))
      }
  }

  def owner  = {
    DB.withConnection("authorization") { implicit c =>
      SQL(
        """
          SELECT * FROM checkin.beacons where uuid = {uuid};
        """
      ).on('uuid -> (uuid+"-"+major+"-"+minor)).as[Option[Beacon.Owner]](parser.singleOpt)
    }
  }

  override def receive = {

    case WatchdogTimeout(receiverId, username) =>
      if(status == BeaconStatus.Connected) {
        status = BeaconStatus.Disconnected
        val ownerinfo = owner.map(o => o.username+"' "+o.name).getOrElse(s"unknown beacon $uuid")
        val content = s"$ownerinfo disconnected from $receiverId @ ${new Date().toString} owned by $username"
        println(content)
        emailSender ! EmailSender.Email(content, List("dario.lencina@compositetech.com", "cadams@compositetech.com", "carmen.waite@compositetech.com"), content)
      }


    case DidGetUpdate(Beacon.Update(_uuid, _major, _minor,power,timestamp), receiverId, username) =>

      val rssiFloat = power.toInt

      if(rssiFloat != -127) {

        timer.foreach { t => t.cancel() }

        val updateMilis = timestamp.getTime
        val systemMilis = new Date().getTime

        val timeDelta = Math.abs(updateMilis - systemMilis)

        println(s"delta $timeDelta")

        timer = Some(context.system.scheduler.scheduleOnce(10 seconds, self, WatchdogTimeout(receiverId, username)))

        val emailContent : Option[String] = status match {
          case BeaconStatus.Disconnected if timeDelta < 10000 =>
            status = BeaconStatus.Connected
            val ownerinfo = owner.map(o => o.username+"' "+o.name).getOrElse(s"unknown beacon $uuid")
            Some(s"$ownerinfo connected to $receiverId owned by $username")

          case BeaconStatus.Connected if timeDelta > 10000 =>
            val ownerinfo = owner.map(o => o.username+"' "+o.name).getOrElse(s"unknown beacon $uuid")
            status = BeaconStatus.Disconnected
            Some(s"$ownerinfo disconnected")

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
