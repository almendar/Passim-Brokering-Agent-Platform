import sbt._
import Keys._

object HelloBuild extends Build {


    
    lazy val sade= Project(id = "SADE",
                           base = file("sade"))

    lazy val brokering= Project(id = "Brokering",
                           base = file("brokering")) dependsOn(sade)
						   
   lazy val root = Project(id = "Passim-Brokering-Agent-Platform",
                            base = file(".")) dependsOn(sade,brokering)
						   
	lazy val externals = Project(id = "Externals",
                           base = file("externals")) dependsOn(root)

	

}