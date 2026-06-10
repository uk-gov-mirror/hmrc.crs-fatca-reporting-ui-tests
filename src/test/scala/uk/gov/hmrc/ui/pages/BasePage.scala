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

package uk.gov.hmrc.ui.pages

import org.openqa.selenium.*
import org.openqa.selenium.support.ui.{ExpectedConditions, FluentWait, Wait}
import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers
import uk.gov.hmrc.selenium.component.PageObject
import uk.gov.hmrc.selenium.webdriver.Driver
import uk.gov.hmrc.ui.conf.TestConfiguration
import uk.gov.hmrc.ui.driver.BrowserDriver
import uk.gov.hmrc.ui.pages.FileNotAcceptedPage.driver
import uk.gov.hmrc.ui.utils.IdGenerators

import java.time.Duration

trait BasePage extends BrowserDriver with Matchers with IdGenerators with PageObject {

  val pageUrl: String
  val baseUrl: String             = TestConfiguration.url("crs-fatca-reporting-frontend") + "/report"
  val baseUrlFi: String           = TestConfiguration.url("crs-fatca-financial-institutions")
  val baseUrlManualSub: String    = TestConfiguration.url("crs-fatca-manual-submission-frontend")
  val submitButtonId: By          = By.id("submit")
  val backLinkText: By            = By.linkText("Back")
  val pageHeader: By              = By.tagName("h1")
  val fileUploadId: By            = By.id("file-upload")
  val yesRadioId: By              = By.id("value")
  val noRadioId: By               = By.id("value-no")
  val backToManageReportsLink: By = By.partialLinkText("Back to manage your CRS and FATCA reports")
  val manageFiPage: By            = By.linkText("Send a CRS or FATCA report")

  def clickOnBackLink(): Unit = {
    onPage()
    click(backLinkText)
  }

  def submitPage(): this.type = {
    onPage(pageUrl)
    click(submitButtonId)
    this
  }

  def onPage(url: String = this.pageUrl): this.type = {
    fluentWait.until(ExpectedConditions.urlToBe(url))
    this
  }

  private def fluentWait: Wait[WebDriver] = new FluentWait[WebDriver](Driver.instance)
    .withTimeout(Duration.ofSeconds(15))
    .pollingEvery(Duration.ofMillis(200))

  def selectYesAndContinue(): Unit = {
    onPage(pageUrl)
    click(yesRadioId)
    click(submitButtonId)
  }

  def selectNoAndContinue(): Unit = {
    onPage(pageUrl)
    click(noRadioId)
    click(submitButtonId)
  }

  def checkH1(h1: String): Assertion =
    getText(pageHeader) should include(h1)

  def uploadAnyFile(file: String): this.type = {
    if (file.nonEmpty) {
      val filePath      = s"${System.getProperty("user.dir")}/src/test/resources/files/$file"
      fluentWait.until(ExpectedConditions.presenceOfElementLocated(fileUploadId))
      val uploadElement = driver.findElement(fileUploadId)
      uploadElement.sendKeys(filePath)
    }
    this
  }

  def waitUntilVisible(locator: By): Unit =
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator))

  def waitForSpinnerCycle(timeoutSeconds: Int = 30): Unit = {
    val spinner = By.cssSelector("svg.ccms-loader")
    waitWith(timeoutSeconds).until(ExpectedConditions.invisibilityOfElementLocated(spinner))
  }

  def waitWith(timeoutSeconds: Int): FluentWait[WebDriver] =
    new FluentWait[WebDriver](Driver.instance)
      .withTimeout(Duration.ofSeconds(timeoutSeconds))
      .pollingEvery(Duration.ofMillis(200))

  def checkDynamicPage(): this.type = {
    onPageContaining(pageUrl)
    this
  }

  def onPageContaining(urlPart: String): this.type = {
    fluentWait.until(ExpectedConditions.urlContains(urlPart))
    this
  }

  def backToManageCrsAndFatcaReport(): this.type = {
    click(backToManageReportsLink)
    this
  }

  def clickManageFinancialInstitutionsLink(): this.type = {
    click(manageFiPage)
    this
  }

  def isOnCorrectPage: Boolean =
    driver.getCurrentUrl.contains(pageUrl)

  case class PageNotFoundException(message: String) extends Exception(message)

}
