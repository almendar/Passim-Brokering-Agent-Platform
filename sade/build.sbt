import AssemblyKeys._ // put this at the top of the file

seq(assemblySettings: _*)

// set the name
name := "SADE"

version := "1.0"

resolvers += "repo.codahale.com" at "http://repo.codahale.com"

libraryDependencies ++= Seq(
			"org.scalatest" %% "scalatest" % "1.6.1" % "test",
			"com.codahale" %% "logula" % "2.1.3"
			)

scalacOptions += "-deprecation"
