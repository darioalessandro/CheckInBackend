package controllers

import model.clientAPI.API
import play.api.mvc.{Action, Controller}
import play.api.routing.JavaScriptReverseRouter

/**
  * Created by darioalessandro on 2/8/16.
  */
class LoginUI extends Controller {

  def login = Action { implicit request =>
    Ok(views.html.login())
  }

  def jsRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("LoginUIRouter")(
        controllers.routes.javascript.LoginUI.login
      )
    ).as("text/javascript")
  }

}
