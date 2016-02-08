package controllers

import play.api.Play
import play.api.mvc.{Action, Controller}
import play.api.Play.current

/**
  * Created by darioalessandro on 2/1/16.
  */
class Auth extends Controller {

  val authServerUrl = Play.application.configuration.getString("auth.url").get

  def callback = Action { request =>
    println(request.queryString.mkString)
    Redirect(controllers.routes.Auth.welcome().url)
  }

  def login = Action {
    Redirect(url = authServerUrl, queryString = Map("client_id" -> Seq("1"), "scope" -> Seq("/")))
  }

  def welcome = Action {
    Ok("Welcome")
  }
}
