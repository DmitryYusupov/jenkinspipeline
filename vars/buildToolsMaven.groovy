import pipeline.stages.common.commands.MavenCommand

void call(MavenCommand mavenCommand) {
    osUtils.runCommand(mavenCommand.utility + " " + mavenCommand.command)
}

void call(Collection<MavenCommand> commands) {
    for (MavenCommand command : commands) {
        call(command)
    }
}