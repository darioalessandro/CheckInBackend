package model

import java.text.SimpleDateFormat

import model.clientAPI.{API, APIError}
import play.api.i18n.Lang
import play.api.mvc.RequestHeader
import play.i18n.Messages

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by darioalessandrolencina@gmail.com on 9/10/14.
 */

object C {
  def dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a Z")
  def sessionHeader : String = "session"
  def invalidCredentials(implicit lang:Lang,  request: RequestHeader)= API(invalidCredentialsError)
  def BadRequest(implicit request: RequestHeader) = API(BadRequestError)
  def NoSession(implicit lang:Lang, request: RequestHeader) = API(NoSessionError)
  def NoActiveAccount(implicit lang:Lang, request: RequestHeader) = API(NoActiveAccountError)
  def NoUserForSession(implicit lang:Lang, request: RequestHeader) = API(NoUserForSessionError)
  def NoPermissions(implicit lang:Lang, request: RequestHeader) = API(NoPermissionsError)
  def noArtifactsFoundResult(criteria: Any)(implicit request: RequestHeader) = API(noArtifactsFound(criteria), logout = false)
  def fnoArtifactsFoundResult(criteria: Any)(implicit request: RequestHeader) = Future{noArtifactsFoundResult(criteria)}
  def noArtifactsFound(criteria: Any)(implicit request: RequestHeader) : Throwable = new Throwable(s"no artifacts found for criteria $criteria")
  def invalidCredentialsError(implicit lang:Lang, request: RequestHeader)= APIError("Invalid Credentials", logout = true)
  def BadRequestError(implicit request: RequestHeader) =  APIError("bad request", logout = false)
  def NoSessionError(implicit lang:Lang, request: RequestHeader) = APIError("Invalid Session",  logout =  true)
  def NoUserForSessionError(implicit lang:Lang, request: RequestHeader) = APIError("No User for Session",  logout = true)
  def NoPermissionsError(implicit lang:Lang, request: RequestHeader) = APIError("No permissions for Session",  logout = true)
  def NoActiveAccountError(implicit lang:Lang, request: RequestHeader) = APIError("Your account has been disabled, please contact the administrator.",  logout = true)
}
