package controllers

import model.{TransformToSecured, ValidateAuthentication, AuthInfo}
import play.api.mvc.{Action, Controller}


/**
 * Created by darioalessandrolencina@gmail.com on 5/6/15.
 */

class Secured extends Controller {

  val Secured = AuthInfo andThen ValidateAuthentication andThen TransformToSecured

}
