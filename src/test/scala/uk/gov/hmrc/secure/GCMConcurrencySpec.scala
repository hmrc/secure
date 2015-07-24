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

import java.util.concurrent.CountDownLatch

import org.scalatest.{Matchers, WordSpec}

class GCMConcurrencySpec extends WordSpec with Matchers {

  "GCM" should {

    "be thread safe" in {
      val latch = new CountDownLatch(1)

      val wrapper = new GCMEncrypterDecrypter("1234567890123456".getBytes, "additional".getBytes)

      for(i <- 0 to 500) {
        val t1 = new GCMEncrypterDecrypterThread(latch, i, wrapper)
        new Thread(t1).start()
      }

      latch.countDown() // inform all the threads to start.

    }

  }

  private class GCMEncrypterDecrypterThread(val latch: CountDownLatch, val Id: Int, val wrapper: GCMEncrypterDecrypter ) extends Runnable {

    override def run() {

      try {
        latch.await()
      } catch {
        case e: InterruptedException => e.printStackTrace()
      }

      val valueToEncrypt = "somedata"
      try {
        val response = wrapper.encrypt(valueToEncrypt.getBytes)
        val decrypt = wrapper.decrypt(response.getBytes)

        valueToEncrypt.getBytes shouldBe decrypt.getBytes
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }

  }

}
