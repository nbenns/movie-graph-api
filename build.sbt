name := "movie-graph-api"

version := "0.1"

scalaVersion := "2.13.2"

addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.11.0" cross CrossVersion.full)
addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")

scalacOptions ++= Seq("-unchecked", "-Ymacro-annotations")

val zioVersion     = "1.0.0-RC18-2"
val calibanVersion = "0.7.7"
val http4sVersion  = "0.21.4"

libraryDependencies ++= Seq(
  "org.typelevel"         %% "cats-core"                 % "2.0.0",
  "org.typelevel"         %% "cats-effect"               % "2.1.2",
  "dev.zio"               %% "zio"                       % zioVersion,
  "dev.zio"               %% "zio-interop-cats"          % "2.0.0.0-RC12",
  "dev.zio"               %% "zio-config"                % "1.0.0-RC17",
  "com.github.ghostdogpr" %% "caliban"                   % calibanVersion,
  "com.github.ghostdogpr" %% "caliban-cats"              % calibanVersion,
  "com.github.ghostdogpr" %% "caliban-http4s"            % calibanVersion,
  "org.http4s"            %% "http4s-dsl"                % http4sVersion,
  "org.http4s"            %% "http4s-server"             % http4sVersion,
  "org.http4s"            %% "http4s-blaze-server"       % http4sVersion,
  "org.http4s"            %% "http4s-prometheus-metrics" % http4sVersion,
  "ch.qos.logback"        % "logback-classic"            % "1.2.3",
  "dev.zio"               %% "zio-test"                  % zioVersion % Test,
  "dev.zio"               %% "zio-test-sbt"              % zioVersion % Test,
  "dev.zio"               %% "zio-test-magnolia"         % zioVersion % Test,
  "dev.zio"               %% "zio-test-junit"            % zioVersion % Test
)
