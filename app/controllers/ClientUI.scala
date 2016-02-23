package controllers

import model.{PermissionsDao, Permissions, AuthInfo}
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

  def homeUI = AuthInfo {  implicit request =>
    Ok(views.html.homeUI())
  }

  implicit val permissionsParser = Json.format[Permissions]

  def main = AuthInfo {  implicit request =>

    val userString = Json.toJson(request.user).toString()

    val permissions = request.user.flatMap(u => new PermissionsDao().permissionsForUser(u.username))

    Ok(views.html.main(userString, Json.toJson(permissions).toString()))
  }

  def wildcardIndex(wildcard : String) = AuthInfo {  implicit request =>
    Ok(views.html.index(Json.toJson(request.user).toString()))
  }

  def index = this.wildcardIndex("")

  def nativeIndex = AuthInfo {
    implicit request =>
      Ok(views.html.native.index(Json.toJson(request.user).toString()))
  }

  def jsRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("ClientUIRouter")(
        controllers.routes.javascript.ClientUI.monitorUI,
        controllers.routes.javascript.ClientUI.main,
        controllers.routes.javascript.ClientUI.homeUI
      )
    ).as("text/javascript")
  }


}
