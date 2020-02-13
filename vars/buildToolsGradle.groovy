import pipeline.stages.common.commands.GradleCommand

static void call(GradleCommand gradleCommand) {
    osUtils.runCommand(gradleCommand.command + gradleCommand.utility)
}

static void call(Collection<GradleCommand> commands) {
    for (GradleCommand command : commands) {
        call(command)
    }
}