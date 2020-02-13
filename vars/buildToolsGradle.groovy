import pipeline.stages.common.commands.GradleCommand

void call(GradleCommand gradleCommand) {
    println("DD")
    println(gradleCommand.command + gradleCommand.utility)
    osUtils.runCommand(gradleCommand.command + gradleCommand.utility)
}

void call(Collection<GradleCommand> commands) {
    for (GradleCommand command : commands) {
        call(command)
    }
}