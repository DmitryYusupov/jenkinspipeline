import pipeline.stages.common.commands.GradleCommand
import utils.os.Os
import utils.os.OsUtils

void call(GradleCommand gradleCommand) {
    def os = OsUtils.getOS()

    def command = gradleCommand.command
    def utility = gradleCommand.utility
    switch (os) {
        case Os.WINDOWS:
            bat "${utility} ${command}"
            break

        case Os.UNIX:
            sh utility + " $command"
            break

        default:
            throw new RuntimeException("No utility detected to execute gradle command '$command'")
    }
}

void call(Collection<GradleCommand> commands) {
    for (GradleCommand command : commands) {
        call(command)
    }
}