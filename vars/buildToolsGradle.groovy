import pipeline.stages.common.commands.GradleCommand

void call(GradleCommand command) {
    bat command.utility + " $command.getCommand()"
}

void call(Collection<GradleCommand> commands) {
    for (GradleCommand command : commands) {
        call(command)
    }
}