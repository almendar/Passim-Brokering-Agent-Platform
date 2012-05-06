import AssemblyKeys._ // put this at the top of the file

seq(assemblySettings: _*)

//scalaVersion := "2.9.0-1"

// set the name
//name := "Brokering"

version := "1.0"

libraryDependencies ++= Seq(
			"org.scalatest" %% "scalatest" % "1.6.1" % "test",
			"com.mongodb.casbah" %% "casbah" % "2.1.5-1",
			"org.apache.httpcomponents" % "httpclient" % "4.1.1",
			"com.hp.hpl.jena" % "jena" % "2.6.4",
			"org.slf4j" % "slf4j-simple" % "1.6.1"
			)
			
libraryDependencies <+= scalaVersion { "org.scala-lang" % "scala-swing" % _ }


scalacOptions += "-deprecation"
