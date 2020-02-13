import pipeline.stages.common.commands.MavenCommand

static void call(MavenCommand mavenCommand) {
    osUtils.runCommand(mavenCommand.command + mavenCommand.utility)
}

static void call(Collection<MavenCommand> commands) {
    for (MavenCommand command : commands) {
        call(command)
    }
}