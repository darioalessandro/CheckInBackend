package controllers

import akka.actor.{Props, ActorRef, ActorSystem}
import model.EmailSender.Email
import model.clientAPI.{API, FAPI}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.routing.JavaScriptReverseRouter
import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.{Inject, Singleton}
import model._
import play.api.Play.current

import scala.concurrent.Future

/*
API between receiver and backend.
Also between monitor website and the backend.
 */

@Singleton
class ReceiverAPI  @Inject()(system: ActorSystem)  extends Secured {

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

  def homeSocket = WebSocket.tryAcceptWithActor[JsValue, JsValue] { request =>

    val sessionDetails = for { headerSession <- request.session.get(C.sessionHeader)
                               serverSession <- DAO.sessionForSessionId(headerSession)
                               user <- DAO.userForSessionId(headerSession)
    } yield (serverSession,user)

    sessionDetails match {
      case Some((session : DAO.Session, user : DAO.User)) =>
        Future.successful(Right(HomeMonitor.props(_, session,user)))
      case _ =>
        Future.successful(Left(Forbidden))
    }
  }

  case class BeaconRegistration(name : String)

  var beaconParser = Json.format[BeaconRegistration]

  def beacon = Secured(parse.json[BeaconRegistration](beaconParser)) { implicit request =>
    DAO.registerBeacon(request.body.name, request.user)
    API("success")
  }

  def jsRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("ClientAPIRouter")(
        routes.javascript.ReceiverAPI.monitorSocket,
        routes.javascript.ReceiverAPI.homeSocket
      )
    ).as("text/javascript")
  }

}
