import sbt.{ Project, State }

object ShellPrompt {
  val buildShellPrompt = {
    (state: State) =>
      {
        val currProject = Project.extract(state).currentProject.id
        val v = Project.extract(state).get(sbt.Keys.version)
        s"[${currProject}|${v}] "
      }
  }
}