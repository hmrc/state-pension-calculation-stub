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

import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "state-pension-calculation-stub"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    majorVersion := 0,
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    dependencyOverrides ++= AppDependencies.overrides
  )
  .settings(publishingSettings: _*)
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(CodeCoverageSettings.settings: _*)
  .settings(scalaVersion := "2.12.12")

// Dependencies upgrades that have been reviewed (16th April 2019) and discounted
dependencyUpdatesFilter -= moduleFilter(organization = "org.scala-lang")
dependencyUpdatesFilter -= moduleFilter(organization = "com.typesafe.play", revision = "2.7.*")
dependencyUpdatesFilter -= moduleFilter(organization = "com.typesafe.play", name = "twirl-api")
dependencyUpdatesFilter -= moduleFilter(organization = "org.scalatestplus.play", revision = "4.*")

// ***************
// Use the silencer plugin to suppress warnings from unused imports in compiled twirl templates
scalacOptions += "-P:silencer:pathFilters=routes"
scalacOptions += "-P:silencer:lineContentFilters=^\\w"
libraryDependencies ++= Seq(
  compilerPlugin("com.github.ghik" % "silencer-plugin" % "1.7.1" cross CrossVersion.full),
  "com.github.ghik" % "silencer-lib" % "1.7.1" % Provided cross CrossVersion.full
)
// ***************
