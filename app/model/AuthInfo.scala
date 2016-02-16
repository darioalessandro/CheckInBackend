package model

import model.C
import model.DAO
import model.DAO.{User, Session}
import play.api.mvc.{Result, ActionRefiner, ActionBuilder, WrappedRequest, Request}
import play.api.mvc.ActionFilter
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.mvc.RequestHeader



import scala.concurrent.Future

case class AuthenticatedInfo[A]( appSession : Option[Session], user: Option[User], request: Request[A]) extends WrappedRequest(request)

case class SecuredRequest[A](user:User,appSession : Session, request : Request[A]) extends WrappedRequest(request)


object AuthInfo extends ActionBuilder[AuthenticatedInfo] {

  def invokeBlock[A](request: Request[A], block: AuthenticatedInfo[A] => Future[Result]) = {

    val sessionDetails = for { headerSession <- request.session.get(C.sessionHeader)
                               serverSession <- DAO.sessionForSessionId(headerSession)
                               user <- DAO.userForSessionId(headerSession)
    } yield (serverSession,user)

    sessionDetails.map {
      case (s: Session, u: User) =>
        block(AuthenticatedInfo(Some(s), Some(u), request))
    }.getOrElse{
      block(AuthenticatedInfo(None, None, request))
    }
  }
}

object ValidateAuthentication extends ActionFilter[AuthenticatedInfo] {
  def filter[A](input: AuthenticatedInfo[A]) = Future.successful {
    implicit val r : RequestHeader = input
    if (input.appSession.isEmpty || input.user.isEmpty)
      Some(C.NoSession)
    else if(!input.user.get.active)
      Some(C.NoActiveAccount)
    else
      None
  }
}

object TransformToSecured extends ActionRefiner[AuthenticatedInfo, SecuredRequest] {
  override protected def refine[A](request : AuthenticatedInfo[A]) : scala.concurrent.Future[scala.Either[play.api.mvc.Result, SecuredRequest[A]]] = Future{
    Right(SecuredRequest(request.user.get,request.appSession.get,request.request))
  }
}






