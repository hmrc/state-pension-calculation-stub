/*
 * Copyright 2023 HM Revenue & Customs
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

package models

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

class TestDataSpec extends AnyWordSpec with Matchers {

  "Parsing valid JSON to a TestData model" should {
    "result in a valid model" in {
      val json = Json.obj(
        "uri"      -> "some/url",
        "request"  -> Json.obj("a" -> 1),
        "status"   -> 1,
        "response" -> Json.obj("some" -> "data")
      )

      val model = TestData("some/url", Json.obj("a" -> 1), 1, Json.obj("some" -> "data"))

      json.as[TestData] shouldBe model
    }
  }

}
