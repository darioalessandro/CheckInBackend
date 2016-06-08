package model

import javax.mail.internet.InternetAddress

import akka.actor.Actor
import courier.{Text, Envelope, Mailer}
import courier._, Defaults._
import model.EmailSender.Email
import play.api.Play
import play.api.Play.current

/**
  * Responsible for sending all the email notifications.
  */

object EmailSender {
  case class Email(title : String, recipients:List[String], body: String)
}

class EmailSender extends Actor {

  val password = Play.application.configuration.getString("EmailSender.password").getOrElse("fuckyou")
  val email = Play.application.configuration.getString("EmailSender.email").getOrElse("fuckyou@fuckyou.com")


  val mailer = Mailer("smtp.gmail.com", 587)
    .auth(true)
    .as(email, password)
    .startTtls(true)()

  override def receive = {
    case Email(title : String, recipients:List[String], body: String) =>

      val recipientsAddresses : Seq[InternetAddress]  = recipients.map{recipient => new InternetAddress(recipient)}.toSeq

     // mailer(Envelope.from(new InternetAddress("checkinblenoreply@gmail.com"))
//        .to(recipientsAddresses:_*)
//        .subject(title)
//        .content(Text(body))).onSuccess {
//        case _ => println("message delivered")
//      }

  }

}
