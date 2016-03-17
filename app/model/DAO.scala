package model

import java.util.UUID

import anorm._
import model.MD5
import play.api.Play.current
import play.api.db.DB
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

/**
 * Created by darioalessandrolencina@gmail.com on 8/18/14.
 */

object DAO {

  case class User(username: String,
                  password: Option[String],
                  firstName:String,
                  lastName:String,
                  active:Boolean){
    def toJsonString : String = Json.toJson(this).toString()
  }

  implicit val userWrites = new Writes[User] {
    def writes(m: User): JsValue = Json.obj(
      "username" -> m.username,
      "firstName" -> m.firstName,
      "lastName" -> m.lastName,
      "active" -> m.active
    )
  }

  case class Session(username : String,
                     session : String,
                     active : Boolean)

  def createSessionFromDB(r : Row) : Session = {
    Session(r[String]("username"),
      r[String]("session"),
      r[Boolean]("active"))
  }

  def createUserFromDBWithoutPassword(r : Row) : User = {
    User(
      r[String]("username"),
      None,
      r[String]("firstName"),
      r[String]("lastName"),
      r[Boolean]("active"))
  }

  def createUserFromDB(r : Row) : User = {
    User(
      r[String]("username"),
      r[Option[String]]("password"),
      r[String]("firstName"),
      r[String]("lastName"),
      r[Boolean]("active"))
  }

  def createSessionForUser(user : User) : String = {
    val session= UUID.randomUUID().toString
    DB.withConnection("authorization") { implicit c =>
      SQL(
        """
          INSERT INTO sessions (username, session, active) VALUES ({username}, {session}, {active});
      """
      ).on('username -> user.username, 'session -> session, 'active -> true).executeInsert()
    }
    session
  }

  def sessionForSessionId(session : String) : Option[Session] =
    DB.withConnection("authorization") { implicit c =>
      SQL(
        """
          select * from sessions where session={session}
        """
      ).on('session -> session)().collect {
        case r => createSessionFromDB(r)
      }.headOption
  }

  def userWithId(username:String) = {
    DB.withConnection("authorization") { implicit c =>

      SQL(
        """
            select * from users where username={username}
      """
    ).
        on('username -> username)().collect {
      case r =>
        createUserFromDB(r)
    }.headOption
    }
  }

  def userForSessionId(session : String) : Option[User] = {
    DB.withConnection("authorization") { implicit c =>
      sessionForSessionId(session) match {
        case Some(sessionObj) =>
          userWithId(sessionObj.username)
        case None =>
          None
      }
    }
  }

  def closeSession(session : String) : Unit = {
    DB.withConnection("authorization") ( implicit c =>
      SQL(
      """
        UPDATE sessions s set s.active={active} where s.session={session};
      """
      ).on('session -> session, 'active -> false).executeInsert()
    )
  }

  def userWithUsernameAndPassword(username:String, password:String) : Option[User] =
    DB.withConnection("authorization") { implicit c =>
      SQL(
      """
        select * from users where username={username} and password = {password}
      """
      ).on('username -> username, 'password -> MD5.MD5Encode(password).toLowerCase)().collect {
      case r =>
        createUserFromDB(r)
      }.headOption
    }

  def registerBeacon(name : String, user : User): Unit = {
    val session= UUID.randomUUID().toString
    DB.withConnection("authorization") { implicit c =>
      SQL(
        """
          INSERT INTO beacons (username, uuid, name) VALUES ({username}, {uuid}, {name});
        """
      ).on('username -> user.username, 'session -> session, 'active -> true).executeInsert()
    }
  }

  def updateUserActive(user : User) : Int = {
    DB.withConnection("authorization") { implicit connection =>
      SQL(
        """
        update users
          set active = {active}
          where username = {username}
      """).on(
          'active -> user.active,
          'username -> user.username
        ).executeUpdate()
      }
  }

  def allUsers : List[User] = {
    DB.withConnection("authorization") { implicit connection =>
      val users= SQL(
        """
            SELECT * FROM users
        """
      )().collect {
        case r=>
          DAO.createUserFromDBWithoutPassword(r)
      }
      users.toList
    }
  }

}
