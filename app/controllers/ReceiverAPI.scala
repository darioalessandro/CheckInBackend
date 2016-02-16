package controllers

import akka.actor.{Props, ActorRef, ActorSystem}
import model.EmailSender.Email
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.routing.JavaScriptReverseRouter
import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.{Inject, Singleton}
import model.{ClientMonitor, Receiver}
import play.api.Play.current

import scala.concurrent.Future

/*
API between receiver and backend.
Also between monitor website and the backend.
 */

@Singleton
class ReceiverAPI  @Inject()(system: ActorSystem)  extends Controller {

  val monitor = system.actorOf(Props[model.Monitor]) //Single point of failure, need to refactor
  val emailSender = system.actorOf(Props[model.EmailSender], name = "emailSender") //Single point of failure, need to refactor

  def socket = WebSocket.tryAcceptWithActor[JsValue, JsValue] { request =>
    Future.successful((request.headers.get("receiverId"),request.headers.get("username")) match {

      case (Some(id), Some(username)) =>
        Right(Receiver.props(_, username,id,monitor))

      case _ =>
        Left(Forbidden)
    })
  }

  def monitorSocket = WebSocket.tryAcceptWithActor[JsValue, JsValue] { request =>
    Future.successful(
        Right(ClientMonitor.props(_, monitor))
    )
  }

  def jsRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("ClientAPIRouter")(
        routes.javascript.ReceiverAPI.monitorSocket
      )
    ).as("text/javascript")
  }

}
