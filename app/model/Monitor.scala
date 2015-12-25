package model

import akka.actor.{DeadLetter, ActorRef, Actor}
import model.Monitor.{SubscribeForReceiverUpdates, ReceiversListChanged}
import model.ReceiverDev.FoundDevices

/**
  * Created by darioalessandro on 12/25/15.
  */

object Monitor {
  case class ReceiversListChanged(receivers : Map[String, Array[Device]])
  class SubscribeForReceiverUpdates
}

class Monitor extends Actor {

  var receivers : Map[String, Array[Device]] = Map[String, Array[Device]]()

  var clientMonitors : List[ActorRef] = List[ActorRef]()

  override def receive = {
    case FoundDevices(devices, receiverId) =>
     receivers = receivers + (receiverId -> devices)
      //TODO: build history
     println(receivers.mkString)
     broadcastToMonitors(receivers)

    case s : SubscribeForReceiverUpdates =>
      this.context.watch(sender())
      this.clientMonitors = sender() :: this.clientMonitors

    case d : DeadLetter =>
      //TODO: handle clientMonitor disconnection


  }

  def broadcastToMonitors(receivers : Map[String, Array[Device]]) = {
    //TODO: use actor selection instead of keeping a manual array
    clientMonitors foreach { client =>
      client ! ReceiversListChanged(receivers)
    }
  }

}
