package controllers

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

    request.body match {
      case LoginData("dario", "dario") =>
        API("OK")

      case LoginData("carmen", "compositetech") =>
        API("OK")

      case LoginData("candace", "compositetech") =>
        API("OK")

      case LoginData(_, _) =>
        API("Invalid credentials", logout = true)

    }
  }

  def jsRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("LoginAPIRouter")(
        controllers.routes.javascript.LoginAPI.login
      )
    ).as("text/javascript")
  }

}
