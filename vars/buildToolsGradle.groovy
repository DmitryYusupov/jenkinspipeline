import pipeline.stages.common.commands.GradleCommand

static void call(GradleCommand gradleCommand) {
    println("DD")
    println(gradleCommand.command + gradleCommand.utility)
    osUtils.runCommand(gradleCommand.command + gradleCommand.utility)
}

static void call(Collection<GradleCommand> commands) {
    for (GradleCommand command : commands) {
        call(command)
    }
}