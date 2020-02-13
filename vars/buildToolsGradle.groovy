import pipeline.stages.common.commands.GradleCommand

void call(GradleCommand gradleCommand) {
    osUtils.runCommand(gradleCommand.utility + " " + gradleCommand.command)
}

void call(Collection<GradleCommand> commands) {
    for (GradleCommand command : commands) {
        call(command)
    }
}