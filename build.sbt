import AssemblyKeys._ // put this at the top of the file

seq(assemblySettings: _*)

// set the name
name := "Brokering"

version := "1.0"

libraryDependencies ++= Seq(
			"net.liftweb" %% "lift-json" % "2.4",
			"org.scalatest" %% "scalatest" % "1.6.1" % "test",
			"com.mongodb.casbah" %% "casbah" % "2.1.5-1",
			"com.weiglewilczek.slf4s" %% "slf4s" % "1.0.7"
			)

scalacOptions += "-deprecation"
