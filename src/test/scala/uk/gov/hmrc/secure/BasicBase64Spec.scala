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

import org.scalatest.{Matchers, WordSpec}
import java.nio.charset.StandardCharsets.UTF_8

class BasicBase64Spec extends WordSpec with Matchers {

  import BasicBase64._

  "Base64" should {
    "encoded string should be different to the source string" in {
      encodeString("0123456789") should not be "0123456789"
    }

    "round trip should return the original" in {
      decodeString(encodeString("0123456789")) shouldBe "0123456789"
      decodeString(encode("0123456789")) shouldBe "0123456789"
      decodeString(encode("0123456789".getBytes(UTF_8))) shouldBe "0123456789"
    }
  }
}
