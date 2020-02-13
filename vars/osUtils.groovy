import utils.os.Os
import utils.os.OsUtils

def runCommandReturningOutput(String command) {
    switch (OsUtils.getOS()) {
        case Os.WINDOWS:
            return bat(returnStdout: true, script: command).trim()

        case Os.UNIX:
            return sh(returnStdout: true, script: command).trim()

        default:
            throw new RuntimeException("No utility detected to execute gradle command '$deleteCreateImageCommand'")
    }
}


def runCommand(String command) {
    switch (OsUtils.getOS()) {
        case Os.WINDOWS:
            bat "$command"
            break

        case Os.UNIX:
            sh "$command"
            break

        default:
            throw new RuntimeException("No utility detected to execute gradle command '$deleteCreateImageCommand'")
    }
}


