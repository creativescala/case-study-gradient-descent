import scala.sys.process._
import laika.ast.LengthUnit._
import laika.ast._
import laika.helium.Helium
import laika.helium.config.Favicon
import laika.helium.config.HeliumIcon
import laika.helium.config.IconLink
import laika.helium.config.ImageLink
import TypelevelGitHubPlugin._

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
ThisBuild / scalaVersion := crossScalaVersions.value.head
ThisBuild / useSuperShell := false

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = tlCrossRootProject.aggregate(code)

lazy val code = crossProject(JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("code"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "org.creativescala" %%% "doodle" % "0.11.2",
      "org.creativescala" %%% "doodle-svg" % "0.11.2",
      "org.creativescala" %%% "doodle-explore" % "0.11.0"
    )
  )

lazy val docs = project
  .in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
  .settings(
    tlSiteRelatedProjects := Seq(
      ("Doodle", url("https://creativescala.org/doodle")),
      ("Doodle SVG", url("https://creativescala.github.io/doodle-svg")),
      ("Creative Scala", url("https://creativescala.org"))
    ),
    laikaExtensions += CreativeScalaDirectives,
    tlSiteHeliumConfig := {
      Helium.defaults.site
        .metadata(
          title = Some("Doodle Explore"),
          authors = developers.value.map(_.name),
          language = Some("en"),
          version = Some(version.value.toString)
        )
        .site
        .layout(
          contentWidth = px(860),
          navigationWidth = px(275),
          topBarHeight = px(50),
          defaultBlockSpacing = px(10),
          defaultLineHeight = 1.5,
          anchorPlacement = laika.helium.config.AnchorPlacement.Right
        )
        .site
        .darkMode
        .disabled
        .site
        .favIcons(
          Favicon.external(
            "https://typelevel.org/img/favicon.png",
            "32x32",
            "image/png"
          )
        )
        .site
        .topNavigationBar(
          homeLink = IconLink.external(
            "https://creativescala.org",
            HeliumIcon.home
          ),
          navLinks = tlSiteApiUrl.value.toList.map { url =>
            IconLink.external(
              url.toString,
              HeliumIcon.api,
              options = Styles("svg-link")
            )
          } ++ List(
            IconLink.external(
              scmInfo.value
                .fold("https://github.com/creativescala")(_.browseUrl.toString),
              HeliumIcon.github,
              options = Styles("svg-link")
            )
            // IconLink.external("https://discord.gg/XF3CXcMzqD", HeliumIcon.chat),
            // IconLink.external("https://twitter.com/typelevel", HeliumIcon.twitter)
          )
        )
    }
  )
