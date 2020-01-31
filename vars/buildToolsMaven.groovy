import pipeline.stages.common.commands.MavenCommand
import utils.os.Os
import utils.os.OsUtils

void call(MavenCommand mavenCommand) {
    def os = OsUtils.getOS()

    def command = mavenCommand.command
    def utility = mavenCommand.utility
    switch (os) {
        case Os.WINDOWS:
            bat "gradle ${command}"
            break

        case Os.UNIX:
            sh utility + " $command"
            break

        default:
            throw new RuntimeException("No utility detected to execute gradle command '$command'")
    }
}

void call(Collection<MavenCommand> commands) {
    for (MavenCommand command : commands) {
        call(command)
    }
}