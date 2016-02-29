package model

import akka.actor.{Props, ActorRef, Actor}

/**
  * Created by darioalessandro on 2/28/16.
  */

object HomeMonitor {
  def props(out: ActorRef) = Props(new HomeMonitor(out))
}

class HomeMonitor(out : ActorRef) extends Actor {


  override def receive = {

    case Beacon.OnStatusChanged(identifier, beaconId, deviceName, status, receiverId, timestamp) =>

  }

}
