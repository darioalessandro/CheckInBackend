package controllers

import play.api.mvc.{Action, Controller}
import play.api.routing.JavaScriptReverseRouter

/**
  * Created by darioalessandro on 2/8/16.
  */
class ClientUI extends Controller {

  def monitorUI = Action {
    Ok(views.html.monitorUI())
  }

  def wildcardIndex(wildcard : String) = Action {
    Ok(views.html.index())
  }

  def index = Action {
    Ok(views.html.index())
  }

  def jsRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("ClientUIRouter")(
        controllers.routes.javascript.ClientUI.monitorUI
      )
    ).as("text/javascript")
  }


}
