package pipeline.stages.common.exceptions

class PipelineException extends RuntimeException {
    PipelineException() {
    }

    PipelineException(Throwable cause) {
        super(cause)
    }

    PipelineException(String message) {
        super(message)
    }
}
