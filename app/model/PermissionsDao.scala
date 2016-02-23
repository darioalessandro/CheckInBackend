package model

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
  * Created by darioalessandro on 2/22/16.
  */

case class Permissions(username : String, engineering : Boolean, home : Boolean, daycare : Boolean)

class PermissionsDao {
  def permissionsForUser(username : String) : Option[Permissions] =
    DB.withConnection("authorization") { implicit c =>
      SQL(
        """
          select * from permissions where username={username}
        """
      ).on('username -> username)().collect {
        case r => createPermissionsFromDB(r)
      }.headOption
    }

  def createPermissionsFromDB(r  : Row) : Permissions =
    Permissions(r[String]("username"),
      r[Boolean]("engineering"),
      r[Boolean]("home"),
      r[Boolean]("daycare"))


}
