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
import java.security.Key
import javax.crypto.Cipher

trait Encrypter {

  protected val key: Key

  validateKey()

  protected def validateKey() {
    if (key == null) throw new IllegalStateException("There is no Key defined for this Encrypter")
  }

  def encrypt(data: Array[Byte]): String = encrypt(data, key.getAlgorithm)

  def encrypt(data: String): String = encrypt(data.getBytes(UTF_8), key.getAlgorithm)

  def encrypt(data: String, algorithm: String): String = encrypt(data.getBytes(UTF_8), algorithm)

  def encrypt(data: Array[Byte], algorithm: String): String = {
    try {
      val cipher: Cipher = Cipher.getInstance(algorithm)
      cipher.init(Cipher.ENCRYPT_MODE, key, cipher.getParameters)
      BasicBase64.encodeToString(cipher.doFinal(data))
    }
    catch {
      case e: Exception => throw new SecurityException("Failed encrypting data", e)
    }
  }

}
