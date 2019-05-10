/*
 * Copyright 2019 HM Revenue & Customs
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
import play.api.Logger
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import repositories.TestDataRepository
import uk.gov.hmrc.play.bootstrap.controller.BackendController

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class TestDataController @Inject()(cc: ControllerComponents,
                                   repo: TestDataRepository)
  extends BackendController(cc) {

  def insert(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[TestData] match {
      case JsSuccess(data, _) =>
        val query: JsObject = Json.obj("request" -> data.request, "uri" -> data.uri)
        repo.findAndUpdate(query, Json.toJson(data).as[JsObject], upsert = true).map(_ => NoContent)
      case JsError(errors) =>
        Logger.warn(s"Bad Request: $errors")
        Future.successful(BadRequest)
    }
  }

  def reset(): Action[AnyContent] = Action.async { implicit request =>
    repo.removeAll().map(_ => NoContent)
  }
}
