// set the name
name := "Brokering"

version := "1.0"

libraryDependencies ++= Seq(
			"net.liftweb" %% "lift-json" % "2.4",
			"org.scalatest" %% "scalatest" % "1.6.1" % "test"
			)

scalacOptions += "-deprecation"