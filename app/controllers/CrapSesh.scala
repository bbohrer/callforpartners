
package controllers

import java.time.{Duration, Instant}
import scala.util.Random

/**
  * This is most obviously something I should use a library for. Whatev.
  * Created by bbohrer on 9/25/16.
  */
object CrapSesh {
  var theSesh:Map[String, SeshRec] = Map()
  val ttl = Duration.ofHours(8)

  case class SeshRec(token:String, time:Instant)

  private def get(email:String):Option[SeshRec] = {
    theSesh.find({case (k,_) => k == email}).map(_._2)
  }

  def logout(email:String):Unit = {
    theSesh = theSesh.filter{case (k,_) => k != email}
  }
  def login(email:String):String = {
    val keyLen = 100
    val key = Random.alphanumeric take keyLen mkString ""
    theSesh = theSesh.+((email, SeshRec(key, Instant.now())))
    key
  }

  def stillValid(email:String,key:String): Boolean = {
    get(email) match {
      case None => false
      case Some(rec) =>
        val theDur = Duration.between(rec.time, Instant.now())
        if (rec.token != key) {
          false
        } else if (theDur.compareTo(ttl) > 0) {
          logout(email)
          false
        } else {
          logout(email)
          login(email)
          true
        }
    }
  }

}
