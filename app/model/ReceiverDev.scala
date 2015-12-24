package model

import akka.actor.{ActorRef, Props, Actor}

/**
  * Created by darioalessandro on 12/21/15.
  */

case class Device(uuid: String)

object ReceiverDev {
  def props(out: ActorRef, receiverId : String) = Props(new ReceiverDev(out, receiverId))
  case class FoundDevices(Device : List[Device])
}


class ReceiverDev(out: ActorRef, receiverId : String) extends Actor {

  def receive = {
    case msg: String =>
      println(s"got message $msg")
      //out ! ("I received your message: " + msg)
  }

  override def postStop() = {
    println("post stop")
  }

  override def preStart() = {
    println(s"pre start $receiverId")
  }

}
