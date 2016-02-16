package model

import java.security.MessageDigest

/**
 * Created by darioalessandrolencina@gmail.com on 2/3/15.
 */
object MD5 {

  def MD5Encode(original : String) : String = {
    val md = MessageDigest.getInstance("MD5")
    md.update(original.getBytes)
    val digest = md.digest()
    val sb = digest map("%02X" format _)
    sb.mkString
  }
}
