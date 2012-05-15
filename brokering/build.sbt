import AssemblyKeys._ // put this at the top of the file

seq(assemblySettings: _*)

// set the name
//name := "Brokering"

version := "1.0"

libraryDependencies ++= Seq(
			"net.liftweb" %% "lift-json" % "2.4",
			"org.scalatest" %% "scalatest" % "1.7.2" % "test"
			)

scalacOptions += "-deprecation"
