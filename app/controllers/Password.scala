package controllers

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.{PBEKeySpec}
import javax.xml.bind.DatatypeConverter

/**
  * Password generation and checking using PBKDF2. Based on security advice from OWASP web security project.
  * @see www.owasp.org
  * Created by bbohrer on 12/29/15.
  */
object Password {
  /* Make a basic effort to confound timing attacks based on short-circuiting string comparisons. This is the
   * recommended algorithm for comparing strings in a way that will never short-circuit, regardless of compiler
   * optimizations. */
  def safeEquals(str1: String, str2: String): Boolean = {
    if(str1.length != str2.length)
      return false

    var acc = 0
    for(i <- str1.indices) {
      acc |= str1(i) ^ str2(i)
    }
    acc == 0
  }

  /* TODO: This was a hilarious hack to get around SQLite being a jerk. Might not be necessary with Postgres. */
  def sanitize(s:Array[Byte]): String = {
    DatatypeConverter.printBase64Binary(s)
  }

  def hash(password: Array[Char], salt: Array[Byte], iterations: Int): String = {
    val spec = new PBEKeySpec(password, salt, iterations, Math.min(160, salt.length*8))
    val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    sanitize(skf.generateSecret(spec).getEncoded)
  }

  def generateSalt(length: Int): String = {
    val saltBuf = new Array[Byte] (length)
    val rng = new SecureRandom()
    rng.nextBytes(saltBuf)
    sanitize(saltBuf)
  }

  def generateKey(password: String, iterations: Int, saltLength: Int): (String, String) = {
    val salt = generateSalt(saltLength)
    val hash = this.hash(password.toCharArray, salt.getBytes("UTF-8"), iterations)
    (hash, salt)
  }
}
