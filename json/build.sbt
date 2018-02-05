name := NamePrefix + "json"

autoCompilerPlugins := true

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
)