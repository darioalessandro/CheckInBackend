package controllers

import model.{C, DAO}
import model.DAO.User
import model.clientAPI.API
import play.api.libs.json.Json
import play.api.mvc.BodyParsers.parse
import play.api.mvc.{Action, Controller}
import play.api.routing.JavaScriptReverseRouter

/**
  * Created by darioalessandro on 2/8/16.
  */
class LoginAPI extends Controller {

  case class LoginData(username : String, password : String)

  implicit val loginParser = Json.format[LoginData]

  def login = Action(parse.json[LoginData]) { implicit request =>

    val loginRequest = request.body
    DAO.userWithUsernameAndPassword(loginRequest.username, loginRequest.password) match {
      case Some(user: User) =>
        if (user.active) {
          val session = DAO.createSessionForUser(user)
          API(user).withSession(C.sessionHeader -> session)
        } else {
          C.NoActiveAccount
        }
      case None =>
        API("Invalid credentials", logout = true)
    }
  }

  def logout = Action { implicit request =>
      request.session.get(C.sessionHeader) match {
        case Some(session) =>
          DAO.closeSession(session)
        case None =>
      }
    API(Json.toJson("bye")).withNewSession
  }


  def jsRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("LoginAPIRouter")(
        controllers.routes.javascript.LoginAPI.login,
        controllers.routes.javascript.LoginAPI.logout
      )
    ).as("text/javascript")
  }

}
