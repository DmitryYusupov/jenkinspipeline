import utils.os.Os
import utils.os.OsUtils

String runCommandReturningOutput(String command) {
    switch (OsUtils.getOS()) {
        case Os.WINDOWS:
            return bat(returnStdout: true, script: command).trim()

        case Os.UNIX:
            return sh(returnStdout: true, script: command).trim()

        default:
            throw new RuntimeException("No utility detected to execute gradle command '$deleteCreateImageCommand'")
    }
}

int runCommandReturningStatus(String command) {
    switch (OsUtils.getOS()) {
        case Os.WINDOWS:
            return bat(returnStatus: true, script: command)

        case Os.UNIX:
            return sh(returnStatus: true, script: command)

        default:
            throw new RuntimeException("No utility detected to execute gradle command '$deleteCreateImageCommand'")
    }
}

boolean runCommandReturningStatusAsBool(String command) {
    switch (OsUtils.getOS()) {
        case Os.WINDOWS:
            return bat(returnStatus: true, script: command) == 0

        case Os.UNIX:
            return sh(returnStatus: true, script: command) == 0

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


