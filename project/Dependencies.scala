import sbt.*

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "ch.qos.logback"       % "logback-classic"     % "1.5.18" % Test,
    "com.vladsch.flexmark" % "flexmark-all"        % "0.64.8" % Test,
    "org.scalatest"       %% "scalatest"           % "3.2.19" % Test,
    "uk.gov.hmrc"         %% "ui-test-runner"      % "0.54.0" % Test,
    "uk.gov.hmrc"         %% "domain-test-play-30" % "13.0.0" % Test,
    "org.mongodb.scala"   %% "mongo-scala-driver"  % "5.1.0" cross CrossVersion.for3Use2_13
  )

}
