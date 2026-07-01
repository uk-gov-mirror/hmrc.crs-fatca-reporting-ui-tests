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

import org.openqa.selenium.By
import org.scalactic.Prettifier.default
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.*

import scala.io.Source
import scala.jdk.CollectionConverters.*

object RulesErrorsPage extends BasePage with Matchers {

  override val pageUrl: String = baseUrl + "/problem/rules-errors"

  private val errorColumnSelector = By.xpath("//*[contains(@id,'errorMessage_')]")

  def readAllTheRulesErrors(): Unit = {
    val expectedErrors = loadExpectedErrors()
    val actualCells    = driver.findElements(errorColumnSelector).asScala.toSeq

    withClue(
      s"Row count mismatch - UI has ${actualCells.size} row but rules-errors.json has ${expectedErrors.size}: "
    ) {
      actualCells.size shouldBe expectedErrors.size
    }

    actualCells.zip(expectedErrors).zipWithIndex.foreach { case ((cell, expected), index) =>
      val actual    = cell.getText.trim
      val errorType = (expected \ "errorType").as[String]

      errorType match {
        case "simple"               =>
          assertSimpleErrors(actual, (expected \ "error").as[String], index + 1)
        case "complex"              =>
          assertComplexErrors(actual, (expected \ "error").as[JsObject], index + 1)
        case "mixed"                =>
          assertMixedErrors(actual, (expected \ "error").as[JsObject], index + 1)
        case "paragraphs"           =>
          assertParagraphErrors(actual, (expected \ "error").as[JsObject], index + 1)
        case "paragraphWithBullets" =>
          assertParagraphWithBulletsErrors(actual, (expected \ "error").as[JsObject], index + 1)
        case other                  =>
          fail(s"Row ${index + 1} unknown errorType '$other' in rules-errors.json")
      }
    }
  }

  private def loadExpectedErrors(): Seq[JsValue] =
    Json
      .parse(Source.fromResource("rules-errors.json").mkString)
      .as[JsArray]
      .value
      .toSeq

  private def assertComplexErrors(actual: String, error: JsObject, row: Int): Unit = {
    val bodyForbidden  = (error \ "bodyForbidden").as[Seq[String]]
    val refIdForbidden = (error \ "refIdForbidden").as[Seq[String]]
    val replacements   = (error \ "replacements").as[Map[String, String]]

    withClue(s"Row $row - missing section header 'CrsBody must not contain:'") {
      normalize(actual) should include(normalize("CrsBody must not contain:"))
    }
    bodyForbidden.foreach { phrase =>
      withClue(s"Row $row - missing bodyForbidden entry '$phrase': ") {
        normalize(actual) should include(normalize(phrase))
      }
    }

    withClue(s"Row $row - missing section header 'MessageRefId and DocRefId must not contain:'") {
      normalize(actual) should include(normalize("MessageRefId and DocRefId must not contain:"))
    }
    refIdForbidden.foreach { phrase =>
      withClue(s"Row $row - missing refIdForbidden entry '$phrase': ") {
        normalize(actual) should include(normalize(phrase))
      }
    }
    withClue(s"Row $row - missing section header 'If elsewhere in the CrsBody, replace:'") {
      normalize(actual) should include(normalize("If elsewhere in the CrsBody, replace:"))

    }
    replacements.foreach { case (char, replacement) =>
      withClue(s"Row $row - missing replacement '$char with $replacement': ") {
        normalize(actual) should include(normalize(s"$char with $replacement"))
      }
    }
  }

  private def normalize(text: String): String =
    text
      .replace("\u2018", "'")
      .replace("\u2019", "'")
      .replace("\u201C", "\"")
      .replace("\u201D", "\"")
      .trim

  private def assertSimpleErrors(actual: String, expected: String, row: Int): Unit =
    withClue(s"Row $row - simple error mismatch: ") {
      actual shouldBe expected
    }

  private def assertMixedErrors(actual: String, error: JsObject, row: Int): Unit = {
    val intro       = (error \ "intro").as[String]
    val mustContain = (error \ "mustContain").as[Seq[String]]
    val closingRule = (error \ "closingRule").as[String]

    withClue(s"Row $row - missing intro text: ") {
      normalize(actual) should include(normalize(intro))
    }
    mustContain.foreach { item =>
      withClue(s"Row $row - missing mustContain item '$item': ") {
        normalize(actual) should include(normalize(item))
      }
    }
    withClue(s"Row $row - missing closing rule text: ") {
      normalize(actual) should include(normalize(closingRule))
    }
  }

  private def assertParagraphErrors(actual: String, error: JsObject, row: Int): Unit = {
    val paragraphs = (error \ "paragraphs").as[Seq[String]]

    paragraphs.foreach { paragraph =>
      withClue(s"Row $row - missing paragraph '$paragraph': ") {
        normalize(actual) should include(normalize(paragraph))
      }
    }
  }

  private def assertParagraphWithBulletsErrors(actual: String, error: JsObject, row: Int): Unit = {
    val sections = (error \ "sections").asOpt[Seq[JsObject]]

    sections match {
      case Some(sectionList) =>
        sectionList.zipWithIndex.foreach { case (section, sectionIndex) =>
          val intro   = (section \ "intro").as[String]
          val bullets = (section \ "bullets").as[Seq[String]]

          withClue(s"Row $row section ${sectionIndex + 1} - missing intro paragraph '$intro': ") {
            normalize(actual) should include(normalize(intro))
          }
          bullets.foreach { bulletPoints =>
            withClue(s"Row $row section ${sectionIndex + 1} - missing bullet points '$bulletPoints': ") {
              normalize(actual) should include(normalize(bulletPoints))
            }
          }
        }
      case None              =>
        val introJson = (error \ "intro").get
        val intros    = introJson match {
          case JsString(text)  => Seq(text)
          case JsArray(values) => values.map(_.as[String]).toSeq
          case _               => fail(s"Row $row - 'intro' must be a string or array")
        }
        val bullets   = (error \ "bullets").as[Seq[String]]

        intros.foreach { intro =>
          withClue(s"Row $row - missing intro paragraph '$intro': ") {
            normalize(actual) should include(normalize(intro))
          }
        }

        bullets.foreach { bulletPoints =>
          withClue(s"Row $row - missing bullet points '$bulletPoints': ") {
            normalize(actual) should include(normalize(bulletPoints))
          }
        }
    }
  }

  def readAllTheFatcaRulesErrors(): Unit = {
    val expectedErrors = loadFatcaExpectedErrors()
    val actualCells    = driver.findElements(errorColumnSelector).asScala.toSeq

    withClue(
      s"Row count mismatch - UI has ${actualCells.size} row but fatca-rules-errors.json has ${expectedErrors.size}: "
    ) {
      actualCells.size shouldBe expectedErrors.size
    }

    actualCells.zip(expectedErrors).zipWithIndex.foreach { case ((cell, expected), index) =>
      val actual    = cell.getText.trim
      val errorType = (expected \ "errorType").as[String]

      errorType match {
        case "simple"               =>
          assertSimpleErrors(actual, (expected \ "error").as[String], index + 1)
        case "complex"              =>
          assertFatcaComplexErrors(actual, (expected \ "error").as[JsObject], index + 1)
        case "mixed"                =>
          assertMixedErrors(actual, (expected \ "error").as[JsObject], index + 1)
        case "paragraphs"           =>
          assertParagraphErrors(actual, (expected \ "error").as[JsObject], index + 1)
        case "paragraphWithBullets" =>
          assertParagraphWithBulletsErrors(actual, (expected \ "error").as[JsObject], index + 1)
        case other                  =>
          fail(s"Row ${index + 1} unknown errorType '$other' in fatca-rules-errors.json")
      }
    }
  }

  private def loadFatcaExpectedErrors(): Seq[JsValue] =
    Json
      .parse(Source.fromResource("fatca-rules-errors.json").mkString)
      .as[JsArray]
      .value
      .toSeq

  private def assertFatcaComplexErrors(actual: String, error: JsObject, row: Int): Unit = {
    val bodyForbidden  = (error \ "bodyForbidden").as[Seq[String]]
    val refIdForbidden = (error \ "refIdForbidden").as[Seq[String]]
    val replacements   = (error \ "replacements").as[Map[String, String]]

    withClue(s"Row $row - missing section header 'The FATCA element must not contain:'") {
      normalize(actual) should include(normalize("The FATCA element must not contain:"))
    }
    bodyForbidden.foreach { phrase =>
      withClue(s"Row $row - missing bodyForbidden entry '$phrase': ") {
        normalize(actual) should include(normalize(phrase))
      }
    }

    withClue(s"Row $row - missing section header 'MessageRefId and DocRefId must not contain:'") {
      normalize(actual) should include(normalize("MessageRefId and DocRefId must not contain:"))
    }
    refIdForbidden.foreach { phrase =>
      withClue(s"Row $row - missing refIdForbidden entry '$phrase': ") {
        normalize(actual) should include(normalize(phrase))
      }
    }
    withClue(s"Row $row - missing section header 'If elsewhere in the file, replace:'") {
      normalize(actual) should include(normalize("If elsewhere in the file, replace:"))

    }
    replacements.foreach { case (char, replacement) =>
      withClue(s"Row $row - missing replacement '$char with $replacement': ") {
        normalize(actual) should include(normalize(s"$char with $replacement"))
      }
    }
  }

}
