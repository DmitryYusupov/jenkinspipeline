package pipeline.stages.common.exceptions

class UnknownBuildToolException extends RuntimeException {
    UnknownBuildToolException() {
        super("Unknown build tool exception")
    }
}
