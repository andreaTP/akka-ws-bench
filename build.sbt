
scalaVersion := "2.13.6"

val AkkaVersion = "2.6.17"
val AkkaHttpVersion = "10.2.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core"   % "2.11.0",
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.11.0" % "compile-internal"
)

run / fork := true
Global / cancelable := true

enablePlugins(JavaServerAppPackaging)
