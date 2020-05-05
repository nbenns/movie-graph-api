name := "movie-graph-api"

version := "0.1"

scalaVersion := "2.13.1"

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

scalacOptions += "-unchecked"

val zioVersion = "1.0.0-RC18-2"
val calibanVersion = "0.7.7"
val http4sVersion = "0.21.4"

libraryDependencies ++= Seq(
  "org.typelevel"           %% "cats-core"                 % "2.0.0",
  "org.typelevel"           %% "cats-effect"               % "2.1.2",
  "dev.zio"                 %% "zio"                       % zioVersion,
  "dev.zio"                 %% "zio-interop-cats"          % "2.0.0.0-RC12",
  "dev.zio"                 %% "zio-config"                % "1.0.0-RC17",
  "com.github.ghostdogpr"   %% "caliban"                   % calibanVersion,
  "com.github.ghostdogpr"   %% "caliban-cats"              % calibanVersion,
  "com.github.ghostdogpr"   %% "caliban-cats"              % calibanVersion,
  "com.github.ghostdogpr"   %% "caliban-http4s"            % calibanVersion,
  "org.http4s"              %% "http4s-dsl"                % http4sVersion,
  "org.http4s"              %% "http4s-server"             % http4sVersion,
  "org.http4s"              %% "http4s-blaze-server"       % http4sVersion,
  "org.http4s"              %% "http4s-prometheus-metrics" % http4sVersion,
  "dev.zio"                 %% "zio-test"                  % zioVersion,
  "dev.zio"                 %% "zio-test-sbt"              % zioVersion       % "test",
  "dev.zio"                 %% "zio-test-magnolia"         % zioVersion       % "test",
  "dev.zio"                 %% "zio-test-junit"            % zioVersion       % "test",
)

