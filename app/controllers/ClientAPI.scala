package controllers

import akka.actor.{ActorRef, ActorSystem}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.{Inject, Singleton}
import model.ReceiverDev
import play.api.Play.current

import scala.concurrent.Future

@Singleton
class ClientAPI  @Inject() (system: ActorSystem)  extends Controller {

  def socket = WebSocket.tryAcceptWithActor[String, String] { request =>
    Future.successful(request.headers.get("receiverId") match {
      case None =>
        Left(Forbidden)
      case Some(id) =>
        Right(ReceiverDev.props(_, id))
    })
  }

}
