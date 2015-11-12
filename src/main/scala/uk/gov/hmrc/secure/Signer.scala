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
import java.security._

import uk.gov.hmrc.secure.Algorithm._

class Signer(val privateKey: PrivateKey) {

  def sign(data: String, algorithm: Algorithm = SHA1withRSA): String = {
    try {
      val signature = Signature.getInstance(algorithm.value())
      signature.initSign(privateKey)
      signature.update(data.getBytes(UTF_8))
      BasicBase64.encodeToString(signature.sign)
    } catch {
      case nsae: NoSuchAlgorithmException => throw new SecurityException("Algorithm '" + algorithm.value() + "' is not supported", nsae)
      case ike: InvalidKeyException => throw new SecurityException("The private key is invalid", ike)
      case se: SignatureException => throw new SecurityException("Signature error", se)
    }
  }

}
