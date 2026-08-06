/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.ui.pages

import org.openqa.selenium.{By, WebElement}
import org.scalatest.matchers.should.Matchers

object DataErrorsPage extends BasePage with Matchers {

  lazy val expectedErrors: Seq[String] =
    loadExpectedErrors()
  override val pageUrl: String         = baseUrl + "/problem/data-errors"

  def dataErrors(): this.type =
    onPage(pageUrl)

  def loadExpectedErrors(): Seq[String] = {
    val source = scala.io.Source.fromResource("expected-errors.txt")
    try
      source.getLines().map(_.trim).filter(_.nonEmpty).toSeq
    finally
      source.close()
  }

  def verifyAllErrors(): Unit = {

    val actual   = normalize(getActualErrors)
    val expected = normalize(expectedErrors)

    val missing    = expected.diff(actual)
    val unexpected = actual.diff(expected)

    withClue(
      s"""
         |Missing errors:
         |${missing.mkString("\n")}
         |
         |Unexpected errors:
         |${unexpected.mkString("\n")}
         """.stripMargin
    ) {
      actual should equal(expected)
    }
  }

  def normalize(errors: Seq[String]): Seq[String] =
    errors.map(_.trim)

  def getActualErrors: Seq[String] =
    driver
      .findElements(By.xpath("//*[contains(@id,'errorMessage')]"))
      .toArray
      .map(_.asInstanceOf[WebElement].getText.trim)
      .toSeq

}
