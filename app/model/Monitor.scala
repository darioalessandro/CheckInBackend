package model

import akka.actor._
import model.Monitor.{SubscribeForReceiverUpdates, ReceiversListChanged}
import model.Receiver.FoundDevices

/**
  * Receives all the updates from the Receivers and routes them through the system.
  */

object Monitor {
  case class ReceiversListChanged(receivers : Map[String, Array[Beacon.Update]])
  class SubscribeForReceiverUpdates
}

class Monitor extends Actor {

  var receivers : Map[String, Array[Beacon.Update]] = Map[String, Array[Beacon.Update]]()

  var beacons : Map[String, ActorRef] = Map[String, ActorRef]()

  var clientMonitors : List[ActorRef] = List[ActorRef]()

  override def receive = {
    case FoundDevices(devices, receiverId, username) =>
     receivers = receivers + (s"""$receiverId-$username""" -> devices)

     devices foreach { device =>
       val beacon  = beacons.get(device.identifier).map {c => c} getOrElse {

         val newBeacon = context.actorOf(Props(new Beacon(device.identifier, device.name)), name = device.identifier)
         beacons = beacons + (device.identifier -> newBeacon)
         newBeacon
       }
       beacon ! Beacon.DidGetUpdate(device, receiverId, username)
     }


      //TODO: build history

     broadcastToMonitors(receivers)

    case d : Terminated =>
      this.context.unwatch(d.getActor)
      this.clientMonitors = this.clientMonitors.filter(_ != d.getActor)

    case s : SubscribeForReceiverUpdates =>
      this.context.watch(sender())
      this.clientMonitors = sender() :: this.clientMonitors
      broadcastToMonitors(receivers)

    case d : DeadLetter =>
      //TODO: handle clientMonitor disconnection


  }

  def broadcastToMonitors(receivers : Map[String, Array[Beacon.Update]]) = {
    //TODO: use actor selection instead of keeping a manual array
    clientMonitors foreach { client =>
      client ! ReceiversListChanged(receivers)
    }
  }

}
