/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.secure

import java.nio.charset.StandardCharsets.UTF_8
import java.security.{GeneralSecurityException, SecureRandom}
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

trait Hasher {
  def hash(plain: String): Scrambled
}

/**
 * Provides a simple API for PBKDF2 password hashing. These algorithms are deliberately slow,
 * providing protection against brute force attacks.
 *
 * Larger values for pbkdf2Iterations will cost more CPU to evaluate; this is deliberate.
 */
class PasswordHash(hashByteSize: Int, pbkdf2Iterations: Int) {

  /** Constructs an instance with default parameters. */
  def this() = this(24, 100)

  require(hashByteSize > 0)
  require(pbkdf2Iterations > 0)

  val PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1"

  // "PDBKDF2WithHmacSHA512" has a longer SHA hash, but is not necessarily better than just having more iterations
  // val PBKDF2_ALGORITHM: String = "PDBKDF2WithHmacSHA512"

  /**
   * Creates a hash using a specified salt. For a given source string, subsequent calls to this method will
   * yield the same result provided the same salt is used.
   * The result is a base64-encoded string.
   */
  @throws(classOf[GeneralSecurityException])
  protected def hashWithSalt(salt: Salt, password: String): Scrambled = {
    val bytes = pbkdf2(password.toCharArray, BasicBase64.decode(salt.value))
    Scrambled(BasicBase64.encodeToString(bytes))
  }

  /** Gets a Hasher for the current PasswordHash. */
  def withSalt(salt: Salt): Hasher = {
    new Hasher {
      /** Creates a hash. The result is a base64-encoded string. */
      @throws(classOf[GeneralSecurityException])
      override def hash(plain: String): Scrambled = hashWithSalt(salt, plain)
    }
  }

  @throws(classOf[GeneralSecurityException])
  private def pbkdf2(password: Array[Char], salt: Array[Byte]): Array[Byte] = {
    val spec = new PBEKeySpec(password, salt, pbkdf2Iterations, hashByteSize * 8)
    val skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM)
    skf.generateSecret(spec).getEncoded
  }
}


object PasswordHash {
  /**
   * Creates a random 24-byte salt. Repeated calls to this method will yield different results.
   * The result is a base64-encoded string.
   */
  @throws(classOf[GeneralSecurityException])
  def newRandomSalt: Salt = newRandomSalt(24)

  /**
   * Creates a random salt. Repeated calls to this method will yield different results.
   * The result is a base64-encoded string.
   */
  @throws(classOf[GeneralSecurityException])
  def newRandomSalt(saltByteSize: Int): Salt = {
    require(saltByteSize > 0)
    val random = new SecureRandom
    val bytes = new Array[Byte](saltByteSize)
    random.nextBytes(bytes)
    Salt(BasicBase64.encodeToString(bytes))
  }

  /**
   * Compares two hashed strings in length-constant time. This comparison method
   * is used so that password hashes cannot be extracted from an on-line
   * system using a timing attack and then attacked off-line.
   */
  def slowEquals(a: String, b: String): Boolean = {
    slowEquals(a.getBytes(UTF_8), b.getBytes(UTF_8))
  }

  /**
   * Compares two byte arrays in length-constant time. This comparison method
   * is used so that password hashes cannot be extracted from an on-line
   * system using a timing attack and then attacked off-line.
   */
  def slowEquals(a: Array[Byte], b: Array[Byte]): Boolean = {
    var diff = a.length ^ b.length
    var i = 0
    while (i < a.length && i < b.length) {
      diff |= a(i) ^ b(i)
      i += 1
    }

    diff == 0
  }

  /**
   * Allows easy generation of hashes.
   */
  def main(args: Array[String]) {
    if (args.length < 2) {
      println("Usage: PasswordHash salt password [hash-bytes] [iterations]")
      println("If salt is '?', a new salt is generated, printed out amd then used for the password hash.")
      System.exit(0)
    }

    val hashBytes = if (args.length > 2) args(2).toInt else 24
    val iterations = if (args.length > 3) args(3).toInt else 100

    val gen = new PasswordHash(hashBytes, iterations)
    val salt =
      if (args(0) != "?") Salt(args(0))
      else {
        val s = newRandomSalt
        println("Salt: " + s)
        s
      }
    val password = args(1)
    println(gen.withSalt(salt).hash(password))
  }
}

case class Scrambled(value: String)

case class Salt(value: String)
