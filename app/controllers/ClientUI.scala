package controllers

import model.AuthInfo
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.api.routing.JavaScriptReverseRouter

/**
  * Created by darioalessandro on 2/8/16.
  */
class ClientUI extends Controller {

  def monitorUI = AuthInfo {  implicit request =>
    Ok(views.html.monitorUI())
  }

  def main = AuthInfo {  implicit request =>
    Ok(views.html.main(Json.toJson(request.user).toString()))
  }

  def wildcardIndex(wildcard : String) = AuthInfo {  implicit request =>
    Ok(views.html.index(Json.toJson(request.user).toString()))
  }

  def index = this.wildcardIndex("")

  def jsRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("ClientUIRouter")(
        controllers.routes.javascript.ClientUI.monitorUI,
        controllers.routes.javascript.ClientUI.main
      )
    ).as("text/javascript")
  }


}
