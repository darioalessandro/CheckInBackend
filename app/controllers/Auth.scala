package controllers

import play.api.mvc.{Action, Controller}

/**
  * Created by darioalessandro on 2/1/16.
  */
class Auth extends Controller {

  def callback = Action { request =>
    println(request.queryString.mkString)
    Redirect("/auth/welcome")
  }

  def login = Action {
    Redirect(url = "http://localhost:9000", queryString = Map("client_id" -> Seq("1"), "scope" -> Seq("/")))
  }

  def welcome = Action {
    Ok("Welcome")
  }
}
