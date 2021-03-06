/*
 * Copyright 2021 HM Revenue & Customs
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

object Algorithm extends Enumeration {
  type Algorithm = Value

  val RSA_ECB_OAEPWithSHA1AndMGF1Padding = Value("RSA/ECB/OAEPWithSHA1AndMGF1Padding")
  val SHA1withRSA = Value("SHA1withRSA")

  class AlgorithmValue(algorithm: Value) {
    def value() = algorithm.toString
  }

  implicit def value2AlgorithmValue(algorithm: Value) = new AlgorithmValue(algorithm)

}
