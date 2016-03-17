package model

import akka.actor.{Props, ActorRef, Actor}

/**
  * Created by darioalessandro on 2/28/16.
  */

object HomeMonitor {

  def props(out: ActorRef,session : DAO.Session, user : DAO.User) = Props(new HomeMonitor(out,session, user))
}

class HomeMonitor(out : ActorRef, session : DAO.Session, user : DAO.User) extends Actor {


  override def receive = {

    case Beacon.OnStatusChanged(identifier, beaconId, deviceName, status, receiverId, timestamp) =>

  }

}
