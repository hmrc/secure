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

/**
 * Simple wrapper for java.util.Base64 basic mode. MIME and URL codecs are not supported.
 * Transcodings always use UTF-8 strings.
 */
object BasicBase64 {
  def encode(string: String): Array[Byte] = encode(string.getBytes(UTF_8))

  def encode(bytes: Array[Byte]): Array[Byte] = java.util.Base64.getEncoder.encode(bytes)

  def encodeString(string: String) = new String(encode(string), UTF_8)

  def encodeString(bytes: Array[Byte]) = new String(encode(bytes), UTF_8)

  def decode(string: String): Array[Byte] = decode(string.getBytes(UTF_8))

  def decode(bytes: Array[Byte]): Array[Byte] = java.util.Base64.getDecoder.decode(bytes)

  def decodeString(string: String) = new String(decode(string), UTF_8)

  def decodeString(bytes: Array[Byte]) = new String(decode(bytes), UTF_8)
}
