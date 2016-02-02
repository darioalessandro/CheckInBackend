package model

import javax.mail.internet.InternetAddress

import akka.actor.Actor
import courier.{Text, Envelope, Mailer}
import courier._, Defaults._
import model.EmailSender.Email

/**
  * Responsible for sending all the email notifications.
  */

object EmailSender {
  case class Email(title : String, recipients:List[String], body: String)
}

class EmailSender extends Actor {

  val mailer = Mailer("smtp.gmail.com", 587)
    .auth(true)
    .as("checkinblenoreply@gmail.com", "N3wmill3nium")
    .startTtls(true)()

  override def receive = {
    case Email(title : String, recipients:List[String], body: String) =>

      val recipientsAddresses : Seq[InternetAddress]  = recipients.map{recipient => new InternetAddress(recipient)}.toSeq

      mailer(Envelope.from(new InternetAddress("checkinblenoreply@gmail.com"))
        .to(recipientsAddresses:_*)
        .subject(title)
        .content(Text(body))).onSuccess {
        case _ => println("message delivered")
      }

  }

}
