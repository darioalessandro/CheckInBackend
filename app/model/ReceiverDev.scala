package model

import akka.actor.{ActorRef, Props, Actor}

/**
  * Created by darioalessandro on 12/21/15.
  */
object ReceiverDev {
  def props(out: ActorRef) = Props(new ReceiverDev(out))
}


class ReceiverDev(out: ActorRef) extends Actor {

  def receive = {
    case msg: String =>
      out ! ("I received your message: " + msg)
  }

  override def postStop() = {
    println("post stop")
    //someResource.close()
  }

  override def preStart() = {
    println("pre start")
  }

}
