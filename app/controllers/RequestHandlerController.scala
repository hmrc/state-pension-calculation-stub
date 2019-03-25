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

import javax.inject.Inject
import models.TestDataRequest
import play.api.Logger
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import repositories.TestDataRepository
import uk.gov.hmrc.play.bootstrap.controller.BackendController

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RequestHandlerController @Inject()(cc: ControllerComponents,
                                         repo: TestDataRepository)
  extends BackendController(cc) {

  def postRequestHandler(uri: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[TestDataRequest] match {
      case JsSuccess(data, _) =>
        repo.find("request" -> data.request).map {
          case xs if xs.isEmpty =>
            NotFound(Json.obj(
              "message" -> "Could not find data in the stub matching the payload",
              "payload" -> data.request))
          case head :: Nil =>
            Status(head.status)(head.response)
          case xs =>
            InternalServerError(Json.obj("message" -> s"Found too many results. ${xs.size}"))
        }
      case JsError(errors) =>
        Logger.warn(s"Bad Request: $errors")
        Future.successful(BadRequest(Json.obj()))
    }
  }
}
