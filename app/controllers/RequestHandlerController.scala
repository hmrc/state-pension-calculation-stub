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

import java.util.UUID

import javax.inject.Inject
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import repositories.TestDataRepository
import uk.gov.hmrc.play.bootstrap.controller.BackendController

import scala.concurrent.ExecutionContext.Implicits.global

class RequestHandlerController @Inject()(cc: ControllerComponents,
                                         repo: TestDataRepository)
  extends BackendController(cc) {

  def postRequestHandler(uri: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    repo.find("request" -> request.body, "uri" -> uri).map {
      case xs if xs.isEmpty =>
        NotFound(Json.obj("payload" -> request.body, "uri" -> uri))
      case head :: Nil =>
        val correlationId = request.headers.get("CorrelationId").getOrElse(UUID.randomUUID().toString)
        Status(head.status)(head.response).withHeaders("CorrelationId" -> correlationId)
      case xs =>
        InternalServerError(Json.obj("message" -> s"Found too many results. ${xs.size}"))
    }
  }
}
