import scala.sys.process._
import laika.rewrite.link.LinkConfig
import laika.rewrite.link.ApiLinks
import laika.theme.Theme

val scala3 = "3.1.3"

ThisBuild / tlBaseVersion := "0.1" // your current series x.y
ThisBuild / organization := "org.creativescala"
ThisBuild / organizationName := "Creative Scala"
ThisBuild / organizationHomepage := Some(url("http://creativescala.org/"))
ThisBuild / startYear := Some(2022)
// ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers := List(
  // your GitHub handle and name
  tlGitHubDev("noelwelsh", "Noel Welsh")
)
ThisBuild / tlSonatypeUseLegacyHost := true

ThisBuild / tlSitePublishBranch := Some("main")

ThisBuild / crossScalaVersions := List(scala3)
ThisBuild / scalaVersion := (ThisBuild / crossScalaVersions).value.head
ThisBuild / useSuperShell := false

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = tlCrossRootProject.aggregate(code, examples)

// Student code goes in here
lazy val code = project
  .in(file("code"))
  .settings(
    libraryDependencies ++= Seq(
      "org.creativescala" %%% "doodle" % "0.11.2",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )

// Code for examples used in the website
lazy val examples = project
  .enablePlugins(ScalaJSPlugin)
  .in(file("examples"))
  .settings(
    libraryDependencies ++= Seq(
      "org.creativescala" %%% "doodle" % "0.11.2",
      "org.creativescala" %%% "doodle-svg" % "0.11.3",
      "org.creativescala" %%% "doodle-explore" % "0.14.0"
    )
  )

lazy val css = taskKey[Unit]("Build the CSS")

// The website
lazy val docs = project
  .in(file("site"))
  .settings(
    mdocIn := file("docs/pages"),
    css := {
      val src = file("docs/css")
      val dest1 = mdocOut.value
      val dest2 = (laikaSite / target).value
      val cmd1 =
        s"npx tailwindcss -i ${src.toString}/creative-scala.css -o ${dest1.toString}/creative-scala.css"
      val cmd2 =
        s"npx tailwindcss -i ${src.toString}/creative-scala.css -o ${dest2.toString}/creative-scala.css"
      cmd1 !

      cmd2 !
    },
    Laika / sourceDirectories += file("docs/templates"),
    Laika / sourceDirectories +=
      (examples / Compile / fastOptJS / artifactPath).value
        .getParentFile() / s"${(examples / moduleName).value}-fastopt",
    laikaTheme := Theme.empty,
    laikaExtensions ++= Seq(
      laika.markdown.github.GitHubFlavor,
      laika.parse.code.SyntaxHighlighting,
      CreativeScalaDirectives
    ),
    laikaConfig := LaikaConfig.defaults
      .withConfigValue(
        "parsers.baseUrl",
        "https://creativescala.github.io/case-study-gradient-descent/index.html"
      ),
    tlSite := Def
      .sequential(
        (examples / Compile / fastOptJS),
        mdoc.toTask(""),
        css,
        laikaSite
      )
      .value
  )
  .enablePlugins(TypelevelSitePlugin)
  .dependsOn(code)
