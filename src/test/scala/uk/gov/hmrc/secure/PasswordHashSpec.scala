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

import org.scalatest.WordSpec

class PasswordHashSpec extends WordSpec {

  "slowEquals" should {
    "For a variety of strings, slowEquals should return true when both parameters are the same" in {
      import PasswordHash._
      assert(slowEquals("", "") === true)
      assert(slowEquals("a", "a") === true)
      assert(slowEquals("A Much Larger String Containing µø¢ etc", "A Much Larger String Containing µø¢ etc") === true)
    }
    "For some similar but different strings, slowEquals should return false" in {
      import PasswordHash._
      assert(slowEquals("", "1") === false)
      assert(slowEquals("a", "b") === false)
      assert(slowEquals("A Much Larger String Containing ¢µø etc", "A Much Larger String Containing µø¢ etc") === false)
    }
  }

  "newRandomSalt" should {
    "create random salts with a different output each time it is called" in {
      val s1 = PasswordHash.newRandomSalt(10)
      val s2 = PasswordHash.newRandomSalt(10)
      val s3 = PasswordHash.newRandomSalt(10)
      assert(s1 !== "")
      assert(s1 !== s2)
      assert(s3 !== s2)
      assert(s3 !== s1)
      assert(s1.value.length === 16)
    }

    "deal with small parameter values ok" in {
      val s1 = PasswordHash.newRandomSalt(1)
      assert(s1.value.length === 4)
      assert(s1 !== "foo")
    }

    "create random-salted hash with length determined by the parameter" in {
      val s1 = PasswordHash.newRandomSalt(12)
      assert(s1.value.length === 16)

      val s2 = PasswordHash.newRandomSalt(24)
      assert(s2.value.length === 32)
    }
  }

  "hash" should {
    "create consistent hashes using a given salt" in {
      val gen = new PasswordHash(1, 1)
      val s1 = gen.withSalt(Salt("salt")).hash("foo")
      val s2 = gen.withSalt(Salt("salt")).hash("foo")
      val s3 = gen.withSalt(Salt("salt")).hash("foo")
      assert(s1 !== "foo")
      assert(s1 === s2)
      assert(s1 === s3)
    }

    "create specific-salted hash with length determined by the constructor parameter" in {
      val gen1 = new PasswordHash(12, 5)
      val s1a = gen1.withSalt(Salt("salt")).hash("foo")
      val s1b = gen1.withSalt(Salt("salt")).hash("foo")
      assert(s1a.value.length === 16)
      assert(s1a !== "foo")

      val gen2 = new PasswordHash(24, 5)
      val s2 = gen2.withSalt(Salt("salt")).hash("foo")
      assert(s2.value.length === 32)
      assert(s2 !== "foo")
    }

    "deal with small constructor parameter values ok" in {
      val gen2 = new PasswordHash(1, 1)
      val s2 = gen2.withSalt(Salt("salt")).hash("foo")
      assert(s2.value.length === 4)
      assert(s2 !== "foo")
    }
  }
}
