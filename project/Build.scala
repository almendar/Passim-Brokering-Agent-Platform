import sbt._
import Keys._

object HelloBuild extends Build {
    lazy val root = Project(id = "Passim-Brokering-Agent-Platform",
                            base = file(".")) aggregate(sade,brokering)

    lazy val sade= Project(id = "SADE",
                           base = file("sade"))

    lazy val brokering= Project(id = "Brokering",
                           base = file("brokering")) dependsOn(sade)



}