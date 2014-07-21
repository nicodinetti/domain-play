resolvers ++= Seq(
	"Nexus Public Repository" at "http://nexus.despegar.it:8080/nexus/content/groups/public",
	"Nexus Snapshots Repository" at "http://nexus.despegar.it:8080/nexus/content/repositories/snapshots",
	"Nexus Proxies Repository" at "http://nexus.despegar.it:8080/nexus/content/groups/proxies",
	"Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
	"Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.6")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.8")

