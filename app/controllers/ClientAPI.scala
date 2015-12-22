package controllers

import akka.actor.ActorSystem
import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.{Inject, Singleton}
import model.ReceiverDev
import play.api.Play.current

@Singleton
class ClientAPI  @Inject() (system: ActorSystem)  extends Controller {

  def socket = WebSocket.acceptWithActor[String, String] {
    request =>
      println(request.headers)
      out =>
        ReceiverDev.props(out)
  }

}
