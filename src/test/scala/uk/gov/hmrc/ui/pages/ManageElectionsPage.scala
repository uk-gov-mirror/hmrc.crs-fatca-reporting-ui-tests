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

class ManageElectionsPage(year: String = "2023", fiId: String = "TES683373303") extends BasePage {
  override val pageUrl: String = baseUrlManualSub + s"/elections/manage-elections-for-$year?fiId=$fiId"
}

object ManageElectionsPage {

  def onPage(year: String = "2023", fiId: String = "TES683373303"): ManageElectionsPage =
    new ManageElectionsPage(year, fiId)
}
