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

package controllers

import javax.inject.{Inject, Singleton}
import models.TestData
import play.api.Logging
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import repositories.TestDataRepository
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import org.mongodb.scala.model.Filters.{and, empty, equal}
import org.mongodb.scala.model.UpdateOptions
import org.mongodb.scala.model.Updates.set
import uk.gov.hmrc.mongo.play.json.Codecs
import org.mongodb.scala.model.Updates._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TestDataController @Inject() (cc: ControllerComponents, repo: TestDataRepository)(implicit ec: ExecutionContext)
    extends BackendController(cc)
    with Logging {

  def insert(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[TestData] match {
      case JsSuccess(data, _) =>
        repo.collection
          .updateOne(
            filter = and(equal("request", Codecs.toBson(data.request)), equal("uri", data.uri)),
            update = combine(
              set("status", data.status),
              set("response", Codecs.toBson(data.response))
            ),
            UpdateOptions().upsert(true)
          )
          .toFuture()
          .map(_ => NoContent)
      case JsError(errors) =>
        logger.warn(s"Bad Request: $errors")
        Future.successful(BadRequest)
    }
  }

  def reset(): Action[AnyContent] = Action.async {
    repo.collection.deleteMany(empty()).toFuture().map(_ => NoContent)
  }

}
