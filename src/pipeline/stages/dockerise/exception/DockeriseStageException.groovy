package pipeline.stages.dockerise.exception

import pipeline.stages.common.exceptions.PipelineException

class DockeriseStageException extends PipelineException {

    DockeriseStageException() {
    }

    DockeriseStageException(Throwable cause) {
        super(cause)
    }

    DockeriseStageException(String message) {
        super(message)
    }
}
