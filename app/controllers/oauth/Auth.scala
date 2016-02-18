package controllers.oauth

import play.api.Play
import play.api.Play.current
import play.api.mvc.{Action, Controller}

/**
  * Created by darioalessandro on 2/1/16.
  */
class Auth extends Controller {

  val authServerUrl = Play.application.configuration.getString("auth.url").get

  def callback = Action { request =>
    println(request.queryString.mkString)
    Ok("")
    //Redirect(controllers.oauth.routes.Auth.welcome().url)
  }

  def login = Action {
    Redirect(url = authServerUrl, queryString = Map("client_id" -> Seq("1"), "scope" -> Seq("/")))
  }

  def welcome = Action {
    Ok("Welcome")
  }
}
