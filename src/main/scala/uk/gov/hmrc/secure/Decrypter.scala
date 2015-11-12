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

import java.security.Key
import javax.crypto.Cipher
import java.nio.charset.StandardCharsets.UTF_8

trait Decrypter {

  protected val key: Key

  validateKey()

  protected def validateKey() {
    if (key == null) throw new IllegalStateException("There is no Key defined for this Decrypter")
  }

  def decrypt(data: String): String = decrypt(data, key.getAlgorithm)

  def decryptAsBytes(data: String): Array[Byte] = decryptAsBytes(data, key.getAlgorithm)

  def decrypt(data: String, algorithm: String): String = new String(decryptAsBytes(data, algorithm), UTF_8)

  def decryptAsBytes(data: String, algorithm: String): Array[Byte] = {
    try {
      val cipher: Cipher = Cipher.getInstance(algorithm)
      cipher.init(Cipher.DECRYPT_MODE, key, cipher.getParameters)
      cipher.doFinal(BasicBase64.decode(data))
    } catch {
      case e: Exception => throw new SecurityException("Failed decrypting data", e)
    }
  }

}
