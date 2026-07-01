/*
 * Copyright 2025 HM Revenue & Customs
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

package uk.gov.hmrc.ui.specs

import uk.gov.hmrc.ui.pages.*
import uk.gov.hmrc.ui.specs.tags.*

class FiReportingFileUploadSpec extends BaseSpec {

  Feature("Upload File Journey") {

    Scenario("Upload Journey (Standard FI)", ReportingTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")

      UploadFilePage
        .onPage()
        .fileUpload("valid-crs-fastresponseaccepted-xml.xml")
      And("They choose to send elections for the reporting period")
      ReportElectionsPage.selectYesAndContinue()

      And("They answer the CRS questions (contracts, dormant accounts, thresholds)")
      CrsContractsPage.selectYesAndContinue()
      CrsDormantAccountsPage.selectYesAndContinue()
      CrsThresholdsPage.selectYesAndContinue()

      Then("They continue from the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      Then("The user should be taken to File Successfully sent page")
      FileConfirmationPage.checkDynamicPage()
      And("The user go back to manage CRS and FATCA reports")
      FileConfirmationPage.backToManageCrsAndFatcaReport()
      Then("The user click the link to check the results of submission checks")
      ManageYourReportsHomePage.clickToCheckForYourRecentSubmissions()
      Then("The user click to check the Next step for a Passed file")
      ResultsOfSubmissionChecksPage.clickGotoConfirmation()
      And("The user navigated to File Confirmation page")
      FileConfirmationPage.checkDynamicPage()

    }

    Scenario("Upload Journey LargeFile - Standard FI)", ReportingTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")
      UploadFilePage
        .onPage()
        .fileUpload("LargeFiles/valid-crs-large-fastresponseaccepted-xml.xml")
      And("They choose to send elections for the reporting period")
      ReportElectionsPage.selectYesAndContinue()
      And("They answer the CRS questions (contracts, dormant accounts, thresholds)")
      CrsContractsPage.selectYesAndContinue()
      CrsDormantAccountsPage.selectYesAndContinue()
      CrsThresholdsPage.selectYesAndContinue()
      Then("They continue from the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      Then("The user should be taken to File Successfully sent page")
      FileConfirmationPage.checkDynamicPage()
    }

    Scenario("Upload rejected fast journey for Fi user CRS", ReportingTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")

      UploadFilePage
        .onPage()
        .fileUpload("valid-crs-fastresponserejected-xml.xml")
      And("They choose to send elections for the reporting period")
      ReportElectionsPage.selectYesAndContinue()

      And("They answer the CRS questions (contracts, dormant accounts, thresholds)")
      CrsContractsPage.selectYesAndContinue()
      CrsDormantAccountsPage.selectYesAndContinue()
      CrsThresholdsPage.selectYesAndContinue()

      Then("They continue from the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      And("The user should be taken to There is a problem with your file data page")
      RulesErrorsPage.checkDynamicPage()
      And("The user go back to manage CRS and FATCA reports")
      RulesErrorsPage.backToManageCrsAndFatcaReport()
      Then("The user click the link to check the results of submission checks")
      ManageYourReportsHomePage.clickToCheckForYourRecentSubmissions()
      Then("The user click to check the Next step for a Filed file")
      ResultsOfSubmissionChecksPage.clickCheckErrors()
      And("The user navigated to Rules-Errors page")
      RulesErrorsPage.checkDynamicPage()

    }

    Scenario("Upload Large Files - rejected fast journey for Fi user CRS", ReportingTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")

      UploadFilePage
        .onPage()
        .fileUpload("LargeFiles/valid-crs-large-fastresponserejected-xml.xml")
      And("They choose to send elections for the reporting period")
      ReportElectionsPage.selectYesAndContinue()

      And("They answer the CRS questions (contracts, dormant accounts, thresholds)")
      CrsContractsPage.selectYesAndContinue()
      CrsDormantAccountsPage.selectYesAndContinue()
      CrsThresholdsPage.selectYesAndContinue()
      Then("They continue from the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      And("The user should be taken to There is a problem with your file data page")
      RulesErrorsPage.checkDynamicPage()
    }

    Scenario("Upload slow journey accepted for Fi is user CRS", ReportingTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")

      UploadFilePage
        .onPage()
        .fileUpload("valid-crs-slowresponseaccepted-xml.xml")
      And("They choose to send elections for the reporting period")
      ReportElectionsPage.selectNoAndContinue()
      Then("They continue from the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      Then("The user lands on 'still checking your file' page")
      StillCheckingYourFilePage.waitUntilFileProcessed()
      And("The user should be taken to the File Passed checks page")
      FilePassedChecksPage.submitPage()
      And("The user should be taken to File Successfully sent page")
      FileConfirmationPage.checkDynamicPage()

    }
    Scenario("Upload Large File - slow journey accepted for Fi is user CRS", ReportingTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")

      UploadFilePage
        .onPage()
        .fileUpload("LargeFiles/valid-crs-large-slowresponseaccepted-xml.xml")
      And("They choose to send elections for the reporting period")
      ReportElectionsPage.selectNoAndContinue()
      Then("They continue from the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      Then("The user lands on 'still checking your file' page")
      StillCheckingYourFilePage.waitUntilFileProcessed()
      And("The user should be taken to the File Passed checks page")
      FilePassedChecksPage.submitPage()
      And("The user should be taken to File Successfully sent page")
      FileConfirmationPage.checkDynamicPage()

    }

    Scenario("Upload slow journey rejected for Fi is user CRS", ReportingTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")

      UploadFilePage
        .onPage()
        .fileUpload("valid-crs-slowresponserejected-xml.xml")
      And("They choose to send elections for the reporting period")
      ReportElectionsPage.selectNoAndContinue()
      Then("They continue from the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      Then("The user lands on 'still checking your file' page")
      StillCheckingYourFilePage.waitUntilFileProcessed()
      And("The user should be taken to the File Failed checks page")
      FileFailedChecksPage.submitPage()
      And("The user should be taken to There is a problem with your file data page")
      RulesErrorsPage.checkDynamicPage()
    }

    Scenario("Upload Large file - slow failed journey for Fi is user CRS", ReportingTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")
      UploadFilePage
        .onPage()
        .fileUpload("LargeFiles/valid-crs-large-slowresponserejected-xml.xml")
      And("Continues the journey for any elections made already for the reporting period")
      ReportElectionsPage.selectNoAndContinue()
      Then("They continue from the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      Then("The user lands on 'still checking your file' page")
      StillCheckingYourFilePage.waitUntilFileProcessed()
      And("The user should be taken to the File Failed checks page")
      FileFailedChecksPage.submitPage()
      And("The user should be taken to There is a problem with your file data page")
      RulesErrorsPage.checkDynamicPage()
    }

    Scenario("Upload a CRS file with large number of CRS Schema error messages", SoloTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")
      UploadFilePage
        .onPage()
        .fileUpload("crs-schema-100errors.xml")
      And("Continues the journey to check the file details")
      Then("The user navigated to data errors page with heading There is a problem with your file data page")
      DataErrorsPage.verifyAllErrors()

    }

    Scenario("Upload a CRS file with business rules-errors", SoloTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")
      UploadFilePage
        .onPage()
        .fileUpload("valid-crs-fastresponserejected-xml.xml")
      ReportElectionsPage.selectNoAndContinue()
      Then("They continue from the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      RulesErrorsPage.readAllTheRulesErrors()
    }

    Scenario("Upload a FATCA file with business rules-errors", SoloTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")
      UploadFilePage
        .onPage()
        .fileUpload("valid-fatca-fastresponserejected-xml.xml")
      Then("The user provides the GIIN if required")
      RequiredGIINPage.maybeEnterGiin()
      And("Continues the journey for any elections made already for the reporting period")
      ReportElectionsPage.selectNoAndContinue()
      Then("They continue from the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      RulesErrorsPage.readAllTheFatcaRulesErrors()
    }

    Scenario("Upload journey for Fi is user CRS and the reporting period is not within CY-12 and CY", ReportingTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")
      UploadFilePage
        .onPage()
        .fileUpload("valid-crs-CY-xml.xml")
      And("Continues the journey to Check your file details page")
      CheckYourFileDetailsPage.onPage()
    }

    Scenario("Upload fast accepted journey for Fi is user FATCA", ReportingTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")
      UploadFilePage
        .onPage()
        .fileUpload("valid-fatca-fastresponseaccepted-xml.xml")
      Then("The user provides the GIIN if required")
      RequiredGIINPage.maybeEnterGiin()
      And("Continues the journey for any elections made already for the reporting period")
      ReportElectionsPage.selectYesAndContinue()
      And("US Treasury regulations and any thresholds for FATCA")
      FatcaUSTreasuryRegulationsPage.selectYesAndContinue()
      FatcaThresholdsPage.selectYesAndContinue()
      Then("The user can review their file details and continue")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      And("The user should be taken to File Successfully sent page")
      FileConfirmationPage.checkDynamicPage()
    }

    Scenario("Upload fast rejected journey for Fi is user FATCA", ReportingTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")
      UploadFilePage
        .onPage()
        .fileUpload("valid-fatca-fastresponserejected-xml.xml")
      Then("The user provides the GIIN if required")
      RequiredGIINPage.maybeEnterGiin()
      And("Continues the journey for any elections made already for the reporting period")
      ReportElectionsPage.selectYesAndContinue()
      And("US Treasury regulations and any thresholds for FATCA")
      FatcaUSTreasuryRegulationsPage.selectYesAndContinue()
      FatcaThresholdsPage.selectYesAndContinue()
      Then("The user can review their file details and continue")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      And("The user should be taken to There is a problem with your file data page")
      RulesErrorsPage.checkDynamicPage()
    }

    Scenario("Upload slow accepted journey for Fi is user FATCA", ReportingTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")
      UploadFilePage
        .onPage()
        .fileUpload("valid-fatca-slowresponseaccepted-xml.xml")
      Then("The user provides the GIIN if required")
      RequiredGIINPage.maybeEnterGiin()
      And("Continues the journey for any elections made already for the reporting period")
      ReportElectionsPage.selectNoAndContinue()
      Then("The user can review their file details and continue")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      Then("The user lands on 'still checking your file' page")
      StillCheckingYourFilePage.waitUntilFileProcessed()
      And("The user should be taken to the File Passed checks page")
      FilePassedChecksPage.submitPage()
      And("The user should be taken to File Successfully sent page")
      FileConfirmationPage.checkDynamicPage()
      And("The user go back to manage CRS and FATCA reports")
      FileConfirmationPage.backToManageCrsAndFatcaReport()
      Then("The user click the link to check the results of submission checks")
      ManageYourReportsHomePage.clickToCheckForYourRecentSubmissions()
      Then("The user click to check the Next step for a Passed file")
      ResultsOfSubmissionChecksPage.clickGotoConfirmation()
      And("The user navigated to File Confirmation page")
      FileConfirmationPage.checkDynamicPage()

    }

    Scenario("Upload slow failed journey for Fi is user FATCA", ReportingTests) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")
      UploadFilePage
        .onPage()
        .fileUpload("valid-fatca-slowresponserejected-xml.xml")
      Then("The user provides the GIIN if required")
      RequiredGIINPage.maybeEnterGiin()
      And("Continues the journey for any elections made already for the reporting period")
      ReportElectionsPage.selectNoAndContinue()
      Then("They continue from the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      SendYourFilePage.submitFileForValidation()
      Then("The user lands on 'still checking your file' page")
      StillCheckingYourFilePage.waitUntilFileProcessed()
      And("The user should be taken to the File Failed checks page")
      FileFailedChecksPage.submitPage()
      And("The user should be taken to There is a problem with your file data page")
      RulesErrorsPage.checkDynamicPage()
      And("The user go back to manage CRS and FATCA reports")
      RulesErrorsPage.backToManageCrsAndFatcaReport()
      Then("The user click the link to check the results of submission checks")
      ManageYourReportsHomePage.clickToCheckForYourRecentSubmissions()
      Then("The user click to check the Next step for a Failed file")
      ResultsOfSubmissionChecksPage.clickCheckErrors()
      And("The user navigated to Rules-Errors page")
      RulesErrorsPage.checkDynamicPage()
    }

    Scenario(
      "Upload journey for Fi is user FATCA and the reporting period is not within CY-12 and CY",
      ReportingTests
    ) {
      AuthLoginPage.loginAsOrganisationUser()
      When("The user hits the uploading page and submits a valid XML file")
      UploadFilePage
        .onPage()
        .fileUpload("valid-fatca-CY-xml.xml")
      And("the user provides the GIIN if required")
      RequiredGIINPage.maybeEnterGiin()
      And("Continues the journey to Check your file details page")
      CheckYourFileDetailsPage.onPage()
    }

    Scenario("Upload slow journey CRS - SDES virus", ReportingTests) {

      Given("The user logs in as an organisation user")
      AuthLoginPage.loginAsOrganisationUser()
      When("The user uploads a CRS large file that triggers SDES virus result")
      UploadFilePage.onPage().fileUpload("SDES/sdesvirus_3.2MB.xml")
      And("They choose not to send elections")
      ReportElectionsPage.selectNoAndContinue()
      And("They continue to the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      And("They submit the file for validation")
      SendYourFilePage.submitFileForValidation()
      Then("The user is shown the virus detected page")
      VirusFoundPage.onPage()
      And("The user go back to manage CRS and FATCA reports")
      VirusFoundPage.backToManageCrsAndFatcaReport()
      Then("The user click the link to check the results of submission checks")
      ManageYourReportsHomePage.clickToCheckForYourRecentSubmissions()
      Then("The user click to check the Next step for a Problem file")
      ResultsOfSubmissionChecksPage.clickCheckProblem()
      And("The user navigated to Virus found page")
      VirusFoundPage.onPage()
    }

    Scenario("Upload journey CRS - SDES processing failure shows tech difficulties", ReportingTests) {

      Given("The user logs in as an organisation user")
      AuthLoginPage.loginAsOrganisationUser()
      When("The user uploads a CRS large file that triggers SDES error result")
      UploadFilePage.onPage().fileUpload("SDES/fastresponsesdes.xml") // your “error” trigger file
      And("They choose not to send elections")
      ReportElectionsPage.selectNoAndContinue()
      And("They continue to the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      And("They submit the file for validation")
      SendYourFilePage.submitFileForValidation()
      Then("The user is shown the technical difficulties page")
      ThereIsAProblemPage.onPage()
      Then("The user click the header content to navigate back to manage your reports")
      ThereIsAProblemPage.clickManageFinancialInstitutionsLink()
      Then("The user click the link to check the results of submission checks")
      ManageYourReportsHomePage.clickToCheckForYourRecentSubmissions()
      Then("The user click to check the Next step for a Problem file")
      ResultsOfSubmissionChecksPage.clickUploadFileAgain()
      And("The user navigated back to file upload page")
      UploadFilePage.onPage()
    }

    Scenario("Upload journey FATCA - File Unexpected - Not accepted", ReportingTests) {
      Given("The user logs in as an organisation user")
      AuthLoginPage.loginAsOrganisationUser()
      When("The user uploads a FATCA file that triggers unexpected - not accepted result")
      UploadFilePage.onPage().fileUpload("valid-fatca-fastunexpected_notaccepted.xml")
      Then("The user provides the GIIN if required")
      RequiredGIINPage.maybeEnterGiin()
      And("They choose not to send elections")
      ReportElectionsPage.selectNoAndContinue()
      And("They continue to the Check your file details page")
      CheckYourFileDetailsPage.submitPage()
      And("They submit the file for validation")
      SendYourFilePage.submitFileForValidation()
      Then("The user is shown the unexpected file not accepted page")
      FileNotAcceptedPage.checkDynamicPage()
      Then("The user click the header content to navigate back to manage your reports")
      FileNotAcceptedPage.clickManageFinancialInstitutionsLink()
      Then("The user click the link to check the results of submission checks")
      ManageYourReportsHomePage.clickToCheckForYourRecentSubmissions()
      Then("The user click to check the Next step for a Problem file")
      ResultsOfSubmissionChecksPage.clickContactUs()
      And("The Contact us take the user back to file not accepted page ")
      FileNotAcceptedPage.checkDynamicPage()

    }
  }
}
