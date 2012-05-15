import AssemblyKeys._ // put this at the top of the file

seq(assemblySettings: _*)

//scalaVersion := "2.9.0-1"

// set the name
//name := "Brokering"

version := "1.0"

retrieveManaged := true

resolvers += "repo.codahale.com" at "http://repo.codahale.com"

libraryDependencies ++= Seq(
			"com.mongodb.casbah" %% "casbah" % "2.1.5-1",
			"org.scalatest" %% "scalatest" % "1.7.2" % "test",
			"org.apache.httpcomponents" % "httpclient" % "4.1.1",
			"com.hp.hpl.jena" % "jena" % "2.6.4",
			"com.codahale" %% "logula" % "2.1.3"
			)
			
libraryDependencies <+= scalaVersion { "org.scala-lang" % "scala-swing" % _ }


scalacOptions += "-deprecation"
