import AssemblyKeys._ // put this at the top of the file

seq(assemblySettings: _*)

scalaVersion := "2.9.2"

// set the name
//name := "Brokering"

version := "1.0"

libraryDependencies ++= Seq(
			"org.scalatest" %% "scalatest" % "1.6.1" % "test",
			"com.mongodb.casbah" %% "casbah" % "2.1.5-1"
			)

scalacOptions += "-deprecation"
